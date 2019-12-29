package org.maxgamer.sticks.core;

public final class FloatingPosition {
    private final float x;
    private final float y;

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

    public FloatingPosition add(float x, float y) {
        return new FloatingPosition(this.x + x, this.y + y);
    }
}
