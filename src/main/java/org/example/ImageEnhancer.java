package org.example;

import java.util.ArrayList;
import java.util.List;

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
    // Sharpening method using a simple Laplacian kernel
    public static double[][] sharpen(double[][] data) {
        if (data == null || data.length == 0 || data[0].length == 0) {
            System.err.println("Error: Invalid image data for sharpening.");
            return null;
        }

        int height = data.length;
        int width = data[0].length;

        double[][] sharpenedData = new double[height][width];
        double[][] laplacianKernel = {{-1, -1, -1}, {-1, 9, -1}, {-1, -1, -1}};

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                double sum = 0.0;
                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        sum += data[y + ky][x + kx] * laplacianKernel[ky + 1][kx + 1];
                    }
                }
                sharpenedData[y][x] = Math.min(Math.max(sum, 0.0), 255.0); // Ensure pixel values stay within 0-255 range
            }
        }

        return sharpenedData;
    }

    // Denoising using median filtering
    public static double[][] denoise(double[][] data) {
        if (data == null || data.length == 0 || data[0].length == 0) {
            System.err.println("Error: Invalid image data for denoising.");
            return null;
        }

        int height = data.length;
        int width = data[0].length;

        double[][] denoisedData = new double[height][width];

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                List<Double> neighborValues = new ArrayList<>();
                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        neighborValues.add(data[y + ky][x + kx]);
                    }
                }
                neighborValues.sort(Double::compareTo);
                denoisedData[y][x] = neighborValues.get(4); // Use the median value as the denoised pixel value
            }
        }

        return denoisedData;
    }

}
