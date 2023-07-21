package org.example.processing;

import org.example.models.HotPixel;

import java.util.ArrayList;
import java.util.List;

public class HotPixelDetector {

    // Method to find the maximum pixel value in the 2D data array
    static double getMaxPixelValue(Object data) {
        if (data == null || !(data instanceof double[][])) {

            // Handle the case when data is null or not a valid 2D array
            return Double.NaN; // Return a special value to indicate invalid data
        }

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
        if (data == null || data.length == 0 || data[0] == null || data[0].length == 0) {

            // Handle the case when data is null or empty
            return Double.NaN; // Return a special value to indicate invalid data
        }

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

    // Method to detect hot pixels
    public static List<HotPixel> detectHotPixels(double[][] data, double hotPixelThreshold) {
        List<HotPixel> hotPixels = new ArrayList<>();

        int imageWidth = data[0].length;
        int imageHeight = data.length;

        // Loop through all pixels in the image and detect hot pixels
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                double pixelValue = data[y][x];
                if (pixelValue >= hotPixelThreshold) {
                    hotPixels.add(new HotPixel(x, y, pixelValue));
                }
            }
        }

        return hotPixels;
    }
}
