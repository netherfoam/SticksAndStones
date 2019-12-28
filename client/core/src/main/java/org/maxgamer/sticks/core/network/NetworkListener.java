package org.maxgamer.sticks.core.network;

import org.maxgamer.sticks.common.network.FrameFactory;
import org.maxgamer.sticks.common.network.frame.Frame;
import org.maxgamer.sticks.common.network.frame.IdentityFrame;
import org.maxgamer.sticks.common.network.frame.TickFrame;
import org.maxgamer.sticks.common.stream.BinaryInputStream;
import org.maxgamer.sticks.core.Game;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;

public class NetworkListener {
    private static final int MAX_BUFFER = 1024 * 64;

    private Game game;
    private Executor executor;
    private Thread thread;
    private BufferedInputStream input;
    private FrameFactory factory;

    public NetworkListener(Game game, Executor executor, FrameFactory factory) {
        this.game = game;
        this.executor = executor;
        this.factory = factory;

        this.thread = new Thread(() -> {
            try {
                while (true) {
                    input.mark(MAX_BUFFER);
                    BinaryInputStream bin = new BinaryInputStream(input);
                    Frame frame = factory.decode(bin);

                    executor.execute(() -> {
                        if (frame instanceof TickFrame) {
                            this.game.handle((TickFrame) frame);
                        }

                        if (frame instanceof IdentityFrame) {
                            this.game.handle((IdentityFrame) frame);
                        }
                    });

                    if (Thread.interrupted()) {
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void listen(InputStream input) {
        this.input = new BufferedInputStream(input, MAX_BUFFER);

        this.thread.start();
    }

    public void stop() {
        thread.interrupt();
    }
}
