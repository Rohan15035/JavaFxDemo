

package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        btnRegister.setOnAction(e -> registerUser());
    }

    private void registerUser() {
        String username = txtNewUser.getText();
        String password = txtNewPass.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lblSignupStatus.setText("Please fill all fields.");
            return;
        }

        try {
            // Ensure users.txt exists
            File file = new File("users.txt");
            if (!file.exists()) {
                file.createNewFile(); // Create file if it doesn't exist
            }

            // Check if user already exists
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.split(":")[0].equals(username)) {
                    lblSignupStatus.setText("Username already exists.");
                    reader.close();
                    return;
                }
            }
            reader.close();

            // Hash password
            String hashedPassword = hashPassword(password);

            // Save to users.txt
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(username + ":" + hashedPassword);
            writer.newLine();
            writer.close();

            lblSignupStatus.setText("User registered successfully!");
            txtNewUser.clear();
            txtNewPass.clear();
        } catch (IOException e) {
            lblSignupStatus.setText("Error saving user: " + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            lblSignupStatus.setText("Error hashing password.");
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