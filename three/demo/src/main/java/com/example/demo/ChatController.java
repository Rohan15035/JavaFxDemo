package com.example.demo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.*;

public class ChatController {
    @FXML
    private TextArea messageArea;
    @FXML
    private TextField messageField;

    private PrintWriter out;
    private Socket socket; // Added to allow closing the socket

    public void startChatClient() {
        try {
            socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            new Thread(new IncomingReader(socket)).start();
            Platform.runLater(() -> messageArea.appendText("Connected to chat server\n"));
        } catch (IOException e) {
            Platform.runLater(() -> messageArea.appendText("Failed to connect to server: " + e.getMessage() + "\n"));
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        messageField.setOnAction(e -> sendMessage());
    }

    @FXML
    private void sendMessage() {
        if (out == null) {
            messageArea.appendText("Not connected to server\n");
            return;
        }
        String message = messageField.getText();
        if (!message.isEmpty()) {
            out.println(message);
            messageField.clear();
        }
    }

    private class IncomingReader implements Runnable {
        private BufferedReader in;

        public IncomingReader(Socket socket) throws IOException {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String message = in.readLine();
                    if (message == null) {
                        Platform.runLater(() -> messageArea.appendText("Disconnected from server\n"));
                        break;
                    }
                    Platform.runLater(() -> messageArea.appendText(message + "\n"));
                }
            } catch (IOException e) {
                Platform.runLater(() -> messageArea.appendText("Error receiving message: " + e.getMessage() + "\n"));
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}