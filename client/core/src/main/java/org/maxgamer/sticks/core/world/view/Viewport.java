package org.maxgamer.sticks.core.world.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.maxgamer.sticks.core.FloatingPosition;

public class Viewport {
    private FloatingPosition center;
    private float radX;
    private float radY;
    private SpriteBatch spriteBatch;

    public Viewport(SpriteBatch spriteBatch, FloatingPosition center, float width, float height) {
        this.center = center;
        this.radX = width / 2;
        this.radY = height / 2;
        this.spriteBatch = spriteBatch;
    }

    public void setCenter(FloatingPosition center) {
        this.center = center;
    }

    public FloatingPosition getCenter() {
        return center;
    }

    public boolean overlaps(FloatingPosition position, float width, float height) {
        if (position.getX() + width < center.getX() - radX) {
            // Too far left
            return false;
        }

        if (position.getX() > center.getX() + radX) {
            // Too far right
            return false;
        }

        if (position.getY() + height < center.getY() - radY) {
            // Too far south
            return false;
        }

        if (position.getY() > center.getY() + radY) {
            // Too far north
            return false;
        }

        return true;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }
}
