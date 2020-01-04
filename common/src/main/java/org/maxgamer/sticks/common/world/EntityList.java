package org.maxgamer.sticks.common.world;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

public class EntityList<T> implements Iterable<T> {
    private int nextId;
    private Map<Integer, T> contents = new HashMap<>();

    public T add(Function<Integer, T> supplier) {
        T result = supplier.apply(nextId);
        add(nextId, result);
        nextId++;

        return result;
    }

    public void add(int id, T type) {
        if (type == null) {
            throw new NullPointerException("Can't add NULL npc as ID " + id);
        }

        if (contents.get(id) != null) {
            throw new IllegalArgumentException("ID" + id + " is taken");
        }

        contents.put(id, type);
    }

    public T remove(int id) {
        return contents.remove(id);
    }

    public T get(int id) {
        return contents.get(id);
    }

    @Override
    public Iterator<T> iterator() {
        return contents.values().iterator();
    }
}
