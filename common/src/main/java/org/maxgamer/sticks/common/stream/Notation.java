package org.maxgamer.sticks.common.stream;

import java.io.IOException;

public interface Notation<T> {
    void write(BinaryOutputStream out, T value) throws IOException;
    T read(BinaryInputStream in) throws IOException;
}
