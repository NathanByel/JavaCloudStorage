package ru.nbdev.cloud.common.messages;

public class GetFileMessage extends AbstractMessage {
    private String fileName;

    public GetFileMessage(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
