package ru.nbdev.cloud.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.Map;

public class SceneController {

    public void switchScene(String scene) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/" + scene + ".fxml"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
