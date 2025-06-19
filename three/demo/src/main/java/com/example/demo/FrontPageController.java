package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
public class FrontPageController {
    @FXML
    private Button btnLogin;

    @FXML
    private Button btnSignup;

    @FXML
    private Label lblStatus;

    @FXML
    private void initialize() {
        btnLogin.setOnAction(e -> loadScene("login.fxml"));
        btnSignup.setOnAction(e -> loadScene("signup.fxml"));
    }



    private void loadScene(String fxmlFile) {
        String path = "/com/example/demo/" + fxmlFile;
        System.out.println("Loading FXML: " + path); // For understanding how files are being uploaded.
        URL resource = getClass().getResource(path);
        System.out.println("Resolved URL: " + resource);

        if (resource == null) {
            lblStatus.setText("FXML not found: " + fxmlFile);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(resource); //  this line fixes my issue
            AnchorPane pane = loader.load();              //  now it's non-static load()
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(pane));
        } catch (IOException e) {
            lblStatus.setText("Failed to load: " + fxmlFile);
            e.printStackTrace();
        }
    }



}