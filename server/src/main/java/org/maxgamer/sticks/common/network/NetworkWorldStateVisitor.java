package org.maxgamer.sticks.common.network;

import org.maxgamer.sticks.common.clock.Tickable;
import org.maxgamer.sticks.common.model.state.CreatureAdd;
import org.maxgamer.sticks.common.model.state.CreatureMove;
import org.maxgamer.sticks.common.model.state.StateChangeVisitor;
import org.maxgamer.sticks.common.network.frame.TickFrame;

public class NetworkWorldStateVisitor implements StateChangeVisitor, Tickable {
    private NettyServer server;
    private TickFrame nextTickFrame = new TickFrame();

    public NetworkWorldStateVisitor(NettyServer server) {
        this.server = server;
    }

    @Override
    public void onChange(CreatureMove creatureMove) {
        nextTickFrame.moved(creatureMove.getCreatureId(), creatureMove.getDx(), creatureMove.getDy());
    }

    @Override
    public void onChange(CreatureAdd creatureAdd) {
        nextTickFrame.added(creatureAdd.getId(), creatureAdd.getProto(), creatureAdd.getX(), creatureAdd.getY());
    }

    public void send() {
        TickFrame frame = this.nextTickFrame;
        this.nextTickFrame = new TickFrame();

        for (Client client : server.getClients()) {
            client.write(frame);
        }
    }

    @Override
    public void tick() throws Exception {
        send();
    }
}
