package ru.nbdev.cloud.server.demo;

import java.io.*;
import java.net.Socket;

public class CloudClient {
    public static void main(String[] args) {
        // serialize();
        stream();
    }

    public static void stream() {
        try {
            try (Socket socket = new Socket("localhost", 8189)) {
                try (
                        BufferedInputStream in = new BufferedInputStream(new FileInputStream("1.txt"));
                        BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream())
                ) {
                    int x = -1;
                    while ((x = in.read()) != -1) {
                        out.write(x);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void serialize() {
        try {
            FileMessage outFileMessage = new FileMessage("1.txt");
            try (Socket socket = new Socket("localhost", 8189)) {
                try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                    out.writeObject(outFileMessage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
