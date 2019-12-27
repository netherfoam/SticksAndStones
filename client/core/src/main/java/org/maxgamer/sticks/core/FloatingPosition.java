package org.maxgamer.sticks.core;

public class FloatingPosition {
    private float x;
    private float y;

    public FloatingPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("(%.1f, %.1f)", x, y);
    }
}
