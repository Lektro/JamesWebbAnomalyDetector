package org.example.models;

import java.util.List;

/**
 * Represents a FITS image along with its associated hot pixels.
 */
public class FitsImage {

    // The 2D array representing the pixel data of the FITS image.
    private final double[][] data;
    private final int width;
    private final int height;

    // A list of hot pixels detected in the FITS image.
    private final List<HotPixel> hotPixels;

    public FitsImage(double[][] data, int width, int height, List<HotPixel> hotPixels) {
        this.data = data;
        this.width = width;
        this.height = height;
        this.hotPixels = hotPixels;
    }

    public double[][] getData() {
        return data;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<HotPixel> getHotPixels() {
        return hotPixels;
    }
}
