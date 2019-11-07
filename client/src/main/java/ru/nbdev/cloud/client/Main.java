package ru.nbdev.cloud.client;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        SceneController.getInstance().setStage(primaryStage);
        SceneController.getInstance().switchScene("Cloud Client", "/fxml/login.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
