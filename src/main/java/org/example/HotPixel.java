package org.example;

public class HotPixel {
    private int x;
    private int y;
    private double value;

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
