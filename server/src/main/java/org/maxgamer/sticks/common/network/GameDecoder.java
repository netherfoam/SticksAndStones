package org.maxgamer.sticks.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.maxgamer.sticks.common.model.World;
import org.maxgamer.sticks.common.network.frame.Frame;
import org.maxgamer.sticks.common.stream.BinaryInputStream;
import org.maxgamer.sticks.common.stream.Streams;

import java.util.List;

public class GameDecoder extends ByteToMessageDecoder {
    private List<Client> clients;
    private Client client;
    private FrameFactory factory;

    public GameDecoder(FrameFactory factory, List<Client> clients, Client client) {
        this.clients = clients;
        this.factory = factory;
        this.client = client;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        try (BinaryInputStream in = Streams.read(byteBuf)) {
            while (in.available() > 0) {
                Frame frame = factory.decode(in);
                out.add(frame);
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        clients.add(client);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        clients.remove(client);
    }
}
