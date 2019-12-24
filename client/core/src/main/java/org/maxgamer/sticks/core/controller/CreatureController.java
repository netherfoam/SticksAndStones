package org.maxgamer.sticks.core.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import org.maxgamer.sticks.core.world.entity.CreatureImpl;
import org.maxgamer.sticks.core.world.Momentum;

import java.util.HashSet;
import java.util.Set;

public class CreatureController extends InputAdapter {
    private CreatureImpl creature;
    private Set<Integer> pressed = new HashSet<>();

    public CreatureController(CreatureImpl creature) {
        this.creature = creature;
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

    public void apply() {
        if (creature.isMoving()) {
            return;
        }

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

        Momentum momentum;
        if (dx != 0) {
            momentum = new Momentum(dx, 0);
        } else if (dy != 0) {
            momentum = new Momentum(0, dy);
        } else {
            // No direction, no input
            return;
        }

        creature.move(momentum);
    }
}
