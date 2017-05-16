package net.protocol;

import java.math.BigInteger;
import java.util.Arrays;

public class MPacket {
    public static final int PACKETS_LENGTH = 512;
    private byte[] content;

    public MPacket(byte[] content) {
        this.content = content;
    }

    public int packetsNumber() {
        return new BigInteger(Arrays.copyOfRange(content, 0, 4)).intValue();
    }

    public byte[] getContent() {
        return content;
    }
}
