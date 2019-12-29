package org.maxgamer.sticks.common.model.state;

import org.maxgamer.sticks.common.world.Direction;

public class CreatureMove extends StateChange {
    private int creatureId;
    private Direction direction;

    public CreatureMove(int creatureId, Direction direction) {
        this.creatureId = creatureId;
        this.direction = direction;
    }

    public int getCreatureId() {
        return creatureId;
    }

    public void setCreatureId(int creatureId) {
        this.creatureId = creatureId;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    void visit(StateChangeVisitor visitor) {
        visitor.onChange(this);
    }
}
