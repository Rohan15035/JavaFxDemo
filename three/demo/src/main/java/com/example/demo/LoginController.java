


package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginController {
    @FXML
    private Button btnLogin;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblLoginStatus;

    @FXML
    private void initialize() {
        btnLogin.setOnAction(e -> login());
    }

    private void login() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lblLoginStatus.setText("Please fill all fields.");
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
            String line;
            String hashedPassword = hashPassword(password);
            boolean validCredentials = false;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(hashedPassword)) {
                    validCredentials = true;
                    break;
                }
            }
            reader.close();

            if (validCredentials) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/chat.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) btnLogin.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Chat - " + username);
                    ChatController controller = loader.getController();
                    controller.startChatClient(username);
                } catch (Exception ex) {
                    lblLoginStatus.setText("Failed to load chat: " + ex.getMessage());
                    ex.printStackTrace();
                }
            } else {
                lblLoginStatus.setText("Invalid credentials.");
            }
        } catch (IOException e) {
            lblLoginStatus.setText("Error reading users: " + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            lblLoginStatus.setText("Error hashing password.");
            e.printStackTrace();
        }
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}