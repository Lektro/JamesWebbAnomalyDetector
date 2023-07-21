package org.example.processing;

import nom.tam.fits.BasicHDU;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.ImageHDU;
import org.example.models.FitsImage;
import org.example.models.HotPixel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.example.processing.HotPixelDetector.getMaxPixelValue;
import static org.example.processing.HotPixelDetector.getMinPixelValue;

public class AnomalyDetectionProcessor {

    // Fields to store the paths and parameters for processing
    final String fitsFilePath;
    final String outputFolder;
    final double hotPixelThreshold;
    final int hotPixelMarkerSize;

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
        try {
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

            // Check if the primary HDU is not an ImageHDU, and handle the error
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
        int imageHeight = imageHDU.getAxes()[0];

        // Loop through all pixels in the image and detect hot pixels
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                double pixelValue = HotPixelDetector.getPixelValue(data, x, y);
                if (pixelValue >= hotPixelThreshold) {
                    hotPixels.add(new HotPixel(x, y, pixelValue));
                }
            }
        }

        // Initialize enhancedData with the original data
        double[][] enhancedData = data;

        // Apply contrast stretching to enhance the image
        enhancedData = ImageEnhancer.contrastStretch(enhancedData);

        // Apply sharpening to enhance the image
        enhancedData = ImageEnhancer.sharpen(enhancedData);

        // Apply de-noising to enhance the image
        enhancedData = ImageEnhancer.denoise(enhancedData);


        // Print the number of hot pixels detected and their coordinates and values in the console
        System.out.println("Number of hot pixels detected: " + hotPixels.size());
        for (HotPixel hotPixel : hotPixels) {
            System.out.println("Hot pixel at (x=" + hotPixel.getX() + ", y=" + hotPixel.getY() +
                    ") with value: " + hotPixel.getValue());
        }

        // If hot pixels are detected, save the processed images and the original
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
            saveFitsImageAsJpeg(enhancedData, imageWidth, imageHeight, enhancedImageFilePath);

            // debug: see if the image was processed and saved in the console
            System.out.println("Images for file '" + fileName + "' processed and saved.");

        } else {

            // If no hot pixels are detected, save the original image as JPEG only
            System.out.println("No hot pixels detected in the image.");

            // Extract the file name and remove the file extension to create a new output folder
            String fileName = new File(fitsFilePath).getName();

            String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
            String fileOutputFolder = outputFolder + "/" + fileNameWithoutExtension;

            // Create a file object for the output folder
            File fileOutputDir = new File(fileOutputFolder);

            // Create a folder for each file in the output directory if it doesn't exist
            if (!fileOutputDir.exists()) {
                fileOutputDir.mkdir();
            }

            // Save the original image as JPEG in the output folder
            String originalImageFilePath = fileOutputFolder + "/original_" + fileNameWithoutExtension + ".jpg";

            // Check the length of the data array in terminal
            System.out.println("Data array length: " + data.length);

            // Call to Utility Method to save the image in the output folder
            saveFitsImageAsJpeg(data, imageWidth, imageHeight, originalImageFilePath);

            // Print save status message in terminal
            System.out.println("Images for file '" + fileName + "' processed and saved.");}

        } catch (FitsException e) {

            // Handle the FitsException here, for example, log the error or show an error message
            e.printStackTrace();

        } catch (IOException e) {
            // Handle any other IOException that might occur during file handling
            e.printStackTrace();
        }
    }

    /**
     * Utility method to convert FITS data to double[][] so we can find the pixel locations
     * Converts the FITS data object to a 2D double array for processing.
     *
     * @param dataObject The FITS data object to be converted.
     * @return A 2D double array representing the pixel values of the image.
     *         If the input dataObject is of type double[][], it is returned directly.
     *         If the input dataObject is of type float[][], it is converted to a double[][].
     *         Returns null if the input dataObject is of an unsupported type.
     */
    private static double[][] convertToDoubleArray(Object dataObject) {

        // Check if the dataObject is of type double[][] (already a double array)
        if (dataObject instanceof double[][]) {
            return (double[][]) dataObject;

        // Check if the dataObject is of type float[][] (needs conversion to double[][])
        } else if (dataObject instanceof float[][]) {

            // Read floatData
            float[][] floatData = (float[][]) dataObject;
            int rows = floatData.length;
            int cols = floatData[0].length;

            // Create a new 2D double array to hold the converted pixel values
            double[][] doubleData = new double[rows][cols];

            // Convert each pixel value from float to double and store in the new array
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    doubleData[i][j] = floatData[i][j];
                }
            }
            // Return the converted double array
            return doubleData;
        }
        // Return null if the dataObject is of an unsupported type
        return null;
    }

    /**
     * Utility method to save the FITS image as JPEG
     * Saves the FITS image as a JPEG file with the specified pixel data and dimensions.
     *
     * @param data          The 2D array containing the pixel values of the FITS image.
     * @param width         The width of the image in pixels.
     * @param height        The height of the image in pixels.
     * @param outputFilePath The file path where the JPEG image will be saved.
     */
    private static void saveFitsImageAsJpeg(double[][] data, int width, int height, String outputFilePath) {

        // Create a new BufferedImage with the specified width and height
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Find the minimum and maximum pixel values in the data array
        double minValue = getMinPixelValue(data);
        double maxValue = getMaxPixelValue(data);

        // Define the color scale for mapping pixel values to RGB colors
        Color[] colorScale = createNaturalColorScale();

        // Get the height and width of the 2D data array
        int dataHeight = data.length;
        int dataWidth = data[0].length;

        // Loop through each pixel of the image and set its RGB value based on the pixel value in the data array
        for (int y = 0; y < height && y < dataHeight; y++) {
            for (int x = 0; x < width && x < dataWidth; x++) {
                double pixelValue = data[y][x];
                int rgbValue = scalePixelValueToRGB(pixelValue, minValue, maxValue, colorScale);
                image.setRGB(x, y, rgbValue);
            }
        }

        try {
            // You can change the format to PNG, BMP, etc.
            String imageFormat = "JPEG";

            // Create a new File object representing the output file path
            File outputFile = new File(outputFilePath);

            // Write the BufferedImage as a your chosen image format to the output file
            ImageIO.write(image, imageFormat, outputFile);

            // Print a message to indicate that the image has been saved successfully
            System.out.println("Image saved as '" + outputFilePath + "'.");

        } catch (IOException e) {
            // If an exception occurs during image saving, print the stack trace for debugging
            e.printStackTrace();
        }
    }

    // Utility method to map the pixel value to an RGB color based on the color scale
    private static int scalePixelValueToRGB(double pixelValue, double minValue, double maxValue, Color[] colorScale) {
        int colorScaleSteps = colorScale.length - 1;
        double normalizedValue = (pixelValue - minValue) / (maxValue - minValue);
        int colorIndex = (int) (normalizedValue * colorScaleSteps);
        colorIndex = Math.min(colorIndex, colorScaleSteps - 1);
        return colorScale[colorIndex].getRGB();
    }

    // Utility method to create a natural grayscale color scale with a slight blue tint
    private static Color[] createNaturalColorScale() {
        int numColors = 256;
        Color[] colorScale = new Color[numColors];
        for (int i = 0; i < numColors; i++) {
            int value = i;
            int r = value;
            int g = value;

            // Add a slight blue tint to the grayscale
            int b = Math.min(value + 30, 255);

            colorScale[i] = new Color(r, g, b);
        }
        return colorScale;
    }
}
