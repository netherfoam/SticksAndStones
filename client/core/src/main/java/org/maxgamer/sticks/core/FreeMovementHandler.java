package org.maxgamer.sticks.core;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.HashSet;
import java.util.Set;

public class FreeMovementHandler extends InputAdapter {
    private float speed;
    private Set<Integer> pressed = new HashSet<>();

    public FreeMovementHandler(float speed) {
        this.speed = speed;
    }

    @Override
    public boolean keyDown(int keycode) {
        pressed.add(keycode);

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        pressed.remove(keycode);

        return false;
    }

    public boolean isPressed(int key) {
        return pressed.contains(key);
    }

    public void apply(OrthographicCamera camera) {
        float dx = 0;
        float dy = 0;

        if (isPressed(Input.Keys.W)) {
            dy += speed;
        }

        if (isPressed(Input.Keys.S)) {
            dy -= speed;
        }

        if (isPressed(Input.Keys.A)) {
            dx -= speed;
        }

        if (isPressed(Input.Keys.D)) {
            dx += speed;
        }

        if (dx != 0 || dy != 0) {
            camera.translate(dx, dy);
        }
    }
}
