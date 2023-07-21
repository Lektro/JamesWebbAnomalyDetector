/*
package org.example;

public class BackgroundSubtractor {
    public static double[][] subtractBackground(double[][] data) {
        // Compute the average or median background value
        double backgroundValue = computeBackgroundValue(data);

        // Subtract the background value from the image data
        double[][] subtractedData = new double[data.length][data[0].length];
        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[0].length; x++) {
                subtractedData[y][x] = data[y][x] - backgroundValue;
            }
        }

        return subtractedData;
    }

    private static double computeBackgroundValue(double[][] data) {
        // Implement the logic to compute the background value
        // This may involve using a statistical method like median or mean
        // For simplicity, let's assume we use the mean here:
        double sum = 0.0;
        for (double[] row : data) {
            for (double pixelValue : row) {
                sum += pixelValue;
            }
        }
        return sum / (data.length * data[0].length);
    }

    // Add other background subtraction methods as needed
}
*/
