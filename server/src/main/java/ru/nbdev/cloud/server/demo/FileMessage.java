package ru.nbdev.cloud.server.demo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileMessage implements Serializable {
    private static final long serialVersionUID = 5120173274117216201L;

    private String filename;
    private byte[] data;

    public String getFilename() {
        return filename;
    }

    public byte[] getData() {
        return data;
    }

    public FileMessage(String filename) throws IOException {
        this.filename = filename;
        int fileLength = new Long(new File(filename).length()).intValue();
        this.data = new byte[fileLength];
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(filename))) {
            for (int i = 0; i < fileLength; i++) {
                data[i] = (byte)in.read();
            }
        }
    }
}
