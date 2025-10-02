package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        Label lbl = new Label("Hello JavaFX!");
        stage.setScene(new Scene(new StackPane(lbl), 1000, 500));
        stage.setTitle("Bachelor Program: Activity Selector");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
