package org.maxgamer.sticks.core.world.entity;

import org.maxgamer.sticks.core.FloatingPosition;
import org.maxgamer.sticks.core.Position;
import org.maxgamer.sticks.core.world.Direction;
import org.maxgamer.sticks.core.viewport.Viewport;

public class EntityImpl implements Entity {
    protected FloatingPosition position;
    protected Direction direction = Direction.SOUTH;
    protected int width = 1;
    protected int height = 1;

    @Override
    public void teleport(Position position) {
        this.position = new FloatingPosition(position.getX(), position.getY());
    }

    @Override
    public boolean isVisible(Viewport viewport) {
        return viewport.overlaps(position, width, height);
    }

    @Override
    public FloatingPosition getPosition() {
        return position;
    }
}
