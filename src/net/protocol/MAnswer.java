package net.protocol;

import net.IdentifiedMoomintroll;

import java.io.IOException;

// TODO: get rid of many parse...() and create...()
public class MAnswer extends MCommand {
    public MAnswer(byte[] data) {
        super(data);
    }

    public static MAnswer createSelectAllAnswer(IdentifiedMoomintroll[] moomintrolls) throws IOException {
        return new MAnswer(createCommand(Type.SELECT_ALL, moomintrolls).data());
    }

    public IdentifiedMoomintroll[] parseAdd() throws IllegalArgumentException, IOException, ClassNotFoundException {
        if (type != Type.ADD) {
            throw new IllegalArgumentException(
                    "Bad type of command: expected \"ADD\", received " + type);
        }
        return (IdentifiedMoomintroll[]) parseCommand(this);
    }

    public long[] parseRemove() throws IllegalArgumentException, IOException, ClassNotFoundException {
        if (type != Type.REMOVE) {
            throw new IllegalArgumentException(
                    "Bad type of command: expected \"REMOVE\", received " + type);
        }
        return (long[]) parseCommand(this);
    }

    public IdentifiedMoomintroll parseUpdate() throws IllegalArgumentException, IOException, ClassNotFoundException {
        if (type != Type.UPDATE) {
            throw new IllegalArgumentException(
                    "Bad type of command: expected \"UPDATE\", received " + type);
        }
        return (IdentifiedMoomintroll) parseCommand(this);
    }

    public IdentifiedMoomintroll[] parseSelectAll() throws IllegalArgumentException, IOException, ClassNotFoundException {
        if (type != Type.SELECT_ALL) {
            throw new IllegalArgumentException(
                    "Bad type of command: expected \"SELECT_ALL\", received " + type);
        }
        return (IdentifiedMoomintroll[]) parseCommand(this);
    }
}
