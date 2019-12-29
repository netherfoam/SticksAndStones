package org.maxgamer.sticks.core.viewport;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import org.maxgamer.sticks.core.FloatingPosition;
import org.maxgamer.sticks.core.world.Zone;
import org.maxgamer.sticks.core.world.entity.CreatureImpl;

import java.util.Map;

public class Viewport {
    public static final int ASPECT_WIDTH = 16;
    public static final int DEFAULT_RES_WIDTH = ASPECT_WIDTH * 100;
    public static final int ASPECT_HEIGHT = 9;
    public static final int DEFAULT_RES_HEIGHT = ASPECT_HEIGHT * 100;
    public static final float VIEWPORT_MULTIPLIER = 1.25f;
    public static final float VIEWPORT_HEIGHT = (int) (ASPECT_HEIGHT * VIEWPORT_MULTIPLIER);
    public static final float VIEWPORT_WIDTH = (int) (ASPECT_WIDTH * VIEWPORT_MULTIPLIER);

    private CreatureImpl focus;
    private float radX;
    private float radY;
    private OrthographicCamera camera;

    private Zone zone;
    private CreatureRender creatures;

    public Viewport(float width, float height, Map<Integer, CreatureImpl> creatures) {
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        camera.position.set(50, 50, 0);
        camera.update();

        this.creatures = new CreatureRender(creatures);

        this.radX = width / 2;
        this.radY = height / 2;
    }

    public void setFocus(CreatureImpl focus) {
        this.focus = focus;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public CreatureRender getCreatures() {
        return creatures;
    }

    public FloatingPosition getCenter() {
        if (focus == null) {
            return null;
        }

        return focus.getPosition();
    }

    public boolean overlaps(FloatingPosition position, float width, float height) {
        FloatingPosition center = getCenter();

        if (center == null) {
            return false;
        }

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

        if (center == null) {
            return;
        }

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
