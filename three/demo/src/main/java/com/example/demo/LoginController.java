//package com.example.demo;
//
//
//
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//import javafx.scene.control.Label;
//import javafx.stage.Stage;
//
//public class LoginController {
//    @FXML
//    private TextField txtUsername;
//
//    @FXML
//    private PasswordField txtPassword;
//
//    @FXML
//    private Button btnLogin;
//
//    @FXML
//    private Label lblLoginStatus;
//
//    @FXML
//    private void initialize() {
//        btnLogin.setOnAction(e -> {
//            String user = txtUsername.getText();
//            String pass = txtPassword.getText();
//            if (user.equals("user1") && pass.equals("pass123")) {
//                lblLoginStatus.setText("Login successful!");
//                // TODO: Load chat screen or start ChatClient
//            } else {
//                lblLoginStatus.setText("Invalid credentials.");
//            }
//        });
//    }
//}
package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private Button btnLogin;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private Label lblLoginStatus;

    @FXML
    private void initialize() {
        btnLogin.setOnAction(e -> {
            String user = txtUsername.getText();
            String pass = txtPassword.getText();
            if (user.equals("user1") && pass.equals("pass123")) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/chat.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) btnLogin.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Chat");
                    ChatController controller = loader.getController();
                    controller.startChatClient();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                lblLoginStatus.setText("Invalid credentials.");
            }
        });
    }
}