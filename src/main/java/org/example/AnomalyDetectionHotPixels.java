package org.example;

import nom.tam.fits.BasicHDU;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.ImageHDU;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.example.HotPixelDetector.getMaxPixelValue;
import static org.example.HotPixelDetector.getMinPixelValue;
import static org.example.HotPixelMarker.scalePixelValueToRGB;

public class AnomalyDetectionHotPixels {

    public static void main(String[] args) {
        // Replace "path/to/your/image.fits" with the actual file path of the FITS image
        String fitsFilePath = "C:/Temp/jw02107028001_02101_00001_nrcb4_cal.fits";

        // Replace this value with an appropriate threshold
        double hotPixelThreshold = 550.0;

        // Adjust the marker size as needed
        int hotPixelMarkerSize = 15;

        try {
            // Step 1: Read FITS image data
            Fits fits = new Fits(fitsFilePath);

            // Debug information: Print the number of HDUs in the FITS file
            int numHDUs = fits.getNumberOfHDUs();
            System.out.println("Number of HDUs in the FITS file: " + numHDUs);

            // Print information about each HDU in the FITS file
            for (int i = 0; i < numHDUs; i++) {
                BasicHDU<?> hdu = fits.getHDU(i);
                System.out.println("HDU " + i + " type: " + hdu.getClass().getSimpleName());
                if (hdu instanceof ImageHDU) {
                    ImageHDU imageHDU = (ImageHDU) hdu;
                    System.out.println("Image axes: " + imageHDU.getAxes()[0] + " x " + imageHDU.getAxes()[1]);
                }
            }

            // Get the primary HDU index starts at 1 , not 0
            BasicHDU<?> hdu = fits.getHDU(1);
            if (hdu == null) {
                System.err.println("Error: FITS imageHDU is null or empty.");
                return;
            }

            if (!(hdu instanceof ImageHDU)) {
                System.err.println("Error: The primary HDU is not an ImageHDU.");
                return;
            }

            ImageHDU imageHDU = (ImageHDU) hdu;
            Object dataObject = imageHDU.getData().getData();
            double[][] data = convertToDoubleArray(dataObject);

            // Ensure data is not null before proceeding
            if (data == null) {
                System.err.println("Error: FITS data is null or empty.");
                return;
            }

            // Step 2: Identify hot pixels (simple anomaly detection example)
            List<HotPixel> hotPixels = new ArrayList<>();

            int imageWidth = imageHDU.getAxes()[1];
            int imageHeight = imageHDU.getAxes()[1];
            for (int y = 0; y < imageHeight; y++) {
                for (int x = 0; x < imageWidth; x++) {
                    double pixelValue = getPixelValue(data, x, y);
                    if (pixelValue > hotPixelThreshold) {
                        hotPixels.add(new HotPixel(x, y, pixelValue));
                    }
                }
            }

            // Enhance the image (choose the desired enhancement method)
            double[][] enhancedImage = ImageEnhancer.contrastStretch(data); // or ImageEnhancer.histogramEqualization(data);

            // Subtract background from the enhanced image
            double[][] backgroundSubtractedImage = BackgroundSubtractor.subtractBackground(enhancedImage);

            // Calibrate the background-subtracted image
            double[][] calibratedImage = ImageCalibrator.calibrateImage(backgroundSubtractedImage);


            // Output detected hot pixels to console
            System.out.println("Number of hot pixels detected: " + hotPixels.size());
            for (HotPixel hotPixel : hotPixels) {
                System.out.println("Hot pixel at (x=" + hotPixel.getX() + ", y=" + hotPixel.getY() +
                        ") with value: " + hotPixel.getValue());
            }

            // Step 3: Process hot pixels and save the marked image
            if (!hotPixels.isEmpty()) {
                FitsImage fitsImage = new FitsImage(data, imageWidth, imageHeight, hotPixels);
                HotPixelMarker.markHotPixelsAndSave(fitsImage, 15, "C:/Temp/marked_image.jpg");

                // Save the original image as JPEG
                saveFitsImageAsJpeg(data, imageWidth, imageHeight, "C:/Temp/original_image.jpg");


            } else {
                System.out.println("No hot pixels detected in the image.");
            }


        } catch (FitsException | IOException e) {
            e.printStackTrace();
        }
    }


    // Utility method to get the pixel value from the image data
    private static double getPixelValue(Object data, int x, int y) {
        if (data instanceof double[][]) {
            double[][] doubleData = (double[][]) data;
            return doubleData[y][x];
        } else if (data instanceof float[][]) {
            float[][] floatData = (float[][]) data;
            return floatData[y][x];
        } else if (data instanceof int[][]) {
            int[][] intData = (int[][]) data;
            return intData[y][x];
        } else if (data instanceof short[][]) {
            short[][] shortData = (short[][]) data;
            return shortData[y][x];
        } else if (data instanceof byte[][]) {
            byte[][] byteData = (byte[][]) data;
            return byteData[y][x];
        } else {
            throw new IllegalArgumentException("Unsupported data type: " + data.getClass().getName());
        }
    }

    // Utility method to convert FITS data to double[][] so we can find the pixel locations
    private static double[][] convertToDoubleArray(Object dataObject) {
        if (dataObject instanceof double[][]) {
            return (double[][]) dataObject;
        } else if (dataObject instanceof float[][]) {
            float[][] floatData = (float[][]) dataObject;
            int rows = floatData.length;
            int cols = floatData[0].length;
            double[][] doubleData = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    doubleData[i][j] = floatData[i][j];
                }
            }
            return doubleData;
        }
        return null;
    }

    // Utility method to save the FITS image as JPEG
    private static void saveFitsImageAsJpeg(double[][] data, int width, int height, String outputFilePath) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        double minValue = getMinPixelValue(data);
        double maxValue = getMaxPixelValue(data);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double pixelValue = data[y][x];
                int rgbValue = scalePixelValueToRGB(pixelValue, minValue, maxValue);
                image.setRGB(x, y, rgbValue);
            }
        }

        try {
            String imageFormat = "JPEG"; // You can change the format to PNG, BMP, etc.
            java.io.File outputFile = new java.io.File(outputFilePath);
            javax.imageio.ImageIO.write(image, imageFormat, outputFile);
            System.out.println("Original image saved as '" + outputFilePath + "'.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}