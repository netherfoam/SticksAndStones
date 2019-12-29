package org.maxgamer.sticks.core.world;

import org.maxgamer.sticks.common.world.Direction;
import org.maxgamer.sticks.core.FloatingPosition;

import java.util.ArrayList;
import java.util.List;

public class Momentum {
    public static final int DURATION_IN_TICKS = 16;
    public static final float TICK_DISTANCE = 1.0f / DURATION_IN_TICKS;

    private int remainingTicks;
    private List<Direction> directions = new ArrayList<>();

    public Momentum() {
        this.remainingTicks = 0;
    }

    public Momentum append(Direction direction) {
        this.remainingTicks += DURATION_IN_TICKS;
        this.directions.add(direction);

        return this;
    }

    public FloatingPosition apply(FloatingPosition position) {
        if (remainingTicks <= 0) {
            return position;
        }

        Direction direction = getDirection();
        FloatingPosition result = position.add(direction.dx * TICK_DISTANCE, direction.dy * TICK_DISTANCE);

        remainingTicks--;

        if (remainingTicks % DURATION_IN_TICKS == 0) {
            directions.remove(0);
        }

        return result;
    }

    public boolean isDone() {
        return remainingTicks <= 0;
    }

    public Direction getDirection() {
        if (directions.isEmpty()) {
            return null;
        }

        return directions.get(0);
    }
}
