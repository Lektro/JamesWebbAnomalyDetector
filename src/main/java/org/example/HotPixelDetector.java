package org.example;

public class HotPixelDetector {

    // Utility methods
    static double getPixelValue(Object data, int x, int y) {
        // Assuming data is a 2D array of double values
        return ((double[][]) data)[y][x];
    }

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

    static double getMinPixelValue(Object data) {
        double min = Double.MAX_VALUE;
        double[][] dataArray = (double[][]) data;
        for (double[] row : dataArray) {
            for (double value : row) {
                if (value < min) {
                    min = value;
                }
            }
        }
        return min;
    }
}
