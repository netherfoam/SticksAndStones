package org.maxgamer.sticks.common.network;

import org.maxgamer.sticks.common.clock.Tickable;
import org.maxgamer.sticks.common.model.state.CreatureAdd;
import org.maxgamer.sticks.common.model.state.CreatureMove;
import org.maxgamer.sticks.common.model.state.CreatureRemove;
import org.maxgamer.sticks.common.model.state.StateChangeVisitor;
import org.maxgamer.sticks.common.network.frame.TickFrame;

public class NetworkWorldStateVisitor implements StateChangeVisitor, Tickable {
    private NettyServer server;
    private TickFrame nextTickFrame = new TickFrame();

    public NetworkWorldStateVisitor(NettyServer server) {
        this.server = server;
    }

    @Override
    public void onChange(CreatureMove move) {
        nextTickFrame.moved(move.getCreatureId(), move.getDirection());
    }

    @Override
    public void onChange(CreatureAdd add) {
        nextTickFrame.added(add.getId(), add.getProto(), add.getX(), add.getY());
    }

    @Override
    public void onChange(CreatureRemove remove) {
        nextTickFrame.removed(remove.getCreatureId());
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
