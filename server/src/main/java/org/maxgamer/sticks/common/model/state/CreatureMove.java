package org.maxgamer.sticks.common.model.state;

public class CreatureMove extends StateChange {
    private int creatureId;
    private byte dx;
    private byte dy;

    public CreatureMove(int creatureId, byte dx, byte dy) {
        this.creatureId = creatureId;
        this.dx = dx;
        this.dy = dy;
    }

    public int getCreatureId() {
        return creatureId;
    }

    public void setCreatureId(int creatureId) {
        this.creatureId = creatureId;
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
    void visit(StateChangeVisitor visitor) {
        visitor.onChange(this);
    }
}
