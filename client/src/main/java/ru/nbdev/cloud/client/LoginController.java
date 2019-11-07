package ru.nbdev.cloud.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.nbdev.cloud.client.common.Helpers;
import ru.nbdev.cloud.common.messages.AbstractMessage;
import ru.nbdev.cloud.common.messages.AuthErrorMessage;
import ru.nbdev.cloud.common.messages.AuthOkMessage;
import ru.nbdev.cloud.common.messages.ClientAuthMessage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private Button btnLogin;
    @FXML private Button btnSignUp;
    @FXML private TextField tfLogin;
    @FXML private TextField tfPass;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnLogin.setOnAction(event -> login());
        btnSignUp.setOnAction(event -> registration(event));
    }

    private void login() {
        Network.start();
        String login = tfLogin.getText();
        String pass = tfPass.getText();
        if (login.length() == 0) {
            Helpers.showMessage("Имя пользователя не может быть пустым!");
            return;
        }

        try {
            Network.sendMsg(new ClientAuthMessage(login, pass));
            AbstractMessage message = Network.readObject();
            if (message instanceof AuthOkMessage) {
                SceneController.getInstance().switchScene("/fxml/main.fxml");
            } else if (message instanceof AuthErrorMessage) {
                Helpers.showMessage("Ошибка авторизации! Неверное имя пользователся или пароль.");
            } else {
                Helpers.showMessage("Ошибка! Сервер вернул неверные данные.");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registration(ActionEvent event) {
        //SceneController.getInstance().switchScene("/registration.fxml");

        // New window (Stage)
        try {
            Stage newWindow = new Stage();
            newWindow.setTitle("Second Stage");
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/registration.fxml"));
            newWindow.setScene(new Scene(root));

            // Specifies the modality for new window.
            newWindow.initModality(Modality.WINDOW_MODAL);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            // Specifies the owner Window (parent) for new window
            newWindow.initOwner(window);

            newWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
