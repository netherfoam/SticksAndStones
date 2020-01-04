package org.maxgamer.sticks.server.model;

import org.maxgamer.sticks.common.world.Direction;
import org.maxgamer.sticks.common.world.EntityList;
import org.maxgamer.sticks.server.model.state.*;

public class World {
    private EntityList<Creature> creatures = new EntityList<>();
    private StateChangeVisitor subscriber = new VoidStateChangeVisitor();

    public World() {

    }

    public void setStateChangeVisitor(StateChangeVisitor visitor) {
        this.subscriber = visitor;
    }

    public EntityList<Creature> getCreatures() {
        return creatures;
    }

    public Creature add(int proto, int x, int y) {
        Creature creature = creatures.add((id) -> new Creature(this, id, proto, x, y));
        subscriber.onChange(new CreatureAdd(creature.getId(), proto, x, y));

        return creature;
    }

    public void move(int id, Direction direction) {
        Creature creature = creatures.get(id);
        creature.move(direction);
        subscriber.onChange(new CreatureMove(id, direction));
    }

    public void remove(int id) {
        Creature creature = creatures.remove(id);

        if (creature != null) {
            subscriber.onChange(new CreatureRemove(id));
        }
    }
}
