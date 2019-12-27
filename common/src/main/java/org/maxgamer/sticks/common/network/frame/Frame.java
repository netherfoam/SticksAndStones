package org.maxgamer.sticks.common.network.frame;

import org.maxgamer.sticks.common.stream.BinaryInputStream;
import org.maxgamer.sticks.common.stream.BinaryOutputStream;

import java.io.IOException;

public abstract class Frame {
    private int opcode;

    public Frame(int opcode) {
        this.opcode = opcode;
    }

    public int getOpcode() {
        return opcode;
    }

    public abstract void read(BinaryInputStream in) throws IOException;
    public abstract void write(BinaryOutputStream out) throws IOException;
}
