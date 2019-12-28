package org.maxgamer.sticks.core.world.entity;

import org.maxgamer.sticks.core.FloatingPosition;
import org.maxgamer.sticks.core.Position;
import org.maxgamer.sticks.core.viewport.Viewport;

public interface Entity {
    void teleport(Position position);
    boolean isVisible(Viewport viewport);
    FloatingPosition getPosition();
}
