package org.example;

import nom.tam.fits.BasicHDU;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.ImageHDU;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.example.HotPixelDetector.getMaxPixelValue;
import static org.example.HotPixelDetector.getMinPixelValue;
import static org.example.HotPixelMarker.scalePixelValueToRGB;

public class AnomalyDetectionProcessor {

    private String fitsFilePath;
    private String outputFolder;
    private double hotPixelThreshold;
    private int hotPixelMarkerSize;

    public AnomalyDetectionProcessor(String fitsFilePath, String outputFolder,
                                     double hotPixelThreshold, int hotPixelMarkerSize) {
        this.fitsFilePath = fitsFilePath;
        this.outputFolder = outputFolder;
        this.hotPixelThreshold = hotPixelThreshold;
        this.hotPixelMarkerSize = hotPixelMarkerSize;
    }

    public void processAndSaveImages() throws FitsException, IOException {
        Fits fits = new Fits(fitsFilePath);

        int numHDUs = fits.getNumberOfHDUs();
        for (int i = 0; i < numHDUs; i++) {
            BasicHDU<?> hdu = fits.getHDU(i);
            System.out.println("HDU " + i + " type: " + hdu.getClass().getSimpleName());
            if (hdu instanceof ImageHDU) {
                ImageHDU imageHDU = (ImageHDU) hdu;
                System.out.println("Image axes: " + imageHDU.getAxes()[0] + " x " + imageHDU.getAxes()[1]);
            }
        }

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

        List<HotPixel> hotPixels = new ArrayList<>();
        int imageWidth = imageHDU.getAxes()[1];
        int imageHeight = imageHDU.getAxes()[1];

        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                double pixelValue = HotPixelDetector.getPixelValue(data, x, y);
                if (pixelValue >= hotPixelThreshold) {
                    hotPixels.add(new HotPixel(x, y, pixelValue));
                }
            }
        }

        double[][] enhancedImage = ImageEnhancer.contrastStretch(data);

        System.out.println("Number of hot pixels detected: " + hotPixels.size());
        for (HotPixel hotPixel : hotPixels) {
            System.out.println("Hot pixel at (x=" + hotPixel.getX() + ", y=" + hotPixel.getY() +
                    ") with value: " + hotPixel.getValue());
        }

        if (!hotPixels.isEmpty()) {
            String fileName = new File(fitsFilePath).getName();
            String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
            String fileOutputFolder = outputFolder + "/" + fileNameWithoutExtension;
            File fileOutputDir = new File(fileOutputFolder);

            if (!fileOutputDir.exists()) {
                fileOutputDir.mkdir();
            }

            String markedImageFilePath = fileOutputFolder + "/marked_" + fileNameWithoutExtension + ".jpg";
            FitsImage fitsImage = new FitsImage(data, imageWidth, imageHeight, hotPixels);
            HotPixelMarker.markHotPixelsAndSave(fitsImage, hotPixelMarkerSize, markedImageFilePath);

            String originalImageFilePath = fileOutputFolder + "/original_" + fileNameWithoutExtension + ".jpg";
            saveFitsImageAsJpeg(data, imageWidth, imageHeight, originalImageFilePath);

            String enhancedImageFilePath = fileOutputFolder + "/enhanced_" + fileNameWithoutExtension + ".jpg";
            saveFitsImageAsJpeg(enhancedImage, imageWidth, imageHeight, enhancedImageFilePath);

            System.out.println("Images for file '" + fileName + "' processed and saved.");

        } else {
            System.out.println("No hot pixels detected in the image.");

            String fileName = new File(fitsFilePath).getName();
            String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
            String fileOutputFolder = outputFolder + "/" + fileNameWithoutExtension;
            File fileOutputDir = new File(fileOutputFolder);
            if (!fileOutputDir.exists()) {
                fileOutputDir.mkdir();
            }
            String originalImageFilePath = fileOutputFolder + "/original_" + fileNameWithoutExtension + ".jpg";
            saveFitsImageAsJpeg(data, imageWidth, imageHeight, originalImageFilePath);
            System.out.println("Images for file '" + fileName + "' processed and saved.");
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

        int dataHeight = data.length; // Assuming 'data' is a 2D array
        int dataWidth = data[0].length; // Assuming all rows have the same width

        for (int y = 0; y < height && y < dataHeight; y++) {
            for (int x = 0; x < width && x < dataWidth; x++) {
                double pixelValue = data[y][x];
                int rgbValue = scalePixelValueToRGB(pixelValue, minValue, maxValue);
                image.setRGB(x, y, rgbValue);
            }
        }

        try {
            // You can change the format to PNG, BMP, etc.
            String imageFormat = "JPEG"; // You can change the format to PNG, BMP, etc.
            java.io.File outputFile = new java.io.File(outputFilePath);
            javax.imageio.ImageIO.write(image, imageFormat, outputFile);
            System.out.println("Image saved as '" + outputFilePath + "'.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
