package ru.nbdev.cloud.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class FilesListViewCell extends ListCell<FileInfo> {

    @FXML private ProgressBar progressBar;
    @FXML private CheckBox checkBox;
    @FXML private Label lblName;
    @FXML private Label lblSize;
    @FXML private AnchorPane anchorPane;

    private FXMLLoader fxmlLoader;

    @Override
    protected void updateItem(FileInfo fileInfo, boolean empty) {
        super.updateItem(fileInfo, empty);

        if (empty || fileInfo == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/files_list_cell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            checkBox.setSelected(fileInfo.isSelected());
            checkBox.setOnAction(event -> {
                fileInfo.setSelected(checkBox.isSelected());
            });

            progressBar.setProgress(fileInfo.getProgress() / 100f);
            lblName.setText(fileInfo.getName());
            lblSize.setText(formatSize(fileInfo.getSize()));

            setText(null);
            setGraphic(anchorPane);
        }
    }

    private String formatSize(long size) {
        if (size < 1000) {
            return String.format("%d b", size);
        } else if (size < 1_000_000) {
            return String.format("%.2f Kb", size / 1000f);
        } else if (size < 1_000_000_000) {
            return String.format("%.2f Mb", size / 1_000_000f);
        } else {
            return String.format("%.2f Gb", size / 1_000_000_000f);
        }
    }
}
