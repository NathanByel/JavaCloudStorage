package ru.nbdev.cloud.client;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
        System.out.println();
    }

    public void onLoginClick(ActionEvent event) {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
            window.setScene(new Scene(root));
            window.show();
            window.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onRegistrationClick(ActionEvent event) {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/registration.fxml"));
            window.setScene(new Scene(root));
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
