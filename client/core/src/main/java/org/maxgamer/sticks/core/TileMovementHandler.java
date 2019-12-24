package org.maxgamer.sticks.core;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.HashSet;
import java.util.Set;

public class TileMovementHandler extends InputAdapter {
    private static final float PARTS = 16;

    private Set<Integer> pressed = new HashSet<>();
    private int[] direction = null;
    private int moves = 0;

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
        if (direction == null) {
            int dx = 0;
            int dy = 0;

            if (isPressed(Input.Keys.W)) {
                dy += 1;
            }

            if (isPressed(Input.Keys.S)) {
                dy -= 1;
            }

            if (isPressed(Input.Keys.A)) {
                dx -= 1;
            }

            if (isPressed(Input.Keys.D)) {
                dx += 1;
            }

            if (dx != 0) {
                direction = new int[]{dx, 0};
            } else if (dy != 0) {
                direction = new int[]{0, dy};
            } else {
                // No direction, no input
                return;
            }
        }

        moves++;


        camera.translate(1 / PARTS * direction[0], 1 / PARTS * direction[1]);

        if (moves >= PARTS) {
            moves = 0;
            direction = null;
        }
    }
}
