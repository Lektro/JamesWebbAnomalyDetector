package org.example;

public class HotPixelDetector {

    // Utility methods
    static double getPixelValue(Object data, int x, int y) {
        if (data instanceof double[][]) {
            double[][] doubleData = (double[][]) data;
            if (y >= 0 && y < doubleData.length && x >= 0 && x < doubleData[y].length) {
                return doubleData[y][x];
            }
        }

        // Handle other data types or out-of-bounds cases if necessary
        System.err.println("Error: Invalid coordinates or unsupported data type.");
        return 0.0;
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
