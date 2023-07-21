package org.example.models;

public class HotPixel {
    private final int x;
    private final int y;
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
