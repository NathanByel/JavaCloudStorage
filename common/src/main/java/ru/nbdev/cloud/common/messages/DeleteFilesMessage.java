package ru.nbdev.cloud.common.messages;

import java.util.ArrayList;
import java.util.List;

public class DeleteFilesMessage extends AbstractMessage {
    private List<String> files;

    public DeleteFilesMessage(String fileName) {
        this(new ArrayList<>());
        files.add(fileName);
    }

    public DeleteFilesMessage(List<String> files) {
        this.files = files;
    }

    public List<String> getFiles() {
        return files;
    }
}
