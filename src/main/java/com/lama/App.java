package com.lama;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;
import java.net.*;

public class App extends Application {

    private TextField inputField;
    private Label responseLabel;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Distributed System Client");

        inputField = new TextField();
        inputField.setPromptText("Enter message to send to server");

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> sendMessage());

        responseLabel = new Label("Response will appear here");

        VBox layout = new VBox(10);
        layout.getChildren().addAll(inputField, sendButton, responseLabel);
        layout.setPadding(new javafx.geometry.Insets(20));

        Scene scene = new Scene(layout, 400, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (message.isEmpty()) {
            responseLabel.setText("Please enter a message");
            return;
        }

        try (Socket socket = new Socket("localhost", 1234);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(message);
            String response = in.readLine();
            responseLabel.setText("Server response: " + response);

        } catch (IOException ex) {
            responseLabel.setText("Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
