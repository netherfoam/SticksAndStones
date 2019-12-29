package org.maxgamer.sticks.common.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.maxgamer.sticks.common.model.World;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NettyServer {
    private FrameFactory frameFactory;
    private World world;

    private EventLoopGroup[] groups;
    private int nextPlayerId = 1;
    private List<Client> clients = new ArrayList<>();

    @Autowired
    public NettyServer(FrameFactory frameFactory, World world) {
        this.frameFactory = frameFactory;
        this.world = world;
    }

    @EventListener
    public void onReady(ContextRefreshedEvent event) {
        this.bind();
    }

    @EventListener
    public void onFinish(ContextStoppedEvent event) {
        for (EventLoopGroup group : this.groups) {
            group.shutdownGracefully();
        }
    }

    public void bind() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        this.groups = new EventLoopGroup[]{bossGroup, workerGroup};

        ServerBootstrap server = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        int playerId = nextPlayerId++;

                        channel.pipeline()
                                .addLast("decoder", new GameDecoder(frameFactory, clients, new Client(playerId, channel)))
                                .addFirst("encoder", new GameEncoder())
                                .addLast("handler", new GameHandler(playerId, world));
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        server.bind(2713);
    }

    public List<Client> getClients() {
        return clients;
    }
}
