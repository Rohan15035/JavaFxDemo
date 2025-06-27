
package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Use ClassLoader to avoid null resource issues
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/frontpage.fxml"));

        Parent root = loader.load();

        primaryStage.setTitle("SwiftChat Messenger");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
