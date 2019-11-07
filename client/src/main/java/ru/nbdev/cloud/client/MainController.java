package ru.nbdev.cloud.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import ru.nbdev.cloud.client.common.Helpers;
import ru.nbdev.cloud.common.messages.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController implements Initializable {

    @FXML private ListView<FileInfo> filesListLeft;
    @FXML private ListView<FileInfo> filesListRight;
    @FXML private Button btnMoveToLeft;
    @FXML private Button btnMoveToRight;
    @FXML private Button btnDelete;
    @FXML private Button btnRefresh;

    private static final String CLIENT_ROOT_DIR = "client_storage/";
    private ObservableList<FileInfo> localFilesObservableList;
    private ObservableList<FileInfo> remoteFilesObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        localFilesObservableList = FXCollections.observableArrayList();
        remoteFilesObservableList = FXCollections.observableArrayList();

        initLists();

        updateLocalFilesList();
        updateRemoteFilesList();

        btnMoveToRight.setOnAction(event -> {
            uploadFiles();
        });

        btnMoveToLeft.setOnAction(event -> {
            downloadFiles();
        });

        btnDelete.setOnAction(event -> {
            deleteFiles();
        });

        btnRefresh.setOnAction(event -> {
            updateLocalFilesList();
            updateRemoteFilesList();
        });
    }

    private void deleteFiles() {
        deleteLocalFiles();
        deleteRemoteFiles();
    }

    private void deleteRemoteFiles() {
        List<String> remoteFiles = filesListRight.getItems().stream()
                .filter(FileInfo::isSelected)
                .map(FileInfo::getName)
                .collect(Collectors.toList());

        Network.sendMsg(new DeleteFilesMessage(remoteFiles));
        updateRemoteFilesList();
    }

    private void deleteLocalFiles() {
        filesListLeft.getItems().stream()
                .filter(FileInfo::isSelected)
                .map(fileInfo -> Paths.get(fileInfo.getName()))
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        updateLocalFilesList();
    }

    private void initLists() {
        filesListLeft.setCellFactory(studentListView -> new FilesListViewCell());
        filesListLeft.setItems(localFilesObservableList);
        filesListLeft.setSelectionModel(new DisabledSelectionModel<>());

        filesListRight.setCellFactory(studentListView -> new FilesListViewCell());
        filesListRight.setItems(remoteFilesObservableList);
        filesListLeft.setSelectionModel(new DisabledSelectionModel<>());
    }

    private void updateLocalFilesList() {
        localFilesObservableList.clear();
        Path rootPath = Paths.get(CLIENT_ROOT_DIR);
        try {
            Files.list(rootPath)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        try {
                            localFilesObservableList.add(new FileInfo(path.getFileName().toString(), Files.size(path)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateRemoteFilesList() {
        try {
            Network.sendMsg(new GetFilesListMessage(""));
            AbstractMessage message = Network.readObject();
            if (message instanceof FilesListMessage) {
                Map<String, Long> files = ((FilesListMessage) message).getFiles();
                remoteFilesObservableList.clear();
                files.forEach((name, size) -> {
                    remoteFilesObservableList.add(new FileInfo(name, size));
                });

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadFiles() {
        List<FileInfo> filesToUpload = filesListLeft.getItems().stream()
                .filter(FileInfo::isSelected)
                .collect(Collectors.toList());

        System.out.println(filesToUpload);

        new Thread(() -> {
            filesToUpload.forEach(fileInfo -> {
                Path path = Paths.get(CLIENT_ROOT_DIR + fileInfo.getName());
                try {
                    String fileName = path.getFileName().toString();
                    long fileSize = Files.size(path);
                    Network.sendMsg(new SendFileMessage(fileName, fileSize));

                    long chunksCount = Files.size(path) / SendFileMessage.CHUNK_SIZE;

                    InputStream r = Files.newInputStream(path);
                    byte[] data = new byte[SendFileMessage.CHUNK_SIZE];
                    int chunkNum = 0;
                    while(r.available() > 0) {
                        int size = r.read(data);

                        if(size == SendFileMessage.CHUNK_SIZE) {
                            Network.sendMsg(new FileChunkMessage(chunkNum++, data));
                        } else {
                            byte[] remainsData = new byte[size];
                            System.arraycopy(data, 0, remainsData, 0, size);
                            Network.sendMsg(new FileChunkMessage(chunkNum++, remainsData));
                        }

                        fileInfo.setProgress( (byte)(chunkNum / (chunksCount / 100)));
                        filesListLeft.refresh();
                    }
                    fileInfo.setSelected(false);
                    fileInfo.setProgress((byte)0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            Helpers.runOnUiThread(() -> updateRemoteFilesList());
        }).start();

        //        try (Socket socket = new Socket("localhost", 8189)) {
//            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//
//            byte[] filenameBytes = "java.txt".getBytes();
//
//            out.writeLong(filenameBytes.length+1);
//            out.write(MsgType.SEND_FILE.getByte());
//            out.writeLong(1024);
//            out.write(filenameBytes);
//
//            out.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        Thread t = new Thread(() -> {
//            try {
//                while (true) {
//                    AbstractMessage msg = Network.readObject();
//                    /*if (am instanceof FileMessage) {
//                        //FileMessage fm = (FileMessage) am;
//                        //Files.write(Paths.get("client_storage/" + fm.getFilename()), fm.getData(), StandardOpenOption.CREATE);
//                        //refreshLocalFilesList();
//                    }*/
//                    System.out.println("Answer from server: " + msg.getClass().getSimpleName());
//                }
//            } catch (ClassNotFoundException | IOException e) {
//                e.printStackTrace();
//            } finally {
//                Network.stop();
//            }
//        });
//        t.setDaemon(true);
//        t.start();
//
//        Network.sendMsg(new ClientAuthMessage("test", "pass"));
    }

    private void downloadFiles() {
        List<String> files = filesListLeft.getItems().stream()
                .filter(FileInfo::isSelected)
                .map(FileInfo::getName)
                .collect(Collectors.toList());


    }
}
