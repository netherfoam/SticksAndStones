package org.maxgamer.sticks.server.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.maxgamer.sticks.server.model.Creature;
import org.maxgamer.sticks.server.model.World;
import org.maxgamer.sticks.common.network.frame.IdentityFrame;
import org.maxgamer.sticks.common.network.frame.MoveFrame;
import org.maxgamer.sticks.common.network.frame.TickFrame;

public class GameHandler extends ChannelInboundHandlerAdapter {
    private int id = -1;
    private World world;

    public GameHandler(World world) {
        this.world = world;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof MoveFrame) {
            MoveFrame frame = (MoveFrame) msg;

            world.move(id, frame.getDirection());
        } else {
            throw new IllegalArgumentException("unhandled frame; " + msg);
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Creature player = world.add(1, 50, 50);
        this.id = player.getId();

        IdentityFrame identity = new IdentityFrame();
        identity.setIdentity(player.getId());
        identity.setProto(3);
        ctx.writeAndFlush(identity).awaitUninterruptibly();

        TickFrame initTick = new TickFrame();

        for (Creature creature : world.getCreatures()) {
            initTick.added(creature.getId(), creature.getProto(), creature.getX(), creature.getY());
        }

        ctx.writeAndFlush(initTick);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        world.remove(id);
    }
}
