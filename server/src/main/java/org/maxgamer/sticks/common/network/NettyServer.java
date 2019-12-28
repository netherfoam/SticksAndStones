package org.maxgamer.sticks.common.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import org.maxgamer.sticks.common.model.World;
import org.maxgamer.sticks.common.model.state.CreatureAdd;
import org.maxgamer.sticks.common.network.frame.IdentityFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NettyServer {
    private static final AttributeKey<Client> CLIENT_ATTR = AttributeKey.newInstance("client");

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
                                .addLast("decoder", new GameDecoder(frameFactory))
                                .addLast("handler", new GameHandler(playerId, world))
                                .addFirst("encoder", new GameEncoder());

                        Client client = new Client(playerId, channel);
                        clients.add(client);
                        channel.attr(CLIENT_ATTR).set(client);

                        IdentityFrame identity = new IdentityFrame();
                        identity.setIdentity(playerId);
                        channel.writeAndFlush(identity);

                        CreatureAdd add = new CreatureAdd(playerId, 1, 50, 50);
                        world.onChange(add);
                    }

                    @Override
                    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
                        //Client client = ctx.attr(CLIENT_ATTR).get();
                        //clients.remove(client);
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
