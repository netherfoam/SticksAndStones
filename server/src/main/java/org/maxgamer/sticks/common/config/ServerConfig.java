package org.maxgamer.sticks.common.config;

import org.maxgamer.sticks.common.clock.Clock;
import org.maxgamer.sticks.common.clock.Tickable;
import org.maxgamer.sticks.common.model.World;
import org.maxgamer.sticks.common.network.FrameFactory;
import org.maxgamer.sticks.common.network.NettyServer;
import org.maxgamer.sticks.common.network.NetworkWorldStateVisitor;
import org.maxgamer.sticks.common.network.frame.MoveFrame;
import org.maxgamer.sticks.common.network.frame.Opcodes;
import org.maxgamer.sticks.common.network.frame.TickFrame;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ComponentScan(basePackageClasses = NettyServer.class)
public class ServerConfig {
    @Bean
    public FrameFactory frameFactory() {
        FrameFactory factory = new FrameFactory();
        factory.register(Opcodes.TICK, TickFrame::new);
        factory.register(Opcodes.MOVE, MoveFrame::new);

        return factory;
    }

    @Bean
    public Clock clock(List<Tickable> tickables) {
        Clock clock = new Clock();
        clock.start();

        tickables.forEach(clock::subscribe);

        return clock;
    }

    @Bean
    public Tickable updateTick(NettyServer server) {
        return new NetworkWorldStateVisitor(server);
    }

    @Bean
    public NetworkWorldStateVisitor networkWorldStateVisitor(NettyServer server, World world) {
        NetworkWorldStateVisitor visitor = new NetworkWorldStateVisitor(server);
        world.setStateChangeVisitor(visitor);

        return visitor;
    }

    @Bean
    public World world() {
        return new World();
    }
}
