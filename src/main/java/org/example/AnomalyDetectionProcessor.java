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

    // Fields to store the paths and parameters for processing
    private String fitsFilePath;
    private String outputFolder;
    private double hotPixelThreshold;
    private int hotPixelMarkerSize;

    // Constructor to initialize the AnomalyDetectionProcessor object
    public AnomalyDetectionProcessor(String fitsFilePath, String outputFolder,
                                     double hotPixelThreshold, int hotPixelMarkerSize) {
        this.fitsFilePath = fitsFilePath;
        this.outputFolder = outputFolder;
        this.hotPixelThreshold = hotPixelThreshold;
        this.hotPixelMarkerSize = hotPixelMarkerSize;
    }

    // Method to process and save the images
    public void processAndSaveImages() throws FitsException, IOException {
        // Create a Fits object to read the FITS file
        Fits fits = new Fits(fitsFilePath);

        // Get the number of Header Data Units (HDUs) in the FITS file
        int numHDUs = fits.getNumberOfHDUs();

        // Loop through all HDUs in the FITS file
        for (int i = 0; i < numHDUs; i++) {
            BasicHDU<?> hdu = fits.getHDU(i);
            System.out.println("HDU " + i + " type: " + hdu.getClass().getSimpleName());

            // If the HDU is an ImageHDU, print its image axes
            if (hdu instanceof ImageHDU) {
                ImageHDU imageHDU = (ImageHDU) hdu;
                System.out.println("Image axes: " + imageHDU.getAxes()[0] + " x " + imageHDU.getAxes()[1]);
            }
        }

        // Get the primary HDU, which is typically the second HDU in the FITS file (index 1)
        BasicHDU<?> hdu = fits.getHDU(1);

        // Check if the primary HDU is null or not an ImageHDU
        if (hdu == null) {
            System.err.println("Error: FITS imageHDU is null or empty.");
            return;
        }

        if (!(hdu instanceof ImageHDU)) {
            System.err.println("Error: The primary HDU is not an ImageHDU.");
            return;
        }

        // Cast the primary HDU to an ImageHDU
        ImageHDU imageHDU = (ImageHDU) hdu;

        // Get the data from the ImageHDU as a 2D array
        Object dataObject = imageHDU.getData().getData();
        double[][] data = convertToDoubleArray(dataObject);

        // Create a list to store the detected hot pixels
        List<HotPixel> hotPixels = new ArrayList<>();

        // Get the dimensions of the image
        int imageWidth = imageHDU.getAxes()[1];
        int imageHeight = imageHDU.getAxes()[1];

        // Loop through all pixels in the image and detect hot pixels
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                double pixelValue = HotPixelDetector.getPixelValue(data, x, y);
                if (pixelValue >= hotPixelThreshold) {
                    hotPixels.add(new HotPixel(x, y, pixelValue));
                }
            }
        }

        // Enhance the image using contrast stretching
        double[][] enhancedImage = ImageEnhancer.contrastStretch(data);

        // Print the number of hot pixels detected and their coordinates and values in the console
        System.out.println("Number of hot pixels detected: " + hotPixels.size());
        for (HotPixel hotPixel : hotPixels) {
            System.out.println("Hot pixel at (x=" + hotPixel.getX() + ", y=" + hotPixel.getY() +
                    ") with value: " + hotPixel.getValue());
        }

        // If hot pixels are detected, save the processed images
        if (!hotPixels.isEmpty()) {

            String fileName = new File(fitsFilePath).getName();
            String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
            String fileOutputFolder = outputFolder + "/" + fileNameWithoutExtension;
            File fileOutputDir = new File(fileOutputFolder);

            // Create a folder for each file in the output directory if it doesn't exist
            if (!fileOutputDir.exists()) {
                fileOutputDir.mkdir();
            }

            // File paths for the marked, original, and enhanced images
            String markedImageFilePath = fileOutputFolder + "/marked_" + fileNameWithoutExtension + ".jpg";
            String originalImageFilePath = fileOutputFolder + "/original_" + fileNameWithoutExtension + ".jpg";
            String enhancedImageFilePath = fileOutputFolder + "/enhanced_" + fileNameWithoutExtension + ".jpg";

            // Create a FitsImage object to hold the data and hot pixels for the marked image
            FitsImage fitsImage = new FitsImage(data, imageWidth, imageHeight, hotPixels);

            // Mark hot pixels in the image and save the marked image
            HotPixelMarker.markHotPixelsAndSave(fitsImage, hotPixelMarkerSize, markedImageFilePath);

            // Save the original image as JPEG
            saveFitsImageAsJpeg(data, imageWidth, imageHeight, originalImageFilePath);

            // Save the enhanced image as JPEG
            saveFitsImageAsJpeg(enhancedImage, imageWidth, imageHeight, enhancedImageFilePath);

            // debug: see if the image was processed and saved in the console
            System.out.println("Images for file '" + fileName + "' processed and saved.");

        } else {
            // If no hot pixels are detected, save the original image as JPEG only
            System.out.println("No hot pixels detected in the image.");

            String fileName = new File(fitsFilePath).getName();
            String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
            String fileOutputFolder = outputFolder + "/" + fileNameWithoutExtension;
            File fileOutputDir = new File(fileOutputFolder);

            // Create a folder for each file in the output directory if it doesn't exist
            if (!fileOutputDir.exists()) {
                fileOutputDir.mkdir();
            }

            // Save the original image as JPEG
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
