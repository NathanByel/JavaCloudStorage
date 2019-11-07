package ru.nbdev.cloud.common.messages;

public class FileChunkMessage extends AbstractMessage {

    private long chunkNumber;
    private byte[] data;

    public FileChunkMessage(long chunkNumber, byte[] data) {
        this.chunkNumber = chunkNumber;
        this.data = data;
    }

    public long getChunkNumber() {
        return chunkNumber;
    }

    public byte[] getData() {
        return data;
    }
}
