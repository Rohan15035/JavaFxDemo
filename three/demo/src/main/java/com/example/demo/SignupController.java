package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class SignupController {
    @FXML
    private TextField txtNewUser;

    @FXML
    private PasswordField txtNewPass;

    @FXML
    private Button btnRegister;

    @FXML
    private Label lblSignupStatus;

    @FXML
    private void initialize() {
        btnRegister.setOnAction(e -> {
            String user = txtNewUser.getText();
            String pass = txtNewPass.getText();
            if (!user.isEmpty() && !pass.isEmpty()) {
                lblSignupStatus.setText("User registered (mock)");
                // TODO: Save credentials or send to server
            } else {
                lblSignupStatus.setText("Please fill all fields.");
            }
        });
    }
}
