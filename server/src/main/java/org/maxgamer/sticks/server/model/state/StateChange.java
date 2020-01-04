package org.maxgamer.sticks.server.model.state;

public abstract class StateChange {
    abstract void visit(StateChangeVisitor visitor);
}
