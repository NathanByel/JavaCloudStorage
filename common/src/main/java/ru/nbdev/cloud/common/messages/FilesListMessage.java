package ru.nbdev.cloud.common.messages;

import java.util.Map;

public class FilesListMessage extends AbstractMessage {
    private Map<String, Long> files;

    public FilesListMessage(Map<String, Long> files) {
        this.files = files;
    }

    public Map<String, Long> getFiles() {
        return files;
    }
}
