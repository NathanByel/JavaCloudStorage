package ru.nbdev.cloud.common;

import ru.nbdev.cloud.common.messages.FileChunkMessage;
import ru.nbdev.cloud.common.messages.SendFileMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReceiveFileHandler {

    long fileSize = 0;
    long fileChunks = 0;
    OutputStream outputStream;

    public void startReceiveFile(SendFileMessage msg) {
        System.out.println("Name: " + msg.getFileName());
        System.out.println("Chunks: " + msg.getChunksCount());
        System.out.println("Size: " + msg.getFileSize());

        fileSize = msg.getFileSize();
        fileChunks = msg.getChunksCount();
        try {
            Path path = Paths.get("server_storage/" + msg.getFileName());
            outputStream = Files.newOutputStream(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveFileChunk(FileChunkMessage msg) {
        System.out.println("Chunk num: " + msg.getChunkNumber());
        try {
            outputStream.write(msg.getData());
            if (msg.getChunkNumber() == fileChunks) {
                outputStream.close();
                System.out.println("End");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
