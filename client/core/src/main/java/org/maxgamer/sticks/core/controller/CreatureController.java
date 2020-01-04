package org.maxgamer.sticks.core.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import org.maxgamer.sticks.common.world.Direction;
import org.maxgamer.sticks.core.network.NetworkController;
import org.maxgamer.sticks.core.world.Zone;
import org.maxgamer.sticks.core.world.entity.CreatureImpl;

import java.util.ArrayList;
import java.util.List;

public class CreatureController extends InputAdapter {
    private NetworkController network;
    private Zone zone;
    private CreatureImpl creature;
    private List<Integer> pressed = new ArrayList<>();
    private List<KeySubscription> subscriptions = new ArrayList<>();

    public CreatureController(NetworkController network, CreatureImpl creature, Zone zone) {
        this.network = network;
        this.creature = creature;
        this.zone = zone;
    }

    public KeySubscription subscribe(KeySubscriber subscriber, boolean onRelease, int... keys) {
        KeySubscription subscription = new KeySubscription(keys, onRelease, subscriber);

        subscriptions.add(subscription);
        return subscription;
    }

    public void unsubscribe(KeySubscription subscription) {
        subscriptions.remove(subscription);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!pressed.contains(keycode)) {
            pressed.add(keycode);
        }

        for (KeySubscription subscription : subscriptions) {
            if (!subscription.isOnRelease() && subscription.isSubscribedTo(keycode)) {
                subscription.getSubscriber().activate(keycode);
            }
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        pressed.remove((Integer) keycode);

        if (keycode == Input.Keys.ESCAPE) {
            Gdx.app.exit();
        }

        for (KeySubscription subscription : subscriptions) {
            if (subscription.isOnRelease() && subscription.isSubscribedTo(keycode)) {
                subscription.getSubscriber().activate(keycode);
            }
        }


        return false;
    }

    public boolean isPressed(int key) {
        return pressed.contains(key);
    }

    public Integer getLastPressed(int... keys) {
        if (keys.length <= 0) {
            return null;
        }

        int idxBest = -1;
        int best = -1;

        for (int key : keys) {
            int index = pressed.indexOf(key);

            if (index > idxBest) {
                idxBest = index;
                best = key;
            }
        }

        if (idxBest == -1) {
            return null;
        }

        return best;
    }

    public void apply() {
        if (creature.isMoving()) {
            return;
        }

        Integer pressed = getLastPressed(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D);
        if (pressed == null) {
            // No keys pressed
            return;
        }

        int dx = 0;
        int dy = 0;

        if (pressed == Input.Keys.W) {
            dy += 1;
        }

        if (pressed == Input.Keys.S) {
            dy -= 1;
        }

        if (pressed == Input.Keys.A) {
            dx -= 1;
        }

        if (pressed == Input.Keys.D) {
            dx += 1;
        }

        boolean collisionX = zone.isCollision((int) creature.getPosition().getX() + dx, (int) creature.getPosition().getY());
        boolean collisionY = zone.isCollision((int) creature.getPosition().getX(), (int) creature.getPosition().getY() + dy);

        Direction direction;
        if (dx != 0 && !collisionX) {
            direction = Direction.of(dx, 0);
        } else if (dy != 0 && !collisionY) {
            direction = Direction.of(0, dy);
        } else {
            if (dx != 0 || dy != 0) {
                creature.setDirection(Direction.of(dx, dy));
            }

            // No direction, no input
            return;
        }

        network.move(direction);
        creature.move(direction);
    }
}
