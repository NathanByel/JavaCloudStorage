package ru.nbdev.cloud.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private ListView<FileInfo> filesListLeft;
    @FXML private ListView<FileInfo> filesListRight;
    @FXML private Button btnMoveToLeft;
    @FXML private Button btnMoveToRight;
    @FXML private Button btnDelete;

    private ObservableList<FileInfo> filesObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filesObservableList = FXCollections.observableArrayList();

        filesObservableList.addAll(
                new FileInfo(true, (byte)0,"File", 124),
                new FileInfo(true, (byte)0,"FileFile", 1240),
                new FileInfo(false, (byte)50,"FileFileFile", 12400),
                new FileInfo(false, (byte)100,"File", 1240000),
                new FileInfo(true, (byte)0,"File", 12400000),
                new FileInfo(true, (byte)0,"FileFileFile", 1240000000)
        );

        filesListLeft.setCellFactory(new Callback<ListView<FileInfo>, ListCell<FileInfo>>() {
            @Override
            public ListCell<FileInfo> call(ListView<FileInfo> studentListView) {
                return new FilesListViewCell();
            }
        });
        filesListLeft.setItems(filesObservableList);

        ObservableList<FileInfo> filesObservableList2 = FXCollections.observableArrayList();
        filesListRight.setCellFactory(new Callback<ListView<FileInfo>, ListCell<FileInfo>>() {
            @Override
            public ListCell<FileInfo> call(ListView<FileInfo> studentListView) {
                return new FilesListViewCell();
            }
        });
        filesListRight.setItems(filesObservableList2);

        btnMoveToRight.setOnAction(event -> {
            filesObservableList2.clear();
            for (FileInfo o : filesObservableList.toArray(new FileInfo[0])) {
                if (o.isSelected()) {
                    filesObservableList2.add(o);
                }
            }
        });
    }
}
