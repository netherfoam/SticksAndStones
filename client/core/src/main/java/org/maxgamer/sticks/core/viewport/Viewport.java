package org.maxgamer.sticks.core.viewport;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import org.maxgamer.sticks.core.FloatingPosition;
import org.maxgamer.sticks.core.world.Zone;
import org.maxgamer.sticks.core.world.entity.CreatureImpl;

public class Viewport {
    private CreatureImpl focus;
    private float radX;
    private float radY;
    private OrthographicCamera camera;

    private Zone zone;
    private CreatureRender creatures = new CreatureRender();

    public Viewport(CreatureImpl focus, float width, float height, OrthographicCamera camera) {
        this.focus = focus;
        this.radX = width / 2;
        this.radY = height / 2;
        this.camera = camera;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public CreatureRender getCreatures() {
        return creatures;
    }

    public FloatingPosition getCenter() {
        return focus.getPosition();
    }

    public boolean overlaps(FloatingPosition position, float width, float height) {
        FloatingPosition center = getCenter();

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

    public Zone getZone() {
        return zone;
    }

    public void render(float delta) {
        FloatingPosition center = getCenter();

        camera.position.set(center.getX(), center.getY(), 0);
        camera.update();

        getZone().render(delta, this);
        getCreatures().render(delta, this);
    }

    public Matrix4 worldMatrix() {
        return camera.combined;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
