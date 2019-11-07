package ru.nbdev.cloud.client.common;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class Helpers {
    public static void showMessage(String text) {
        // Создаем Alert, указываем текст и кнопки, которые на нем должны быть
        Alert alert = new Alert(Alert.AlertType.ERROR, text);
        // showAndWait() показывает Alert и блокирует остальное приложение пока мы не закроем Alert
        alert.showAndWait();
    }

    public static void runOnUiThread(Runnable r) {
        if (Platform.isFxApplicationThread()) {
            r.run();
        } else {
            Platform.runLater(r);
        }
    }
}
