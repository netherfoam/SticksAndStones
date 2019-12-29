package org.maxgamer.sticks.common.network.frame;

import org.maxgamer.sticks.common.stream.BinaryInputStream;
import org.maxgamer.sticks.common.stream.BinaryOutputStream;
import org.maxgamer.sticks.common.world.Direction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TickFrame extends Frame {
    private List<AddedEntity> added = new ArrayList<>();
    private List<RemovedEntity> removed = new ArrayList<>();
    private List<MovedEntity> moved = new ArrayList<>();

    public TickFrame() {
        super(Opcodes.TICK);
    }

    public void added(int id, int proto, int x, int y) {
        AddedEntity add = new AddedEntity();
        add.id = id;
        add.proto = proto;
        add.x = x;
        add.y = y;

        added.add(add);
    }

    public void removed(int id) {
        RemovedEntity remove = new RemovedEntity();
        remove.id = id;

        removed.add(remove);
    }

    public void moved(int id, Direction direction) {
        MovedEntity move = new MovedEntity();
        move.id = id;
        move.code = direction.code();

        moved.add(move);
    }

    public List<AddedEntity> getAdded() {
        return Collections.unmodifiableList(added);
    }

    public List<MovedEntity> getMoved() {
        return moved;
    }

    public List<RemovedEntity> getRemoved() {
        return Collections.unmodifiableList(removed);
    }

    @Override
    public void read(BinaryInputStream in) throws IOException {
        int totalAdded = in.readUnsignedShort();

        for (int i = 0; i < totalAdded; i++) {
            AddedEntity added = new AddedEntity();
            added.id = in.readInt();
            added.proto = in.readInt();
            added.x = in.readUnsignedShort();
            added.y = in.readUnsignedShort();

            this.added.add(added);
        }

        int totalRemoved = in.readUnsignedShort();
        for (int i = 0; i < totalRemoved; i++) {
            RemovedEntity removed = new RemovedEntity();
            removed.id = in.readInt();

            this.removed.add(removed);
        }

        int movedTotal = in.readUnsignedShort();
        for (int i = 0; i < movedTotal; i++) {
            MovedEntity moved = new MovedEntity();
            moved.id = in.readInt();
            moved.code = in.readByte();

            this.moved.add(moved);
        }
    }

    @Override
    public void write(BinaryOutputStream out) throws IOException {
        out.writeShort(this.added.size());
        for (AddedEntity added : this.added) {
            out.writeInt(added.id);
            out.writeInt(added.proto);
            out.writeShort(added.x);
            out.writeShort(added.y);
        }

        out.writeShort(this.removed.size());
        for (RemovedEntity removed : this.removed) {
            out.writeInt(removed.id);
        }

        out.writeShort(this.moved.size());
        for (MovedEntity moved : this.moved) {
            out.writeInt(moved.id);
            out.writeByte(moved.code);
        }
    }

    public static class AddedEntity {
        public int id;
        public int proto;
        public int x;
        public int y;
    }

    public static class RemovedEntity {
        public int id;
    }

    public static class MovedEntity {
        public int id;
        public byte code;
    }
}
