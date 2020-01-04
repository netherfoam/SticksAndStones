package org.maxgamer.sticks.core.prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

public class PrototypeFactory {
    private static final Map<Class, String> DIRECTORIES = new HashMap<>();
    static {
        DIRECTORIES.put(CreaturePrototype.class, "creature");
        DIRECTORIES.put(ItemPrototype.class, "item");
    }

    private ObjectMapper mapper;

    public PrototypeFactory() {
        mapper = new ObjectMapper();
    }

    public <T> T get(Class<T> type, int protoId) {
        String path = "proto/" + DIRECTORIES.getOrDefault(type, "other") + "/" + protoId + ".json";

        FileHandle handle = Gdx.files.internal(path);

        try (InputStream in = handle.read()) {
            return mapper.readValue(in, type);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
