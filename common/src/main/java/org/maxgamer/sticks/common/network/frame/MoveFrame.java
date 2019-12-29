package org.maxgamer.sticks.common.network.frame;

import org.maxgamer.sticks.common.stream.BinaryInputStream;
import org.maxgamer.sticks.common.stream.BinaryOutputStream;
import org.maxgamer.sticks.common.world.Direction;

import java.io.IOException;

public class MoveFrame extends Frame {
    private Direction direction;

    public MoveFrame() {
        super(Opcodes.MOVE);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void read(BinaryInputStream in) throws IOException {
        direction = Direction.decode(in.readByte());
    }

    @Override
    public void write(BinaryOutputStream out) throws IOException {
        out.writeByte(direction.code());
    }
}
