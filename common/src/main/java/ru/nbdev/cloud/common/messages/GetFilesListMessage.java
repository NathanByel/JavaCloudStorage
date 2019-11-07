package ru.nbdev.cloud.common.messages;

public class GetFilesListMessage extends AbstractMessage {
    private String path;

    public GetFilesListMessage(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
