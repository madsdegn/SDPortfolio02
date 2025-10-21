package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    // Declare ComboBoxes as class members
    private ComboBox<String> basicComboBox;
    private ComboBox<String> subjectComboBox1;
    private ComboBox<String> subjectComboBox2;
    private ComboBox<String> basicCourseComboBox;
    private ComboBox<String> subjectCourseComboBox1;
    private ComboBox<String> subjectCourseComboBox2;

    @Override
    public void start(Stage stage) {

        // Create label and field for user name input
        Label nameLabel = new Label("Enter Your Name");
        TextField nameField = new TextField();
        nameField.setPromptText("Your Name");
        nameField.setPrefWidth(200);

        // Create labels for ComboBoxes
        Label basicLabel = new Label("Select Basic Program");
        Label subjectLabel1 = new Label("Select Subject Module 1");
        Label subjectLabel2 = new Label("Select Subject Module 2");
        Label basicCourseLabel = new Label("Select Basic Courses");
        Label subjectCourseLabel1 = new Label("Select Subject Module Courses 1");
        Label subjectCourseLabel2 = new Label("Select Subject Module Courses 2");

        // Initialize ComboBoxes
        // Basic Program ComboBox
        basicComboBox = new ComboBox<>();
        basicComboBox.getItems().addAll(
                "Software Development",
                "Web Development",
                "IT Security"
        );
        basicComboBox.setValue("Basic Program");
        basicComboBox.setPrefWidth(200);

        // Subject Module ComboBox 1
        subjectComboBox1 = new ComboBox<>();
        subjectComboBox1.getItems().addAll(
                "1st Semester",
                "2nd Semester",
                "3rd Semester",
                "4th Semester"
        );
        subjectComboBox1.setValue("Subject Module");
        subjectComboBox1.setPrefWidth(200);

        // Subject Module ComboBox 2
        subjectComboBox2 = new ComboBox<>();
        subjectComboBox2.getItems().addAll(
                "Lectures",
                "Exercises",
                "Project Work",
                "Exams"
        );
        subjectComboBox2.setValue("Subject Module");
        subjectComboBox2.setPrefWidth(200);

        // Basic Course ComboBox
        basicCourseComboBox = new ComboBox<>();
        basicCourseComboBox.getItems().addAll(
                "Lectures",
                "Exercises",
                "Project Work",
                "Exams"
        );
        basicCourseComboBox.setValue("Basic Courses");
        basicCourseComboBox.setPrefWidth(200);

        // Subject Module Course ComboBox 1
        subjectCourseComboBox1 = new ComboBox<>();
        subjectCourseComboBox1.getItems().addAll(
                "Lectures",
                "Exercises",
                "Project Work",
                "Exams"
        );
        subjectCourseComboBox1.setValue("Basic Courses");
        subjectCourseComboBox1.setPrefWidth(200);

        // Subject Module Course ComboBox 2
        subjectCourseComboBox2 = new ComboBox<>();
        subjectCourseComboBox2.getItems().addAll(
                "Lectures",
                "Exercises",
                "Project Work",
                "Exams"
        );
        subjectCourseComboBox2.setValue("Basic Courses");
        subjectCourseComboBox2.setPrefWidth(200);

        // Create HBoxes for layout
        HBox topContainer = new HBox(20);
        HBox bottomContainer = new HBox(20);

        // Create individual VBoxes for topContainer and each input with its label
        VBox nameBox = new VBox(5);
        nameBox.getChildren().addAll(nameLabel, nameField);

        VBox basicBox = new VBox(5);
        basicBox.getChildren().addAll(basicLabel, basicComboBox);

        VBox subjectBox1 = new VBox(5);
        subjectBox1.getChildren().addAll(subjectLabel1, subjectComboBox1);

        VBox subjectBox2 = new VBox(5);
        subjectBox2.getChildren().addAll(subjectLabel2, subjectComboBox2);

        // Add all VBoxes to the topContainer HBox
        topContainer.getChildren().addAll(nameBox, basicBox, subjectBox1, subjectBox2);

        // Create a separator between top and bottom containers
        Separator topSeparator = new Separator();
        topSeparator.setPrefWidth(800);

        // Create individual VBoxes for bottomContainer and each input with its label
        VBox basicCourseBox = new VBox(5);
        basicCourseBox.getChildren().addAll(basicCourseLabel, basicCourseComboBox);

        VBox subjectCourseBox1 = new VBox(5);
        subjectCourseBox1.getChildren().addAll(subjectCourseLabel1, subjectCourseComboBox1);

        VBox subjectCourseBox2 = new VBox(5);
        subjectCourseBox2.getChildren().addAll(subjectCourseLabel2, subjectCourseComboBox2);

        // Add a read-only TextArea to show selected courses
        TextArea selectedArea = new TextArea();
        selectedArea.setEditable(false);
        selectedArea.setPromptText("Selected courses...");
        selectedArea.setPrefWidth(200);
        selectedArea.setPrefHeight(200);
        selectedArea.setWrapText(true);

        // Add all VBoxes and the textarea to the bottomContainer HBox
        bottomContainer.getChildren().addAll(basicCourseBox, subjectCourseBox1, subjectCourseBox2, selectedArea);

        // Main layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(topContainer, topSeparator, bottomContainer);

        Scene scene = new Scene(layout, 900, 300);
        stage.setScene(scene);
        stage.setTitle("Bachelor Program: Activity Selector");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
