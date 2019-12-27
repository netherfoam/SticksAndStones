package org.maxgamer.sticks.common.stream;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BinaryInputStream extends DataInputStream {
    /**
     * Creates a DataInputStream that uses the specified
     * underlying InputStream.
     *
     * @param in the specified input stream
     */
    public BinaryInputStream(InputStream in) {
        super(in);
    }

    public <T> T read(Notation<T> notation) throws IOException {
        return notation.read(this);
    }

    @Override
    public int available() throws IOException {
        return super.available();
    }
}
