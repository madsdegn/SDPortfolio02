// Assignment 1 - Bachelor Program Activity Selector
// Mads Degn, Daniel Holst Pedersen
// 28/10/25
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
    // ComboBoxes used for dropdown selections
    private ComboBox<String> basicComboBox;          // Select basic program
    private ComboBox<String> subjectComboBox1;       // Select subject module 1
    private ComboBox<String> subjectComboBox2;       // Select subject module 2
    private ComboBox<String> basicCourseComboBox;    // Select courses for basic program
    private ComboBox<String> subjectCourseComboBox1; // Select courses for subject module 1
    private ComboBox<String> subjectCourseComboBox2; // Select courses for subject module 2

    // Text input for student name
    private TextField nameField;

    // TextArea to display all added activities for the current student
    private TextArea selectedArea;

    // Data model
    // Store all student selections in memory.
    // Key = student name
    // Value = Assignment1StudentSelection object
    private Map<String, Assignment1StudentSelection> selections = new HashMap<>();

    @Override
    public void start(Stage stage) {

        // Name input section
        Label nameLabel = new Label("Enter Your Name");
        nameField = new TextField();
        nameField.setPromptText("Your Name"); // Placeholder text
        nameField.setPrefWidth(200);

        // Labels for dropdowns
        Label basicLabel = new Label("Select Basic Program");
        Label subjectLabel1 = new Label("Select Subject Module 1");
        Label subjectLabel2 = new Label("Select Subject Module 2");
        Label basicCourseLabel = new Label("Select Basic Courses");
        Label subjectCourseLabel1 = new Label("Select Subject Module Courses 1");
        Label subjectCourseLabel2 = new Label("Select Subject Module Courses 2");

        // Initialize ComboBoxes
        basicComboBox = new ComboBox<>();
        subjectComboBox1 = new ComboBox<>();
        subjectComboBox2 = new ComboBox<>();
        basicCourseComboBox = new ComboBox<>();
        subjectCourseComboBox1 = new ComboBox<>();
        subjectCourseComboBox2 = new ComboBox<>();

        // Set a consistent width for all ComboBoxes
        for (ComboBox<String> cb : new ComboBox[]{basicComboBox, subjectComboBox1, subjectComboBox2,
            basicCourseComboBox, subjectCourseComboBox1, subjectCourseComboBox2}) {
            cb.setPrefWidth(200);
        }

        // TextArea for displaying results
        selectedArea = new TextArea();
        selectedArea.setEditable(false); // user cannot type here
        selectedArea.setPromptText("Selected activities...");
        selectedArea.setPrefWidth(200);
        selectedArea.setPrefHeight(250);
        selectedArea.setWrapText(true); // wrap long lines

        // Add Activity buttons
        // Each button is tied to a specific course ComboBox
        Button addBasicActivityButton = new Button("Add Activity");
        addBasicActivityButton.setOnAction(e -> addActivity(basicCourseComboBox));
        addBasicActivityButton.setPrefWidth(200); // Set width to 200 to match combo boxes

        Button addSubject1ActivityButton = new Button("Add Activity");
        addSubject1ActivityButton.setOnAction(e -> addActivity(subjectCourseComboBox1));
        addSubject1ActivityButton.setPrefWidth(200); // Set width to 200 to match combo boxes

        Button addSubject2ActivityButton = new Button("Add Activity");
        addSubject2ActivityButton.setOnAction(e -> addActivity(subjectCourseComboBox2));
        addSubject2ActivityButton.setPrefWidth(200); // Set width to 200 to match combo boxes

        // Layout containers
        // Top row is name, program and subject module selectors
        HBox topContainer = new HBox(20,
                new VBox(5, nameLabel, nameField),
                new VBox(5, basicLabel, basicComboBox),
                new VBox(5, subjectLabel1, subjectComboBox1),
                new VBox(5, subjectLabel2, subjectComboBox2)
        );

        // Bottom row is course selectors, add buttons and textarea
        VBox basicCourseBox = new VBox(5, basicCourseLabel, basicCourseComboBox, addBasicActivityButton);
        VBox subjectCourseBox1 = new VBox(5, subjectCourseLabel1, subjectCourseComboBox1, addSubject1ActivityButton);
        VBox subjectCourseBox2 = new VBox(5, subjectCourseLabel2, subjectCourseComboBox2, addSubject2ActivityButton);
        VBox textAreaBox = new VBox(10, selectedArea);

        HBox bottomContainer = new HBox(20, basicCourseBox, subjectCourseBox1, subjectCourseBox2, textAreaBox);

        // Main layout
        // Vertical stacking of top and bottom sections
        VBox layout = new VBox(15, topContainer, new Separator(), bottomContainer);
        layout.setPadding(new Insets(20));

        // Scene and Stage setup
        Scene scene = new Scene(layout, 900, 400);
        stage.setScene(scene);
        stage.setTitle("Bachelor Program: Activity Selector");
        stage.show();

        // Initialize data
        loadPrograms(); // load basic programs into the first ComboBox

        // Event handlers
        // When a basic program is chosen, load subject modules and basic courses
        basicComboBox.setOnAction(e -> {
            loadSubjectModules();
            loadBasicCourses(basicCourseComboBox);
        });

        // When subject module 1 is chosen, load its courses
        subjectComboBox1.setOnAction(e -> {
            String selected = subjectComboBox1.getValue();
            if (selected != null) {
                loadSubjectCourses(selected, subjectCourseComboBox1);
            }
        });

        // When subject module 2 is chosen, load its courses
        subjectComboBox2.setOnAction(e -> {
            String selected = subjectComboBox2.getValue();
            if (selected != null) {
                loadSubjectCourses(selected, subjectCourseComboBox2);
            }
        });
    }

    // Data loading methods
    // Load basic programs
    private void loadPrograms() {
        basicComboBox.getItems().setAll("HumTek", "NatBach");
    }

    // Load subject modules
    private void loadSubjectModules() {
        subjectComboBox1.getItems().setAll("Computer Science", "Informatics");
        subjectComboBox2.getItems().setAll("Computer Science", "Informatics");
    }

    // Load courses depending on which subject module was chosen
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

    // Load basic courses
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

    // Add activity method
    // Called when the user clicks one of the "Add Activity" buttons
    private void addActivity(ComboBox<String> comboBox) {
        String studentName = nameField.getText();
        String program = basicComboBox.getValue();
        String subject1 = subjectComboBox1.getValue();
        String subject2 = subjectComboBox2.getValue();
        String activity = comboBox.getValue();

        // Input must have a name and an activity selected
        if (studentName == null || studentName.isBlank() || activity == null) {
            selectedArea.appendText("Please enter your name and select an activity first.\n");
            return;
        }

        // Retrieve or create the student selection model
        Assignment1StudentSelection selection = selections.get(studentName);
        if (selection == null) {

            // Create a new model object for this student
            selection = new Assignment1StudentSelection(studentName, program, subject1, subject2);
            selections.put(studentName, selection);
        }

        // Add the chosen activity to the student list
        selection.addActivity(activity);

        // Refresh the text area to show all activities for this student
        selectedArea.setText(selection.toString());
    }

    public static void main(String[] args) {
        launch(); // Launches the JavaFX application
    }
}
