package org.maxgamer.sticks.common.model.state;

public abstract class StateChange {
    abstract void visit(StateChangeVisitor visitor);
}
