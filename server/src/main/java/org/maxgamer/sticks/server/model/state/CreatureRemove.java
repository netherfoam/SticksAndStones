package org.maxgamer.sticks.server.model.state;

public class CreatureRemove extends StateChange {
    private int creatureId;

    public CreatureRemove(int creatureId) {
        this.creatureId = creatureId;
    }

    public int getCreatureId() {
        return creatureId;
    }

    public void setCreatureId(int creatureId) {
        this.creatureId = creatureId;
    }

    @Override
    void visit(StateChangeVisitor visitor) {
        visitor.onChange(this);
    }
}
