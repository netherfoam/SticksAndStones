package org.maxgamer.sticks.core.sound;

import com.badlogic.gdx.audio.Sound;

import java.util.List;

public class SoundLoop {
    private int index;
    private long duration;
    private long lastEnded = 0;
    private List<Sound> sounds;
    private float volume;

    public SoundLoop(List<Sound> sounds, long duration, float volume) {
        this.sounds = sounds;
        this.duration = duration;
        this.volume = volume;
    }

    private Sound next() {
        if (sounds.isEmpty()) {
            return null;
        }

        return sounds.get(index++ % sounds.size());
    }

    public void play() {
        if (lastEnded >= System.currentTimeMillis()) {
            // Still playing a previous sound
            return;
        }

        Sound current = next();
        if (current == null) {
            return;
        }

        lastEnded = System.currentTimeMillis() + duration;
        current.play(volume);
    }
}
