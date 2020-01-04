package org.maxgamer.sticks.core.world;

import org.maxgamer.sticks.core.world.entity.CreatureImpl;
import org.maxgamer.sticks.common.world.EntityList;

public class World {
    private Zone zone;
    private EntityList<CreatureImpl> creatures = new EntityList<>();

    public World(Zone zone) {
        this.zone = zone;
    }

    public EntityList<CreatureImpl> getCreatures() {
        return creatures;
    }

    public Zone getZone() {
        return zone;
    }
}
