package org.example;

public class ImageEnhancer {
    public static double[][] contrastStretch(double[][] data) {
        // Find the minimum and maximum pixel values in the data
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (double[] row : data) {
            for (double pixelValue : row) {
                if (pixelValue < min) {
                    min = pixelValue;
                }
                if (pixelValue > max) {
                    max = pixelValue;
                }
            }
        }

        // Apply contrast stretching to enhance the image
        double[][] enhancedData = new double[data.length][data[0].length];
        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[0].length; x++) {
                double pixelValue = data[y][x];
                enhancedData[y][x] = (pixelValue - min) / (max - min) * 255.0;
            }
        }

        return enhancedData;

    }

    // Add other image enhancement methods as needed
}
