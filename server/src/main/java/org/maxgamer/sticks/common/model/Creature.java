package org.maxgamer.sticks.common.model;

import org.maxgamer.sticks.common.world.Direction;

public class Creature {
    private World world;
    private int id;
    private int proto;
    private int x;
    private int y;

    public Creature(World world, int id, int proto, int x, int y) {
        this.world = world;
        this.id = id;
        this.proto = proto;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(Direction direction) {
        this.x += direction.dx;
        this.y += direction.dy;
    }

    public World getWorld() {
        return world;
    }

    public int getProto() {
        return proto;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
