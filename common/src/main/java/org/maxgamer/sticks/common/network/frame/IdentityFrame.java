package org.maxgamer.sticks.common.network.frame;

import org.maxgamer.sticks.common.stream.BinaryInputStream;
import org.maxgamer.sticks.common.stream.BinaryOutputStream;

import java.io.IOException;

public class IdentityFrame extends Frame {
    public static final int OPCODE = 12;
    private int identity;

    public IdentityFrame() {
        super(OPCODE);
    }

    @Override
    public void read(BinaryInputStream in) throws IOException {
        identity = in.readInt();
    }

    @Override
    public void write(BinaryOutputStream out) throws IOException {
        out.writeInt(identity);
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }
}
