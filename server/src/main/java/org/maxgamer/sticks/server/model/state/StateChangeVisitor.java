package org.maxgamer.sticks.server.model.state;

public interface StateChangeVisitor {
    void onChange(CreatureMove move);

    void onChange(CreatureAdd add);

    void onChange(CreatureRemove remove);
}
