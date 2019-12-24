package org.maxgamer.sticks.core.world;

import org.maxgamer.sticks.core.FloatingPosition;

public class Momentum {
    public static final int DURATION_IN_TICKS = 16;

    private int progress;
    private float x;
    private float y;

    public Momentum(float x, float y) {
        if ((x == 0) == (y == 0)) {
            throw new IllegalArgumentException("Momentum flows in one direction gave two. X: " + x + ", Y: " + y);
        }

        this.x = x;
        this.y = y;
    }

    public int getProgress() {
        return progress;
    }

    public FloatingPosition apply(FloatingPosition position) {
        if (progress >= DURATION_IN_TICKS) {
            return position;
        }

        FloatingPosition result = new FloatingPosition(position.getX() + x / DURATION_IN_TICKS, position.getY() + y / DURATION_IN_TICKS);

        progress++;

        return result;
    }

    public boolean isDone() {
        return progress >= DURATION_IN_TICKS;
    }

    public Direction getDirection() {
        if (x > 0) {
            return Direction.EAST;
        }

        if (x < 0) {
            return Direction.WEST;
        }

        if (y > 0) {
            return Direction.NORTH;
        }

        if (y < 0) {
            return Direction.SOUTH;
        }

        return Direction.SOUTH;
    }
}
