package net.client;

import net.IdentifiedMoomintroll;

public interface CommandHandler {
    void add(IdentifiedMoomintroll[] moomintrolls);

    void remove(long[] ids);

    void update(IdentifiedMoomintroll moomintroll);

    void reload(IdentifiedMoomintroll[] moomintrolls);
}
