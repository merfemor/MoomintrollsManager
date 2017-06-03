package net.protocol;

import trolls.Moomintroll;

import java.io.IOException;

public class MRequest extends MCommand {

    private MRequest(byte[] data) {
        super(data);
    }

    public static MCommand createSelectAllRequest() throws IOException {
        return createCommand(Type.SELECT_ALL, null);
    }

    public static MCommand createDisconnectRequest() throws IOException {
        return createCommand(Type.DISCONNECT, null);
    }

    public static MCommand createRemoveRequest(long[] ids) throws IOException {
        return createCommand(Type.REMOVE, ids);
    }

    public static MCommand createAddRequest(Moomintroll[] moomintrolls) throws IOException {
        return createCommand(Type.ADD, moomintrolls);
    }

    public static MCommand createUpdateRequest(Moomintroll moomintroll) throws IOException {
        return createCommand(Type.UPDATE, moomintroll);
    }

    public static long[] parseRemoveRequest(MCommand command) throws IllegalArgumentException, IOException, ClassNotFoundException {
        if (command.type() != Type.REMOVE) {
            throw new IllegalArgumentException(
                    "Bad type of command: expected \"REMOVE\", received " + command.type());
        }
        return (long[]) parseCommand(command);
    }

    public static Moomintroll[] parseAddRequest(MCommand command) throws IllegalArgumentException, IOException, ClassNotFoundException {
        if (command.type() != Type.ADD) {
            throw new IllegalArgumentException(
                    "Bad type of command: expected \"ADD\", received " + command.type());
        }
        return (Moomintroll[]) parseCommand(command);
    }

    public static Moomintroll parseUpdateRequest(MCommand command) throws IllegalArgumentException, IOException, ClassNotFoundException {
        if (command.type() != Type.UPDATE) {
            throw new IllegalArgumentException(
                    "Bad type of command: expected \"UPDATE\", received " + command.type());
        }
        return (Moomintroll) parseCommand(command);
    }
}
