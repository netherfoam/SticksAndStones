package org.maxgamer.sticks.common.network.frame;

import org.maxgamer.sticks.common.stream.BinaryInputStream;
import org.maxgamer.sticks.common.stream.BinaryOutputStream;

import java.io.IOException;

public class MoveFrame extends Frame {
    public static final int OPCODE = 11;

    private byte dx;
    private byte dy;

    public MoveFrame() {
        super(OPCODE);
    }

    public byte getDx() {
        return dx;
    }

    public void setDx(byte dx) {
        this.dx = dx;
    }

    public byte getDy() {
        return dy;
    }

    public void setDy(byte dy) {
        this.dy = dy;
    }

    @Override
    public void read(BinaryInputStream in) throws IOException {
        dx = in.readByte();
        dy = in.readByte();
    }

    @Override
    public void write(BinaryOutputStream out) throws IOException {
        out.writeByte(dx);
        out.writeByte(dy);
    }
}
