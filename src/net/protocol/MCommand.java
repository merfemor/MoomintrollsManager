package net.protocol;

import com.google.common.primitives.Bytes;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.protocol.MPacket.PACKETS_LENGTH;

public class MCommand {
    public class Type {
        public static final byte ADD = 0,
                MULTIPLE_ADD = 1,
                REMOVE = 2,
                MULTIPLE_REMOVE = 3,
                UPDATE = 4,
                SELECT_ALL = 5,
                DISCONNECT = -1;

        private Type() {
        }
    }

    private final byte type;
    private final byte[] data;

    private MCommand(byte[] data) {
        this.data = data;
        type = data[0];
    }

    public byte type() {
        return type;
    }

    public byte[] getData() {
        return data;
    }

    public static long[] parseRemoveCommand(MCommand command) throws IllegalArgumentException, IOException, ClassNotFoundException {
        if (command.type() != Type.REMOVE && command.type() != Type.MULTIPLE_REMOVE) {
            throw new IllegalArgumentException(
                    "Can't parse remove command from not remove type (= " + command.type() + ") command");
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(
                command.data, 1, command.data.length - 1);
        ObjectInputStream ois = new ObjectInputStream(bis);
        long[] ids = (long[]) ois.readObject();
        ois.close();
        bis.close();
        return ids;
    }

    public static MCommand createRemoveCommand(long[] ids) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write((ids.length > 1) ? Type.MULTIPLE_REMOVE : Type.REMOVE);

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
        objectOutputStream.writeObject(ids);
        objectOutputStream.flush();
        objectOutputStream.close();
        MCommand command = new MCommand(os.toByteArray());
        os.close();
        return command;
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

    public List<MPacket> toPackets() {
        List<MPacket> packets = new ArrayList<>();
        int packetsNum = (int) Math.ceil((double) (data.length + 4) / PACKETS_LENGTH);
        byte[] bytes = Bytes.concat(ByteBuffer.allocate(4).putInt(packetsNum).array(), data);

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
}
