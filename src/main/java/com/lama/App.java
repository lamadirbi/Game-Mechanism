package com.lama;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Random;

public class App extends Application {

    private int targetNumber;
    private int guessCount;

    private TextField guessInput;
    private Label feedbackLabel;
    private Label guessCountLabel;
    private Button guessButton;
    private Button restartButton;

    @Override
    public void start(Stage stage) {
        stage.setTitle("لعبة تخمين الرقم");

        targetNumber = generateRandomNumber();
        guessCount = 0;

        guessInput = new TextField();
        guessInput.setPromptText("أدخل تخمينك بين 1 و 100");

        guessButton = new Button("خمن");
        guessButton.setOnAction(e -> processGuess());

        restartButton = new Button("إعادة اللعبة");
        restartButton.setOnAction(e -> restartGame());
        restartButton.setDisable(true);

        feedbackLabel = new Label("أدخل رقمًا وابدأ بالتخمين!");
        guessCountLabel = new Label("عدد المحاولات: 0");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(guessInput, guessButton, restartButton, feedbackLabel, guessCountLabel);

        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    private int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(100) + 1; // بين 1 و 100
    }

    private void processGuess() {
        String input = guessInput.getText().trim();
        if (input.isEmpty()) {
            feedbackLabel.setText("يرجى إدخال رقم.");
            return;
        }

        int guess;
        try {
            guess = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            feedbackLabel.setText("ادخل رقما صحيحا بين 1 و 100.");
            return;
        }

        if (guess < 1 || guess > 100) {
            feedbackLabel.setText("يجب أن يكون الرقم بين 1 و 100.");
            return;
        }

        guessCount++;
        guessCountLabel.setText("عدد المحاولات: " + guessCount);

        if (guess == targetNumber) {
            feedbackLabel.setText("مبروك! لقد تخمنت الرقم الصحيح!");
            guessInput.setDisable(true);
            guessButton.setDisable(true);
            restartButton.setDisable(false);
        } else if (guess < targetNumber) {
            feedbackLabel.setText("الرقم أكبر من " + guess + ". حاول مرة أخرى.");
        } else {
            feedbackLabel.setText("الرقم أصغر من " + guess + ". حاول مرة أخرى.");
        }

        guessInput.clear();
    }

    private void restartGame() {
        targetNumber = generateRandomNumber();
        guessCount = 0;
        guessCountLabel.setText("عدد المحاولات: 0");
        feedbackLabel.setText("أدخل رقمًا وابدأ بالتخمين!");
        guessInput.setDisable(false);
        guessButton.setDisable(false);
        restartButton.setDisable(true);
        guessInput.clear();
    }

    public static void main(String[] args) {
        launch();
    }
}
