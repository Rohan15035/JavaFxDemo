//package com.example.demo;
//
//import javafx.application.Platform;
//import javafx.fxml.FXML;
//import javafx.scene.control.*;
//import javafx.scene.layout.HBox;
//
//import java.io.*;
//import java.net.*;
//
//public class ChatController {
//    @FXML
//    private ListView<String> messageList;
//    @FXML
//    private TextField messageField;
//    @FXML
//    private Button sendButton;
//    @FXML
//    private Button disconnectButton;
//
//    private PrintWriter out;
//    private Socket socket;
//    private String username;
//
//    public void startChatClient(String username) {
//        this.username = username;
//        try {
//            socket = new Socket("localhost", 12345);
//            out = new PrintWriter(socket.getOutputStream(), true);
//            out.println("JOIN:" + username);
//            new Thread(new IncomingReader(socket)).start();
//            Platform.runLater(() -> messageList.getItems().add("Connected to chat server as " + username));
//            // Set up custom cell factory for message styling
//            messageList.setCellFactory(listView -> new ListCell<>() {
//                @Override
//                protected void updateItem(String item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (empty || item == null) {
//                        setText(null);
//                        setGraphic(null);
//                    } else {
//                        HBox hbox = new HBox();
//                        Label label = new Label(item);
//                        label.setWrapText(true);
//                        label.setMaxWidth(300);
//                        label.setPadding(new javafx.geometry.Insets(5, 10, 5, 10));
//                        hbox.getChildren().add(label);
//                        hbox.setPadding(new javafx.geometry.Insets(2, 5, 2, 5));
//                        hbox.setPrefWidth(listView.getWidth() - 20); // Ensure HBox spans ListView width
//                        if (item.startsWith(username + ":")) {
//                            hbox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
//                            label.setStyle("-fx-background-color: #e0f7fa; -fx-background-radius: 5; -fx-padding: 5px 10px; -fx-text-fill: #000000;");
//                        } else {
//                            hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
//                            label.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5; -fx-padding: 5px 10px; -fx-text-fill: #000000;");
//                        }
//                        setGraphic(hbox);
//                        setText(null);
//                    }
//                }
//            });
//        } catch (IOException e) {
//            Platform.runLater(() -> messageList.getItems().add("Failed to connect to server: " + e.getMessage()));
//            e.printStackTrace();
//        }
//    }
//
//    @FXML
//    private void initialize() {
//        if (sendButton != null) {
//            sendButton.setOnAction(e -> sendMessage());
//        }
//        if (messageField != null) {
//            messageField.setOnAction(e -> sendMessage());
//        }
//        if (disconnectButton != null) {
//            disconnectButton.setOnAction(e -> disconnect());
//        }
//    }
//
//    @FXML
//    private void sendMessage() {
//        if (out == null) {
//            messageList.getItems().add("Not connected to server");
//            return;
//        }
//        String message = messageField.getText();
//        if (!message.isEmpty()) {
//            out.println("MSG:" + username + ": " + message);
//            messageField.clear();
//        }
//    }
//
//    @FXML
//    private void disconnect() {
//        if (socket != null) {
//            try {
//                out.println("LEAVE:" + username);
//                socket.close();
//                out = null;
//                socket = null;
//                Platform.runLater(() -> {
//                    messageList.getItems().add("Disconnected from server");
//                    messageField.setDisable(true);
//                    sendButton.setDisable(true);
//                    disconnectButton.setDisable(true);
//                });
//            } catch (IOException e) {
//                Platform.runLater(() -> messageList.getItems().add("Error disconnecting: " + e.getMessage()));
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private class IncomingReader implements Runnable {
//        private BufferedReader in;
//
//        public IncomingReader(Socket socket) throws IOException {
//            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        }
//
//        @Override
//        public void run() {
//            try {
//                while (true) {
//                    String message = in.readLine();
//                    if (message == null) {
//                        Platform.runLater(() -> messageList.getItems().add("Disconnected from server"));
//                        break;
//                    }
//                    if (!message.startsWith("USERS:")) {
//                        Platform.runLater(() -> messageList.getItems().add(message));
//                    }
//                }
//            } catch (IOException e) {
//                Platform.runLater(() -> messageList.getItems().add("Error receiving message: " + e.getMessage()));
//                e.printStackTrace();
//            } finally {
//                try {
//                    socket.close();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }
//    }
//}

package com.example.demo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.*;
import java.net.*;

public class ChatController {
    @FXML
    private ListView<String> messageList;// ListView is a UI control that displays a scrollable list of items—think of
                                            // it like a vertical (or horizontal) menu where each item is selectable and can be customized visually.
                                            //Displays chat messages, including messages from the server, connection status, and errors.
    @FXML
    private TextField messageField;
    @FXML
    private Button sendButton; //Triggers sending the message in messageField to the server.sendMessage() when clicked.
                                // It’s disabled when the client is not connected to prevent sending messages without a server connection.
    @FXML
    private Button disconnectButton;

    private PrintWriter out;     //Sends messages to the server’s input stream.
    private Socket socket;
    private String username;

    public void startChatClient(String username) {
        this.username = username;
        try {
            socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("JOIN:" + username);

            new Thread(new IncomingReader(socket)).start(); // Thread implementation using runnable

            Platform.runLater(() -> messageList.getItems().add("Connected to chat server as " + username));

            // Setup custom chat bubble formatting
            messageList.setCellFactory(listView -> new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                        return;
                    }

                    HBox hbox = new HBox();
                    Label label = new Label();
                    label.setWrapText(true);
                    label.setMaxWidth(300);
                    label.setPadding(new javafx.geometry.Insets(5, 10, 5, 10));
                    hbox.setPadding(new javafx.geometry.Insets(2, 5, 2, 5));
                    hbox.setPrefWidth(listView.getWidth() - 20);

                    // Message parsing
                    String msgText = item;
                    String sender = "";

                    if (item.startsWith("MSG:")) {
                        String[] parts = item.substring(4).split(":", 2);
                        if (parts.length == 2) {
                            sender = parts[0];
                            msgText = parts[1];
                        }
                    }

                    label.setText(msgText.trim());

                    if (sender.equals(username)) {
                        hbox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
                        label.setStyle("-fx-background-color: #d1ffd6; -fx-background-radius: 10; -fx-text-fill: black;");
                    } else {
                        hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                        label.setStyle("-fx-background-color: #eeeeee; -fx-background-radius: 10; -fx-text-fill: black;");
                    }

                    hbox.getChildren().add(label);
                    setGraphic(hbox);
                    setText(null);
                }
            });

        } catch (IOException e) {
            Platform.runLater(() -> messageList.getItems().add("Failed to connect to server: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        if (sendButton != null) {
            sendButton.setOnAction(e -> sendMessage());
        }
        if (messageField != null) {
            messageField.setOnAction(e -> sendMessage());
        }
        if (disconnectButton != null) {
            disconnectButton.setOnAction(e -> disconnect());
        }
    }

    @FXML
    private void sendMessage() {
        if (out == null) {
            messageList.getItems().add("Not connected to server");
            return;
        }

        String message = messageField.getText();
        if (!message.isEmpty()) {
            out.println("MSG:" + username + ":" + message);
            messageField.clear();
        }
    }

    @FXML
    private void disconnect() {
        if (socket != null) {
            try {
                out.println("LEAVE:" + username);
                socket.close();
                out = null;
                socket = null;
                Platform.runLater(() -> {
                    messageList.getItems().add("Disconnected from server");
                    messageField.setDisable(true);
                    sendButton.setDisable(true);
                    disconnectButton.setDisable(true);
                });
            } catch (IOException e) {
                Platform.runLater(() -> messageList.getItems().add("Error disconnecting: " + e.getMessage()));
                e.printStackTrace();
            }
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
                        Platform.runLater(() -> messageList.getItems().add("Disconnected from server"));
                        break;
                    }

                    if (!message.startsWith("USERS:")) {
                        Platform.runLater(() -> messageList.getItems().add(message));
                    }
                }
            } catch (IOException e) {
                Platform.runLater(() -> messageList.getItems().add("Error receiving message: " + e.getMessage()));
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



/*
Platform refers to the javafx.application.Platform class. It’s a utility class that helps you safely interact with the JavaFX Application Thread
, which is the only thread allowed to update the UI.



In JavaFX, Platform.runLater() is a method that lets you safely update the UI from a background thread. Since JavaFX is single-threaded
when it comes to UI operations, any changes to UI components (like ListView, Label, Button, etc.) must happen on the JavaFX Application Thread.
If you're doing something in a background thread—like reading from a socket or performing a long computation—you can’t directly update the UI from that thread.
 Doing so will throw an IllegalStateException.
That’s where Platform.runLater() comes in. It schedules your UI update code to run on the JavaFX Application Thread.




 */