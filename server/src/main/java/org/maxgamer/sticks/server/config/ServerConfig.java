package org.maxgamer.sticks.server.config;

import org.maxgamer.sticks.common.world.Direction;
import org.maxgamer.sticks.server.Settings;
import org.maxgamer.sticks.common.clock.Clock;
import org.maxgamer.sticks.common.clock.Tickable;
import org.maxgamer.sticks.server.model.Creature;
import org.maxgamer.sticks.server.model.World;
import org.maxgamer.sticks.server.model.factory.WorldFactory;
import org.maxgamer.sticks.common.network.FrameFactory;
import org.maxgamer.sticks.server.network.NettyServer;
import org.maxgamer.sticks.server.network.NetworkWorldStateVisitor;
import org.maxgamer.sticks.common.network.frame.MoveFrame;
import org.maxgamer.sticks.common.network.frame.Opcodes;
import org.maxgamer.sticks.common.network.frame.TickFrame;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;
import java.util.WeakHashMap;

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
        Clock clock = new Clock(Settings.TICKS_PER_SECOND);
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
    public World world() throws IOException {
        try (InputStream state = getClass().getClassLoader().getResourceAsStream("world.json")) {
            return WorldFactory.load(state);
        }
    }

    @Bean
    public Tickable wanderTick(World world) {

        return new Tickable() {
            private WeakHashMap<Creature, Long> moves = new WeakHashMap<>();
            private Random random = new Random();

            @Override
            public boolean tick() throws Exception {
                for (Creature creature : world.getCreatures()) {
                    if (creature.getAi() != Creature.AI.WANDER) {
                        return false;
                    }

                    if (random.nextInt(10) != 0) {
                        // 1 in 10 chance every tick of moving, 16 ticks per second
                        // So, 1.6 move attempts per second
                        continue;
                    }

                    long lastMoveAt = moves.getOrDefault(creature, 0L);
                    if (lastMoveAt > System.currentTimeMillis() - 2400) {
                        continue;
                    }

                    int dirId = random.nextInt(Direction.values().length);
                    Direction dir = Direction.values()[dirId];

                    world.move(creature.getId(), dir);

                    moves.put(creature, System.currentTimeMillis());
                }

                return false;
            }
        };
    }
}
