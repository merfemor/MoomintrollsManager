package net.protocol;

import java.io.IOException;

public class MAnswer extends MCommand {
    public MAnswer(byte[] data) {
        super(data);
    }

    public static MAnswer createSelectAllAnswer(IdentifiedMoomintroll[] moomintrolls) throws IOException {
        return new MAnswer(createCommand(Type.SELECT_ALL, moomintrolls).data());
    }

    public IdentifiedMoomintroll[] parseSelectAll() throws IllegalArgumentException, IOException, ClassNotFoundException {
        if (type != Type.ADD) {
            throw new IllegalArgumentException(
                    "Bad type of command: expected \"ADD\", received " + type);
        }
        return (IdentifiedMoomintroll[]) parseCommand(this);
    }
}
