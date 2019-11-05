package ru.nbdev.cloud.client;

public class FileInfo {
    private boolean selected;
    private byte progress;
    private String name;
    private long size;

    public FileInfo(boolean selected, byte progress, String name, long size) {
        this.selected = selected;
        this.progress = progress;
        this.name = name;
        this.size = size;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public byte getProgress() {
        return progress;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }
}
