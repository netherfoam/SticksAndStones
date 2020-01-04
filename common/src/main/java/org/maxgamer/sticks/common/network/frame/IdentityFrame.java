package org.maxgamer.sticks.common.network.frame;

import org.maxgamer.sticks.common.stream.BinaryInputStream;
import org.maxgamer.sticks.common.stream.BinaryOutputStream;

import java.io.IOException;

public class IdentityFrame extends Frame {
    private int identity;
    private int proto = 1;

    public IdentityFrame() {
        super(Opcodes.IDENTITY);
    }

    @Override
    public void read(BinaryInputStream in) throws IOException {
        identity = in.readInt();
        proto = in.readInt();
    }

    @Override
    public void write(BinaryOutputStream out) throws IOException {
        out.writeInt(identity);
        out.writeInt(proto);
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public int getProto() {
        return proto;
    }

    public void setProto(int proto) {
        this.proto = proto;
    }
}
