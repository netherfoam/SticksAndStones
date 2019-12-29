package org.maxgamer.sticks.common.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.maxgamer.sticks.common.model.Creature;
import org.maxgamer.sticks.common.model.World;
import org.maxgamer.sticks.common.model.state.CreatureAdd;
import org.maxgamer.sticks.common.model.state.CreatureMove;
import org.maxgamer.sticks.common.model.state.CreatureRemove;
import org.maxgamer.sticks.common.network.frame.IdentityFrame;
import org.maxgamer.sticks.common.network.frame.MoveFrame;
import org.maxgamer.sticks.common.network.frame.TickFrame;
import org.maxgamer.sticks.common.world.Direction;

public class GameHandler extends ChannelInboundHandlerAdapter {
    private int id;
    private World world;

    public GameHandler(int id, World world) {
        this.id = id;
        this.world = world;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof MoveFrame) {
            MoveFrame frame = (MoveFrame) msg;

            CreatureMove move = new CreatureMove(id, frame.getDirection());
            world.onChange(move);
        } else {
            throw new IllegalArgumentException("unhandled frame; " + msg);
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        IdentityFrame identity = new IdentityFrame();
        identity.setIdentity(id);
        ctx.writeAndFlush(identity).awaitUninterruptibly();

        CreatureAdd add = new CreatureAdd(id, 1, 50, 50);
        world.onChange(add);

        TickFrame initTick = new TickFrame();

        for (Creature creature : world.getCreatures().values()) {
            initTick.added(creature.getId(), creature.getProto(), creature.getX(), creature.getY());
        }

        ctx.writeAndFlush(initTick);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        CreatureRemove remove = new CreatureRemove(id);

        world.onChange(remove);
    }
}
