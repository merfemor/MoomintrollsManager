package net.protocol;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.protocol.MPacket.PACKETS_LENGTH;

public class MCommand {
    protected final byte type;
    protected final byte[] data;
    public MCommand(byte[] data) {
        this.data = data;
        type = data[0];
    }

    public static MCommand fromPackets(List<MPacket> packets) {
        byte[] data = new byte[packets.size() * PACKETS_LENGTH];

        // TODO: replace with Streams API method or smth like that
        for (int i = 0; i < packets.size(); i++) {
            byte[] content = packets.get(i).getContent();
            for (int j = 0; j < content.length; j++) {
                data[i * PACKETS_LENGTH + j] = content[j];
            }
        }
        return new MCommand(Arrays.copyOfRange(data, 4, data.length));
    }

    protected static MCommand createCommand(byte type, Object object) throws IllegalArgumentException, IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(type);
        if (object != null) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            objectOutputStream.close();
        }
        MCommand command = new MCommand(os.toByteArray());
        os.close();
        return command;
    }

    protected static Object parseCommand(MCommand command) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(
                command.data, 1, command.data.length - 1);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object o = ois.readObject();
        ois.close();
        bis.close();
        return o;
    }

    public byte type() {
        return type;
    }

    public byte[] data() {
        return data;
    }

    public List<MPacket> toPackets() {
        List<MPacket> packets = new ArrayList<>();
        int packetsNum = (int) Math.ceil((double) (data.length + 4) / PACKETS_LENGTH);
        ByteBuffer buffer = ByteBuffer.allocate(4 + data.length);
        buffer.putInt(packetsNum);
        buffer.put(data);
        byte[] bytes = buffer.array();

        for (int i = 0; i < packetsNum; i++) {
            MPacket packet = new MPacket(
                    Arrays.copyOfRange(bytes,
                            i * PACKETS_LENGTH,
                            Math.min(bytes.length, (i + 1) * PACKETS_LENGTH))
            );
            packets.add(packet);
        }
        return packets;
    }

    public static class Type {
        public static final byte ADD = 0,
                REMOVE = 1,
                UPDATE = 2,
                SELECT_ALL = 3,
                DISCONNECT = -1;

        private Type() {

        }

        public static String name(final byte value) {
            switch (value) {
                case 0:
                    return "ADD";
                case 1:
                    return "REMOVE";
                case 2:
                    return "UPDATE";
                case 3:
                    return "SELECT_ALL";
                case -1:
                    return "DISCONNECT";
            }
            return null;
        }
    }

}
