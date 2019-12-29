package org.maxgamer.sticks.common.model.state;

public interface StateChangeVisitor {
    void onChange(CreatureMove move);

    void onChange(CreatureAdd add);

    void onChange(CreatureRemove remove);
}
