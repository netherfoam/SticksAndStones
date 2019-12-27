package org.maxgamer.sticks.common.model.state;

public interface StateChangeVisitor {
    void onChange(CreatureMove creatureMove);

    void onChange(CreatureAdd creatureAdd);
}
