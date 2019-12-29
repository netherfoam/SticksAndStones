package org.maxgamer.sticks.common.model;

import org.maxgamer.sticks.common.model.state.CreatureAdd;
import org.maxgamer.sticks.common.model.state.CreatureMove;
import org.maxgamer.sticks.common.model.state.CreatureRemove;
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

    public Map<Integer, Creature> getCreatures() {
        return creatures;
    }

    @Override
    public void onChange(CreatureMove move) {
        Creature creature = creatures.get(move.getCreatureId());
        creature.move(move.getDirection());

        subscriber.onChange(move);
    }

    @Override
    public void onChange(CreatureAdd add) {
        Creature creature = new Creature(this, add.getId(), add.getProto(), add.getX(), add.getY());
        creatures.put(add.getId(), creature);

        subscriber.onChange(add);
    }

    @Override
    public void onChange(CreatureRemove remove) {
        creatures.remove(remove.getCreatureId());

        subscriber.onChange(remove);
    }
}
