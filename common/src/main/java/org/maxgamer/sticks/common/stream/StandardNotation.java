package org.maxgamer.sticks.common.stream;

import java.io.IOException;

public class StandardNotation {
    public static final Notation<String> STRING = new Notation<String>() {
        @Override
        public void write(BinaryOutputStream out, String value) throws IOException {
            for (char c : value.toCharArray()) {
                out.write(c);
            }

            out.write(0x00);
        }

        @Override
        public String read(BinaryInputStream in) throws IOException {
            StringBuilder sb = new StringBuilder();
            byte b;
            while ((b = in.readByte()) != 0) {
                sb.append((char) b);
            }
            return sb.toString();
        }
    };

    public static final Notation<Integer> SHORT_A = new Notation<Integer>() {
        @Override
        public void write(BinaryOutputStream out, Integer value) throws IOException {
            int b1 = value & 0xFF;
            int b2 = (value >> 8) & 0xFF;

            out.write(b2);
            out.write(b1 + 128);
        }

        @Override
        public Integer read(BinaryInputStream in) throws IOException {
            int b1 = in.read();
            int b2 = in.read();

            return (((b1 & 0xff) << 8) + (b2 - 128 & 0xff));
        }
    };
}
