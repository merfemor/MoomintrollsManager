package net.protocol;

// TODO: make class MHeaderPacket
public class MPacket {
    private boolean isHeader;
    public String content;

    MPacket(byte[] content, boolean isHeader) {
        this.content = new String(content);
        this.isHeader = isHeader;
    }

    public byte[] getContent() {
        return content.getBytes();
    }

    public boolean isHeader() {
        return isHeader;
    }
}
