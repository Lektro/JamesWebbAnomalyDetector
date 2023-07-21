package org.example;

public class ImageEnhancer {

    // Method for contrast stretching to enhance image quality
    public static double[][] contrastStretch(double[][] data) {

        // Check if the image data is valid (not null and has non-zero dimensions)
        if (data == null || data.length == 0 || data[0].length == 0) {
            System.err.println("Error: Invalid image data for contrast stretching.");
            return null;
        }

        // Find the minimum and maximum pixel values in the data
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        // Loop through each row of the data array
        for (double[] row : data) {
            // Loop through each pixel value in the row
            for (double pixelValue : row) {

                // Find the minimum pixel value in the data
                if (pixelValue < min) {
                    min = pixelValue;
                }
                // Find the maximum pixel value in the data
                if (pixelValue > max) {
                    max = pixelValue;
                }
            }
        }

        // Apply contrast stretching to enhance the image
        double[][] enhancedData = new double[data.length][data[0].length];

        // Loop through each row of the data array
        for (int y = 0; y < data.length; y++) {

            // Loop through each pixel value in the row
            for (int x = 0; x < data[0].length; x++) {

                // Get the pixel value at the current location
                double pixelValue = data[y][x];

                // Apply contrast stretching formula to enhance the pixel value
                enhancedData[y][x] = (pixelValue - min) / (max - min) * 255.0;
            }
        }

        // Return the enhanced data array
        return enhancedData;

    }
    // Add other image enhancement methods as needed

}
