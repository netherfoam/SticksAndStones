package org.maxgamer.sticks.server.network;

import io.netty.channel.socket.SocketChannel;
import org.maxgamer.sticks.common.network.frame.Frame;

public class Client {
    private SocketChannel channel;

    public Client(SocketChannel channel) {
        this.channel = channel;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public void write(Frame message) {
        channel.writeAndFlush(message);
    }
}
