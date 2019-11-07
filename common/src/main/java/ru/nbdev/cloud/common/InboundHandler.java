package ru.nbdev.cloud.common;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.nbdev.cloud.common.messages.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

public class InboundHandler extends ChannelInboundHandlerAdapter {

    private ReceiveFileHandler handler = new ReceiveFileHandler();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client auth...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Client message: " + msg.getClass().getSimpleName());
        //ctx.writeAndFlush(new MyMessage("Hello Client!"));

        if (msg instanceof SendFileMessage) {
            receiveFile(ctx, (SendFileMessage) msg);
        } else if (msg instanceof FileChunkMessage) {
            receiveFileChunk(ctx, (FileChunkMessage) msg);
        } else if (msg instanceof GetFilesListMessage) {
            getFilesList(ctx, (GetFilesListMessage) msg);
        } else if (msg instanceof DeleteFilesMessage) {
            deleteFiles(ctx, (DeleteFilesMessage) msg);
        }


    }

    private void deleteFiles(ChannelHandlerContext ctx, DeleteFilesMessage msg) {
        msg.getFiles().forEach(fileName -> {
            if (fileName.length() > 0) {
                Path path = Paths.get("server_storage/" + fileName);
                try {
                    Files.delete(path);

                    System.out.println("Deleted file: " + path.toAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getFilesList(ChannelHandlerContext ctx, GetFilesListMessage msg) {
        Path rootPath = Paths.get("server_storage/" + msg.getPath());
        try {
            Map<String, Long> files = Files.list(rootPath)
                    .filter(path -> !Files.isDirectory(path))
                    .collect(Collectors.toMap(path -> path.getFileName().toString(), path -> {
                        long size = 0;
                        try {
                            size = Files.size(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return size;
                    }));

            FilesListMessage message = new FilesListMessage(files);
            ctx.writeAndFlush(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveFile(ChannelHandlerContext ctx, SendFileMessage msg) {
        handler.startReceiveFile(msg);
    }

    private void receiveFileChunk(ChannelHandlerContext ctx, FileChunkMessage msg) {
        handler.receiveFileChunk(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
