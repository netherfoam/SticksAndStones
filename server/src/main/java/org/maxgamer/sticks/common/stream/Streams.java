package org.maxgamer.sticks.common.stream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.ByteArrayInputStream;

public final class Streams {
    public static BinaryInputStream read(ByteBuf source) {
        return new BinaryInputStream(
                new ByteBufInputStream(source)
        );
    }

    public static BinaryOutputStream write(ByteBuf target) {
        return new BinaryOutputStream(
                new ByteBufOutputStream(target)
        );
    }

    public static BinaryInputStream read(byte[] source) {
        return new BinaryInputStream(
                new ByteArrayInputStream(source)
        );
    }

    private Streams() {
        // Static class
    }
}