package net.protocol;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class MProtocol {
    public static final int PACKETS_LENGTH = 512;
    public static final byte HEADER_PACKET = 0;

    public static int packetsNumber(MPacket header) {
        if (!header.isHeader())
            return -1;
        return new BigInteger(Arrays.copyOfRange(header.getContent(), 1, 5)).intValue();
    }

    public static MPacket generatePacket(String data) {
        if (data.length() > PACKETS_LENGTH - 5) {
            return null;
        }
        ByteBuffer buffer = ByteBuffer.allocate(5 + data.length());
        buffer.put((byte) 0);
        buffer.putInt(1);
        buffer.put(data.getBytes());
        return new MPacket(buffer.array(), true);
    }

    public static MPacket parsePacket(byte[] content) {
        return new MPacket(content, content[0] == HEADER_PACKET);
    }

    private MProtocol() {
    }
}
