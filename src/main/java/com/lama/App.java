package com.lama;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class App extends Application {

    private TextField expressionField;
    private Button sendButton;
    private Label resultLabel;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Distributed Calculator Client");

        expressionField = new TextField();
        expressionField.setPromptText("Enter mathematical expression (e.g., 2+3*4)");

        sendButton = new Button("Calculate");
        sendButton.setOnAction(e -> sendExpression());

        resultLabel = new Label("Result will appear here");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(expressionField, sendButton, resultLabel);

        Scene scene = new Scene(layout, 400, 200);
        stage.setScene(scene);
        stage.show();
    }

        private void sendExpression() {
        String expression = expressionField.getText().trim();
        if (expression.isEmpty()) {
            resultLabel.setText("Please enter a mathematical expression");
            return;
        }

        // Validate expression for invalid characters like '='
        if (expression.contains("=")) {
            resultLabel.setText("خطأ: التعبير الرياضي لا يجب أن يحتوي على '='");
            return;
        }

        expressionField.setDisable(true);
        sendButton.setDisable(true);
        resultLabel.setText("Calculating...");

        new Thread(() -> {
            try (Socket socket = new Socket("localhost", 2345);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                out.println(expression);
                String response = in.readLine();

                Platform.runLater(() -> {
                    resultLabel.setText("Result: " + response);
                    expressionField.setDisable(false);
                    sendButton.setDisable(false);
                });

            } catch (ConnectException ce) {
                Platform.runLater(() -> {
                    resultLabel.setText("خطأ: لا يمكن الاتصال بالخادم. يرجى التأكد من أن الخادم يعمل.");
                    expressionField.setDisable(false);
                    sendButton.setDisable(false);
                });
            } catch (IOException ex) {
                Platform.runLater(() -> {
                    resultLabel.setText("خطأ: " + ex.getMessage());
                    expressionField.setDisable(false);
                    sendButton.setDisable(false);
                });
            }
        }).start();
    }

    public static void main(String[] args) {
        launch();
    }
}
