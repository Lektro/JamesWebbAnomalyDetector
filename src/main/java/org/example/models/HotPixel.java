package org.example.models;
/**
 * Represents a Hot Pixel along with its associated co-ordinates.
 */
public class HotPixel {
    // The x-coordinate of the hot pixel.
    private final int x;
    // The y-coordinate of the hot pixel.
    private final int y;

    // The value of the hot pixel.
    private final double value;

    public HotPixel(int x, int y, double value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getValue() {
        return value;
    }
}
