package com.example;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Assignment1App extends Application {

    // UI controls
    private ComboBox<String> basicComboBox;
    private ComboBox<String> subjectComboBox1;
    private ComboBox<String> subjectComboBox2;
    private ComboBox<String> basicCourseComboBox;
    private ComboBox<String> subjectCourseComboBox1;
    private ComboBox<String> subjectCourseComboBox2;
    private TextField nameField;
    private TextArea selectedArea;

    // Store selections by student name
    private Map<String, Assignment1StudentSelection> selections = new HashMap<>();

    @Override
    public void start(Stage stage) {
        // Name input
        Label nameLabel = new Label("Enter Your Name");
        nameField = new TextField();
        nameField.setPromptText("Your Name");
        nameField.setPrefWidth(200);

        // Labels
        Label basicLabel = new Label("Select Basic Program");
        Label subjectLabel1 = new Label("Select Subject Module 1");
        Label subjectLabel2 = new Label("Select Subject Module 2");
        Label basicCourseLabel = new Label("Select Basic Courses");
        Label subjectCourseLabel1 = new Label("Select Subject Module Courses 1");
        Label subjectCourseLabel2 = new Label("Select Subject Module Courses 2");

        // ComboBoxes
        basicComboBox = new ComboBox<>();
        subjectComboBox1 = new ComboBox<>();
        subjectComboBox2 = new ComboBox<>();
        basicCourseComboBox = new ComboBox<>();
        subjectCourseComboBox1 = new ComboBox<>();
        subjectCourseComboBox2 = new ComboBox<>();

        for (ComboBox<String> cb : new ComboBox[]{basicComboBox, subjectComboBox1, subjectComboBox2,
            basicCourseComboBox, subjectCourseComboBox1, subjectCourseComboBox2}) {
            cb.setPrefWidth(200);
        }

        // TextArea
        selectedArea = new TextArea();
        selectedArea.setEditable(false);
        selectedArea.setPromptText("Selected activities...");
        selectedArea.setPrefWidth(300);
        selectedArea.setPrefHeight(250);
        selectedArea.setWrapText(true);

        // Add Activity buttons
        Button addBasicActivityButton = new Button("Add Activity");
        addBasicActivityButton.setOnAction(e -> addActivity(basicCourseComboBox));

        Button addSubject1ActivityButton = new Button("Add Activity");
        addSubject1ActivityButton.setOnAction(e -> addActivity(subjectCourseComboBox1));

        Button addSubject2ActivityButton = new Button("Add Activity");
        addSubject2ActivityButton.setOnAction(e -> addActivity(subjectCourseComboBox2));

        // Layout
        HBox topContainer = new HBox(20,
                new VBox(5, nameLabel, nameField),
                new VBox(5, basicLabel, basicComboBox),
                new VBox(5, subjectLabel1, subjectComboBox1),
                new VBox(5, subjectLabel2, subjectComboBox2)
        );

        VBox basicCourseBox = new VBox(5, basicCourseLabel, basicCourseComboBox, addBasicActivityButton);
        VBox subjectCourseBox1 = new VBox(5, subjectCourseLabel1, subjectCourseComboBox1, addSubject1ActivityButton);
        VBox subjectCourseBox2 = new VBox(5, subjectCourseLabel2, subjectCourseComboBox2, addSubject2ActivityButton);

        VBox textAreaBox = new VBox(10, selectedArea);

        HBox bottomContainer = new HBox(20, basicCourseBox, subjectCourseBox1, subjectCourseBox2, textAreaBox);

        VBox layout = new VBox(15, topContainer, new Separator(), bottomContainer);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 1000, 400);
        stage.setScene(scene);
        stage.setTitle("Bachelor Program: Activity Selector");
        stage.show();

        // Load initial data
        loadPrograms();

        // Hook up events
        basicComboBox.setOnAction(e -> {
            loadSubjectModules();
            loadBasicCourses(basicCourseComboBox);
        });

        subjectComboBox1.setOnAction(e -> {
            String selected = subjectComboBox1.getValue();
            if (selected != null) {
                loadSubjectCourses(selected, subjectCourseComboBox1);
            }
        });

        subjectComboBox2.setOnAction(e -> {
            String selected = subjectComboBox2.getValue();
            if (selected != null) {
                loadSubjectCourses(selected, subjectCourseComboBox2);
            }
        });
    }

    // --- Loaders (hardcoded for now) ---
    private void loadPrograms() {
        basicComboBox.getItems().setAll("HumTek", "NatBach");
    }

    private void loadSubjectModules() {
        subjectComboBox1.getItems().setAll("Computer Science", "Informatics");
        subjectComboBox2.getItems().setAll("Computer Science", "Informatics");
    }

    private void loadSubjectCourses(String subject, ComboBox<String> comboBox) {
        comboBox.getItems().clear();

        if ("Computer Science".equals(subject)) {
            comboBox.getItems().setAll(
                    "Subject Module Project, Computer Science",
                    "Essential Computing",
                    "Software Development",
                    "Interactive Digital Systems"
            );
        } else if ("Informatics".equals(subject)) {
            comboBox.getItems().setAll(
                    "Subject Module Project, Informatics",
                    "OFIT",
                    "BANDIT",
                    "WITS"
            );
        }
    }

    private void loadBasicCourses(ComboBox<String> comboBox) {
        comboBox.getItems().setAll(
                "Basic Project 1",
                "Basic Project 2",
                "Basic Project 3",
                "DK",
                "STS",
                "TSA",
                "Science Theory"
        );
    }

    // --- Add activity ---
    private void addActivity(ComboBox<String> comboBox) {
        String studentName = nameField.getText();
        String program = basicComboBox.getValue();
        String subject1 = subjectComboBox1.getValue();
        String subject2 = subjectComboBox2.getValue();
        String activity = comboBox.getValue();

        if (studentName == null || studentName.isBlank() || activity == null) {
            selectedArea.appendText("⚠ Please enter your name and select an activity first.\n");
            return;
        }

        // Get or create the student's selection model
        Assignment1StudentSelection selection = selections.get(studentName);
        if (selection == null) {
            selection = new Assignment1StudentSelection(studentName, program, subject1, subject2);
            selections.put(studentName, selection);
        }

        // Add the activity to the model
        selection.addActivity(activity);

        // Refresh the text area to show all activities for this student
        selectedArea.setText(selection.toString());
    }

    public static void main(String[] args) {
        launch();
    }
}
