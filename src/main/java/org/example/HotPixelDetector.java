package org.example;

public class HotPixelDetector {

    // Method to find the maximum pixel value in the 2D data array
    static double getMaxPixelValue(Object data) {
        double max = Double.MIN_VALUE;
        double[][] dataArray = (double[][]) data;
        for (double[] row : dataArray) {
            for (double value : row) {
                if (value > max) {
                    max = value;
                }
            }
        }
        return max;
    }

    // Method to find the minimum pixel value in the 2D data array
    public static double getMinPixelValue(double[][] data) {
        double minValue = Double.MAX_VALUE;
        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[y].length; x++) {
                double pixelValue = getPixelValue(data, x, y);
                if (pixelValue < minValue) {
                    minValue = pixelValue;
                }
            }
        }
        return minValue;
    }

    // Method to get the pixel value from the 2D data array at the specified (x, y) coordinates
    static double getPixelValue(double[][] data, int x, int y) {
        if (data != null && y >= 0 && y < data.length && x >= 0 && x < data[y].length) {
            return data[y][x];
        }
        // Default value for out-of-bounds cases
        return 0.0;
    }
}
