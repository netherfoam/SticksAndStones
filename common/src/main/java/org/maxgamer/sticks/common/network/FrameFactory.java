package org.maxgamer.sticks.common.network;

import org.maxgamer.sticks.common.network.frame.Frame;
import org.maxgamer.sticks.common.stream.BinaryInputStream;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class FrameFactory {
    private Map<Integer, Supplier<Frame>> suppliers = new HashMap<>();

    public void register(int opcode, Supplier<Frame> supplier) {
        this.suppliers.put(opcode, supplier);
    }

    public Frame decode(BinaryInputStream in) throws IOException {
        int opcode = in.readUnsignedByte();

        Supplier<Frame> supplier = suppliers.get(opcode);
        if (supplier == null) {
            throw new IOException("Missing opcode: " + opcode);
        }

        Frame frame = supplier.get();
        frame.read(in);

        return frame;
    }
}
