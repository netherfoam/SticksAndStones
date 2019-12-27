package org.maxgamer.sticks.common.model.state;

public class CreatureAdd extends StateChange {
    private int id;
    private int proto;
    private int x;
    private int y;

    public CreatureAdd(int id, int proto, int x, int y) {
        this.id = id;
        this.proto = proto;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public int getProto() {
        return proto;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    void visit(StateChangeVisitor visitor) {
        visitor.onChange(this);
    }
}
