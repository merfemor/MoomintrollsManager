package net.client;

import trolls.Moomintroll;

public interface CommandHandler {
    void add(Moomintroll[] moomintrolls);

    void remove(long[] ids);

    void update(Moomintroll moomintroll);

    void reload(Moomintroll[] moomintrolls);
}
