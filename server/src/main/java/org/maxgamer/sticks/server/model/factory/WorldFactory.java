package org.maxgamer.sticks.server.model.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.maxgamer.sticks.common.clock.Clock;
import org.maxgamer.sticks.server.model.Creature;
import org.maxgamer.sticks.server.model.World;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class WorldFactory {
    public static World load(InputStream state) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        WorldState ws = mapper.readValue(state, WorldState.class);

        World world = new World();

        for (CreatureState creature : ws.creatures) {
            Creature spawn = world.add(creature.proto, creature.position.x, creature.position.y);
            spawn.setAi(Creature.AI.WANDER);
        }

        return world;
    }

    public static class WorldState {
        public List<CreatureState> creatures = new LinkedList<>();
    }

    public static class CreatureState {
        public int proto;
        public PositionState position;
    }

    public static class PositionState {
        public int x;
        public int y;
    }
}
