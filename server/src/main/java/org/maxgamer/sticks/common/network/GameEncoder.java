package org.maxgamer.sticks.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.maxgamer.sticks.common.network.frame.Frame;
import org.maxgamer.sticks.common.stream.BinaryOutputStream;
import org.maxgamer.sticks.common.stream.Streams;

public class GameEncoder extends MessageToByteEncoder<Frame> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Frame frame, ByteBuf byteBuf) throws Exception {
        try (BinaryOutputStream out = Streams.write(byteBuf)) {
            out.writeByte(frame.getOpcode());
            frame.write(out);
        }
    }
}
