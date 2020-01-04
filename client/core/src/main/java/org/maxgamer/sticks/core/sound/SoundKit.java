package org.maxgamer.sticks.core.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

import java.util.*;

public class SoundKit {
    private Map<SoundType, List<Sound>> sounds = new HashMap<>();

    public void load(SoundType type, FileHandle path) {
        Sound sound = Gdx.audio.newSound(path);

        List<Sound> list = sounds.computeIfAbsent(type, (t) -> new ArrayList<>());
        list.add(sound);
    }

    public List<Sound> get(SoundType type) {
        return Collections.unmodifiableList(sounds.getOrDefault(type, Collections.emptyList()));
    }

    public Sound get(SoundType type, int index, boolean loop) {
        List<Sound> list = get(type);
        if (list == null) {
            return null;
        }

        if (index >= list.size() && !loop) {
            return null;
        }

        return list.get(index % list.size());
    }
}
