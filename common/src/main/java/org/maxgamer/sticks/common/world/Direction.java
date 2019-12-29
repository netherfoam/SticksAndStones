package org.maxgamer.sticks.common.world;

public enum Direction {
    SOUTH(0, -1),
    WEST(-1, 0),
    EAST(1, 0),
    NORTH(0, 1);

    public static Direction decode(byte code) {
        return Direction.values()[code];
    }

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

    public final byte dx;
    public final byte dy;

    Direction(int dx, int dy) {
        this.dx = (byte) dx;
        this.dy = (byte) dy;
    }

    public byte code() {
        return (byte) ordinal();
    }
}
