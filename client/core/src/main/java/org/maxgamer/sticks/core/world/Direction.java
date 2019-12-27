package org.maxgamer.sticks.core.world;

public enum Direction {
    SOUTH,
    WEST,
    EAST,
    NORTH;

    public static Direction of(int dx, int dy) {
        if (dx > 0) {
            return Direction.EAST;
        }

        if (dx < 0) {
            return Direction.WEST;
        }

        if (dy > 0) {
            return Direction.NORTH;
        }

        if (dy < 0) {
            return Direction.SOUTH;
        }

        return Direction.SOUTH;
    }
}
