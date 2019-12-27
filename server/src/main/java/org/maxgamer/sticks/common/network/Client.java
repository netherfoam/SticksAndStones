package org.maxgamer.sticks.common.network;

import io.netty.channel.socket.SocketChannel;
import org.maxgamer.sticks.common.network.frame.Frame;

public class Client {
    private int playerId;
    private SocketChannel channel;

    public Client(int playerId, SocketChannel channel) {
        this.playerId = playerId;
        this.channel = channel;
    }

    public int getPlayerId() {
        return playerId;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public void write(Frame message) {
        channel.write(message);
    }
}
