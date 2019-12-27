package org.maxgamer.sticks.common.model;

import org.maxgamer.sticks.common.model.state.CreatureAdd;
import org.maxgamer.sticks.common.model.state.CreatureMove;
import org.maxgamer.sticks.common.model.state.StateChangeVisitor;

import java.util.HashMap;
import java.util.Map;

public class World implements StateChangeVisitor {
    private Map<Integer, Creature> creatures = new HashMap<>();
    private StateChangeVisitor subscriber;

    public World() {

    }

    public void setStateChangeVisitor(StateChangeVisitor visitor) {
        this.subscriber = visitor;
    }

    @Override
    public void onChange(CreatureMove creatureMove) {
        Creature creature = creatures.get(creatureMove.getCreatureId());
        creature.move(creatureMove.getDx(), creatureMove.getDy());

        subscriber.onChange(creatureMove);
    }

    @Override
    public void onChange(CreatureAdd creatureAdd) {
        Creature creature = new Creature(this, creatureAdd.getId(), creatureAdd.getProto(), creatureAdd.getX(), creatureAdd.getY());
        creatures.put(creatureAdd.getId(), creature);

        subscriber.onChange(creatureAdd);
    }
}
