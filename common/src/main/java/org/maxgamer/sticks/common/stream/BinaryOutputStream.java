package org.maxgamer.sticks.common.stream;

import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BinaryOutputStream extends DataOutputStream {
    /**
     * Creates a new data output stream to write data to the specified
     * underlying output stream. The counter <code>written</code> is
     * set to zero.
     *
     * @param out the underlying output stream, to be saved for later
     *            use.
     * @see FilterOutputStream#out
     */
    public BinaryOutputStream(OutputStream out) {
        super(out);
    }

    public <T> void write(Notation<T> notation, T value) throws IOException {
        notation.write(this, value);
    }
}
