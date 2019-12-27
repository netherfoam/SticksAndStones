package org.maxgamer.sticks.common.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.maxgamer.sticks.common.model.World;
import org.maxgamer.sticks.common.model.state.CreatureMove;
import org.maxgamer.sticks.common.network.frame.MoveFrame;

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
            System.out.println("Player moved");

            MoveFrame frame = (MoveFrame) msg;

            CreatureMove move = new CreatureMove(id, frame.getDx(), frame.getDy());
            world.onChange(move);
        }
    }
}
