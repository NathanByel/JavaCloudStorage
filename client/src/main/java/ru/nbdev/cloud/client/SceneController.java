package ru.nbdev.cloud.client;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class SceneController {
    private static SceneController instance = new SceneController();
    private Stage stage;

    public static SceneController getInstance() {
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setOnCloseHandler(EventHandler<WindowEvent> onCloseHandler) {
        stage.setOnCloseRequest(onCloseHandler);
    }

    public void switchScene(String sceneFile) {
        switchScene(null, sceneFile);
    }

    public void switchScene(String title, String sceneFile) {
        if (stage != null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource(sceneFile));
                if (title != null) {
                    stage.setTitle(title);
                }

                //stage.setOnCloseRequest(null);
                stage.setScene(new Scene(root));
                stage.show();
                stage.centerOnScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
