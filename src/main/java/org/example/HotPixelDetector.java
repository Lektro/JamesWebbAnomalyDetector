package org.example;

public class HotPixelDetector {

    // Utility methods
    public double getPixelValue(Object data, int x, int y) throws PixelValueException {
        if (data instanceof double[][]) {
            double[][] doubleData = (double[][]) data;
            if (y >= 0 && y < doubleData.length && x >= 0 && x < doubleData[y].length) {
                return doubleData[y][x];
            }
        } else if (data instanceof float[][]) {
            float[][] floatData = (float[][]) data;
            if (y >= 0 && y < floatData.length && x >= 0 && x < floatData[y].length) {
                return floatData[y][x];
            }
        } else if (data instanceof int[][]) {
            int[][] intData = (int[][]) data;
            if (y >= 0 && y < intData.length && x >= 0 && x < intData[y].length) {
                return intData[y][x];
            }
        }

        throw new PixelValueException("Error: Invalid coordinates or unsupported data type.");
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

    static double getPixelValue(double[][] data, int x, int y) {
        if (y >= 0 && y < data.length && x >= 0 && x < data[y].length) {
            return data[y][x];
        }
        // Default value for out-of-bounds cases
        return 0.0;
    }
    public class PixelValueException extends Exception {
        public PixelValueException(String message) {
            super(message);
        }
    }
}
