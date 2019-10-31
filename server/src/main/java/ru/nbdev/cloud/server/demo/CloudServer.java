package ru.nbdev.cloud.server.demo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class CloudServer {
    public static void main(String[] args) {
        // serialize();
        stream();
    }

    public static void stream() {
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            try (Socket socket = serverSocket.accept()) {
                try (
                        BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
                        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("__1.txt"))
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
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            System.out.println("Сервер запущен. Ждем подключение клиента...");

            FileMessage inFileMessage = null;
            try (Socket socket = serverSocket.accept()) {
                try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                    inFileMessage = (FileMessage) in.readObject();
                }
            }
            try (FileOutputStream out = new FileOutputStream("_" + inFileMessage.getFilename())) {
                out.write(inFileMessage.getData());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
