package net.protocol;

import trolls.Moomintroll;

import java.io.IOException;

// TODO: get rid of many parse...() and create...()
public class MAnswer extends MCommand {
    public MAnswer(byte[] data) {
        super(data);
    }

    public static MAnswer createSelectAllAnswer(Moomintroll[] moomintrolls) throws IOException {
        return new MAnswer(createCommand(Type.SELECT_ALL, moomintrolls).data());
    }

    public static MAnswer createAddAnswer(Moomintroll[] moomintrolls) throws IOException {
        return new MAnswer(createCommand(Type.ADD, moomintrolls).data());
    }

    public Moomintroll[] parseAdd() throws IllegalArgumentException, IOException, ClassNotFoundException {
        if (type != Type.ADD) {
            throw new IllegalArgumentException(
                    "Bad type of command: expected \"ADD\", received " + type);
        }
        return (Moomintroll[]) parseCommand(this);
    }

    public long[] parseRemove() throws IllegalArgumentException, IOException, ClassNotFoundException {
        if (type != Type.REMOVE) {
            throw new IllegalArgumentException(
                    "Bad type of command: expected \"REMOVE\", received " + type);
        }
        return (long[]) parseCommand(this);
    }

    public Moomintroll parseUpdate() throws IllegalArgumentException, IOException, ClassNotFoundException {
        if (type != Type.UPDATE) {
            throw new IllegalArgumentException(
                    "Bad type of command: expected \"UPDATE\", received " + type);
        }
        return (Moomintroll) parseCommand(this);
    }

    public Moomintroll[] parseSelectAll() throws IllegalArgumentException, IOException, ClassNotFoundException {
        if (type != Type.SELECT_ALL) {
            throw new IllegalArgumentException(
                    "Bad type of command: expected \"SELECT_ALL\", received " + type);
        }
        return (Moomintroll[]) parseCommand(this);
    }
}
