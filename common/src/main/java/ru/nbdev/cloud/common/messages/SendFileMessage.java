package ru.nbdev.cloud.common.messages;

public class SendFileMessage extends AbstractMessage {

    public static final int CHUNK_SIZE = 1_000_000;
    private String fileName;
    private long fileSize;
    private long chunksCount;

    public SendFileMessage(String fileName, long fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        chunksCount = fileSize / CHUNK_SIZE;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public long getChunksCount() {
        return chunksCount;
    }
}
