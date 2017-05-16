package net.protocol;

import com.google.common.primitives.Bytes;
import trolls.Moomintroll;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.protocol.MPacket.PACKETS_LENGTH;

public class MCommand {
    public class Type {
        public static final byte ADD = 0,
                REMOVE = 1,
                UPDATE = 2,
                SELECT_ALL = 3,
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

    private static MCommand createCommand(byte type, Object object) throws IllegalArgumentException, IOException {
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

    public static MCommand createRemoveCommand(long[] ids) throws IOException {
        return createCommand(Type.REMOVE, ids);
    }

    public static MCommand createAddCommand(Moomintroll[] moomintrolls) throws IOException {
        return createCommand(Type.ADD, moomintrolls);
    }

    public static MCommand createUpdateCommand(IdentifiedMoomintroll moomintroll) throws IOException {
        return createCommand(Type.UPDATE, moomintroll);
    }

    public static MCommand createSelectAllCommand() throws IOException {
        return createCommand(Type.SELECT_ALL, null);
    }

    public static MCommand createDisconnectCommand() throws IOException {
        return createCommand(Type.DISCONNECT, null);
    }

    private static Object parseCommand(MCommand command) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(
                command.data, 1, command.data.length - 1);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object o = ois.readObject();
        ois.close();
        bis.close();
        return o;
    }

    public static long[] parseRemoveCommand(MCommand command) throws IllegalArgumentException, IOException, ClassNotFoundException {
        if (command.type() != Type.REMOVE) {
            throw new IllegalArgumentException(
                    "Bad type of command: expected \"REMOVE\", received " + command.type());
        }
        return (long[]) parseCommand(command);
    }

    public static Moomintroll[] parseAddCommand(MCommand command) throws IllegalArgumentException, IOException, ClassNotFoundException {
        if (command.type() != Type.ADD) {
            throw new IllegalArgumentException(
                    "Bad type of command: expected \"ADD\", received " + command.type());
        }
        return (Moomintroll[]) parseCommand(command);
    }

    public static IdentifiedMoomintroll parseUpdateCommand(MCommand command) throws IllegalArgumentException, IOException, ClassNotFoundException {
        if (command.type() != Type.UPDATE) {
            throw new IllegalArgumentException(
                    "Bad type of command: expected \"UPDATE\", received " + command.type());
        }
        return (IdentifiedMoomintroll) parseCommand(command);
    }

}
