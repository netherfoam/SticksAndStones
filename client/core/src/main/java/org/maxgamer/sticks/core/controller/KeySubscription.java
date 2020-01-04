package org.maxgamer.sticks.core.controller;

public class KeySubscription {
    private int[] keys;
    private boolean onRelease = true;
    private KeySubscriber subscriber;

    public KeySubscription(int[] keys, boolean onRelease, KeySubscriber subscriber) {
        this.keys = keys;
        this.onRelease = onRelease;
        this.subscriber = subscriber;
    }

    public int[] getKeys() {
        return keys;
    }

    public boolean isSubscribedTo(int key) {
        for (int k : keys) {
            if (k == key) return true;
        }

        return false;
    }

    public boolean isOnRelease() {
        return onRelease;
    }

    public KeySubscriber getSubscriber() {
        return subscriber;
    }
}
