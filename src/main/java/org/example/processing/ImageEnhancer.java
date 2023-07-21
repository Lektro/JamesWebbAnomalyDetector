package org.example.processing;

import java.util.ArrayList;
import java.util.List;

import static org.example.processing.HotPixelDetector.getMaxPixelValue;
import static org.example.processing.HotPixelDetector.getMinPixelValue;

public class ImageEnhancer {

    // Method for contrast stretching to enhance image quality
    public static double[][] contrastStretch(double[][] data) {
        // Check if the image data is valid (not null and has non-zero dimensions)
        if (data == null || data.length == 0 || data[0].length == 0) {
            System.err.println("Error: Invalid image data for contrast stretching.");
            return null;
        }

        // Find the minimum and maximum pixel values in the data
        double min = getMinPixelValue(data);
        double max = getMaxPixelValue(data);

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

    // Sharpening method using a simple Laplacian kernel
    public static double[][] sharpen(double[][] data) {

        // Check if the image data is valid (not null and has non-zero dimensions)
        if (data == null || data.length == 0 || data[0].length == 0) {
            System.err.println("Error: Invalid image data for sharpening.");
            return null;
        }

        int height = data.length;
        int width = data[0].length;

        double[][] sharpenedData = new double[height][width];
        double[][] laplacianKernel = {{-1, -1, -1}, {-1, 9, -1}, {-1, -1, -1}};

        // Apply the kernel to the inner portion of the data array to avoid out-of-bounds indexing
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

        // Copy the boundary pixels from the original data to the sharpened data
        for (int y = 0; y < height; y++) {
            sharpenedData[y][0] = data[y][0]; // Left boundary
            sharpenedData[y][width - 1] = data[y][width - 1]; // Right boundary
        }

        for (int x = 0; x < width; x++) {
            sharpenedData[0][x] = data[0][x]; // Top boundary
            sharpenedData[height - 1][x] = data[height - 1][x]; // Bottom boundary
        }

        return sharpenedData;
    }


    // De-noising using median filtering
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

                // Use the median value as the de-noised pixel value
                denoisedData[y][x] = neighborValues.get(4); // Use the median value as the de-noised pixel value
            }
        }

        return denoisedData;
    }


}
