package com.lama;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application {

    private ObservableList<String> taskList1;
    private ObservableList<String> taskList2;

    private ListView<String> listView1;
    private ListView<String> listView2;

    private TextField taskInput;

    @Override
    public void start(Stage stage) {
        stage.setTitle("نظام قائمة مهام موزعة بسيط (محاكاة)");

        taskList1 = FXCollections.observableArrayList();
        taskList2 = FXCollections.observableArrayList();

        listView1 = new ListView<>(taskList1);
        listView2 = new ListView<>(taskList2);

        taskInput = new TextField();
        taskInput.setPromptText("أدخل مهمة جديدة");

        Button addButton = new Button("إضافة");
        addButton.setOnAction(e -> addTask());

        Button deleteButton1 = new Button("حذف من القائمة 1");
        deleteButton1.setOnAction(e -> deleteTask(listView1, taskList1));

        Button deleteButton2 = new Button("حذف من القائمة 2");
        deleteButton2.setOnAction(e -> deleteTask(listView2, taskList2));

        Button syncButton = new Button("مزامنة (محاكاة التوزيع)");
        syncButton.setOnAction(e -> syncTasks());

        VBox leftBox = new VBox(10, new Label("قائمة المهام 1"), listView1, deleteButton1);
        VBox rightBox = new VBox(10, new Label("قائمة المهام 2"), listView2, deleteButton2);

        HBox listsBox = new HBox(20, leftBox, rightBox);
        VBox.setVgrow(listView1, Priority.ALWAYS);
        VBox.setVgrow(listView2, Priority.ALWAYS);

        HBox inputBox = new HBox(10, taskInput, addButton, syncButton);
        inputBox.setPadding(new Insets(10));

        VBox root = new VBox(10, listsBox, inputBox);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void addTask() {
        String task = taskInput.getText().trim();
        if (!task.isEmpty()) {
            taskList1.add(task);
            taskInput.clear();
        }
    }

    private void deleteTask(ListView<String> listView, ObservableList<String> taskList) {
        String selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            taskList.remove(selected);
        }
    }

    private void syncTasks() {
        // المحاكاة: مزامنة قائمة المهام 1 إلى قائمة المهام 2
        taskList2.setAll(taskList1);
    }

    public static void main(String[] args) {
        launch();
    }
}
