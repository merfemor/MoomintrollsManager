package net.protocol;

import trolls.Moomintroll;

import java.io.Serializable;

public class IdentifiedMoomintroll implements Serializable {
    private final long id;
    private final Moomintroll moomintroll;

    public IdentifiedMoomintroll(long id, Moomintroll moomintroll) {
        this.moomintroll = moomintroll;
        this.id = id;
    }

    public long id() {
        return id;
    }

    public Moomintroll moomintroll() {
        return moomintroll;
    }
}
