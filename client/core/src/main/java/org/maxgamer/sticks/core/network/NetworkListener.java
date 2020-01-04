package org.maxgamer.sticks.core.network;

import org.maxgamer.sticks.common.network.FrameFactory;
import org.maxgamer.sticks.common.network.frame.Frame;
import org.maxgamer.sticks.common.network.frame.IdentityFrame;
import org.maxgamer.sticks.common.network.frame.Opcodes;
import org.maxgamer.sticks.common.network.frame.TickFrame;
import org.maxgamer.sticks.common.stream.BinaryInputStream;
import org.maxgamer.sticks.core.world.NetworkHandler;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.concurrent.Executor;

public class NetworkListener {
    private static final int MAX_BUFFER = 1024 * 64;
    private static FrameFactory FRAME_FACTORY;

    static {
        FRAME_FACTORY = new FrameFactory();

        FRAME_FACTORY.register(Opcodes.TICK, TickFrame::new);
        FRAME_FACTORY.register(Opcodes.IDENTITY, IdentityFrame::new);
    }

    private NetworkHandler networkHandler;
    private Executor executor;
    private Thread thread;
    private BufferedInputStream input;

    public NetworkListener(NetworkHandler networkHandler, Executor executor) {
        this.networkHandler = networkHandler;
        this.executor = executor;

        this.thread = new Thread(() -> {
            try {
                while (true) {
                    input.mark(MAX_BUFFER);
                    BinaryInputStream bin = new BinaryInputStream(input);
                    Frame frame = FRAME_FACTORY.decode(bin);

                    executor.execute(() -> {
                        if (frame instanceof TickFrame) {
                            this.networkHandler.handle((TickFrame) frame);
                        }

                        if (frame instanceof IdentityFrame) {
                            this.networkHandler.handle((IdentityFrame) frame);
                        }
                    });

                    if (Thread.interrupted()) {
                        return;
                    }
                }
            } catch (SocketException e) {
                if (!Thread.interrupted()) {
                    // If the thread is interrupted, this is expected because we're shutting down.
                    // If the thread wasn't interrupted, something closed our connection on us! :(
                    e.printStackTrace();
                }
            } catch (EOFException e) {
                // Server close detected - should close the client or something here
                e.printStackTrace();
            } catch (IOException e) {
                // Some other error occurred. Connection probably lost
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
