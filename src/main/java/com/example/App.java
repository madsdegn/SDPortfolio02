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
        stage.setScene(new Scene(new StackPane(lbl), 400, 250));
        stage.setTitle("My JavaFX App");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
