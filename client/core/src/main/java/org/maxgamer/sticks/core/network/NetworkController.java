package org.maxgamer.sticks.core.network;

import org.maxgamer.sticks.common.network.frame.MoveFrame;
import org.maxgamer.sticks.common.stream.BinaryOutputStream;

import java.io.*;
import java.net.Socket;

public class NetworkController {
    private Socket socket;

    private OutputStream out;
    private InputStream in;

    public void connect(String host, int port) throws IOException {
        this.socket = new Socket(host, port);

        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
    }

    public void move(int dx, int dy) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (BinaryOutputStream bos = new BinaryOutputStream(baos)) {
                MoveFrame frame = new MoveFrame();
                frame.setDx((byte) dx);
                frame.setDy((byte) dy);

                bos.writeByte(MoveFrame.OPCODE);
                frame.write(bos);
            }

            out.write(baos.toByteArray());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public InputStream input() {
        return in;
    }
}
