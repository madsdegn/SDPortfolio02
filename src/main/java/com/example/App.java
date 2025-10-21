package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

public class App extends Application {

    // Declare UI controls as fields so we can access them in multiple methods
    private ComboBox<String> basicComboBox;
    private ComboBox<String> subjectComboBox1;
    private ComboBox<String> subjectComboBox2;
    private ComboBox<String> basicCourseComboBox;
    private ComboBox<String> subjectCourseComboBox1;
    private ComboBox<String> subjectCourseComboBox2;
    private TextField nameField;
    private TextArea selectedArea;

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

        // ComboBoxes (empty at first, filled from DB)
        basicComboBox = new ComboBox<>();
        basicComboBox.setPrefWidth(200);

        subjectComboBox1 = new ComboBox<>();
        subjectComboBox1.setPrefWidth(200);

        subjectComboBox2 = new ComboBox<>();
        subjectComboBox2.setPrefWidth(200);

        basicCourseComboBox = new ComboBox<>();
        basicCourseComboBox.setPrefWidth(200);

        subjectCourseComboBox1 = new ComboBox<>();
        subjectCourseComboBox1.setPrefWidth(200);

        subjectCourseComboBox2 = new ComboBox<>();
        subjectCourseComboBox2.setPrefWidth(200);

        // TextArea
        selectedArea = new TextArea();
        selectedArea.setEditable(false);
        selectedArea.setPromptText("Selected courses...");
        selectedArea.setPrefWidth(200);
        selectedArea.setPrefHeight(200);
        selectedArea.setWrapText(true);

        // Save button
        Button saveButton = new Button("Save Selection");
        saveButton.setOnAction(e -> saveSelection());
        saveButton.setPrefWidth(200);

        // Layout
        HBox topContainer = new HBox(20);
        VBox nameBox = new VBox(5, nameLabel, nameField);
        VBox basicBox = new VBox(5, basicLabel, basicComboBox);
        VBox subjectBox1 = new VBox(5, subjectLabel1, subjectComboBox1);
        VBox subjectBox2 = new VBox(5, subjectLabel2, subjectComboBox2);
        topContainer.getChildren().addAll(nameBox, basicBox, subjectBox1, subjectBox2);

        // Course selectors with labels
        VBox basicCourseBox = new VBox(5, basicCourseLabel, basicCourseComboBox);
        VBox subjectCourseBox1 = new VBox(5, subjectCourseLabel1, subjectCourseComboBox1);
        VBox subjectCourseBox2 = new VBox(5, subjectCourseLabel2, subjectCourseComboBox2);

        // TextArea + Save button stacked
        VBox textAreaBox = new VBox(10, selectedArea, saveButton);

        // Bottom row now has 3 labeled course boxes + the textAreaBox
        HBox bottomContainer = new HBox(20, basicCourseBox, subjectCourseBox1, subjectCourseBox2, textAreaBox);

        // Main layout
        VBox layout = new VBox(10, topContainer, new Separator(), bottomContainer);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 900, 300);
        stage.setScene(scene);
        stage.setTitle("Bachelor Program: Activity Selector");
        stage.show();

        // Load initial data from DB
        loadPrograms();

        // Hook up events: when a program is chosen, load its subject modules
        basicComboBox.setOnAction(e -> {
            int programId = basicComboBox.getSelectionModel().getSelectedIndex() + 1;
            loadSubjectModules(programId);
        });

        // When a subject module is chosen, load its courses
        subjectComboBox1.setOnAction(e -> {
            int moduleId = subjectComboBox1.getSelectionModel().getSelectedIndex() + 1;
            loadCourses(moduleId, subjectCourseComboBox1);
        });

        subjectComboBox2.setOnAction(e -> {
            int moduleId = subjectComboBox2.getSelectionModel().getSelectedIndex() + 1;
            loadCourses(moduleId, subjectCourseComboBox2);
        });
    }

    // --- Database loaders ---
    private void loadPrograms() {
        basicComboBox.getItems().clear();
        String sql = "SELECT name FROM programs";

        try (Connection conn = Database.connect(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                basicComboBox.getItems().add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSubjectModules(int programId) {
        subjectComboBox1.getItems().clear();
        subjectComboBox2.getItems().clear();
        String sql = "SELECT name FROM subject_modules WHERE program_id = ?";

        try (Connection conn = Database.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, programId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                subjectComboBox1.getItems().add(rs.getString("name"));
                subjectComboBox2.getItems().add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCourses(int moduleId, ComboBox<String> comboBox) {
        comboBox.getItems().clear();
        String sql = "SELECT name FROM courses WHERE module_id = ?";

        try (Connection conn = Database.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, moduleId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                comboBox.getItems().add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- Save selection ---
    private void saveSelection() {
        String sql = "INSERT INTO selections(student_name, program, subject1, subject2, course_basic, course_subject1, course_subject2) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, basicComboBox.getValue());
            pstmt.setString(3, subjectComboBox1.getValue());
            pstmt.setString(4, subjectComboBox2.getValue());
            pstmt.setString(5, basicCourseComboBox.getValue());
            pstmt.setString(6, subjectCourseComboBox1.getValue());
            pstmt.setString(7, subjectCourseComboBox2.getValue());

            pstmt.executeUpdate();

            selectedArea.setText("Saved!\n"
                    + "Name: " + nameField.getText() + "\n"
                    + "Program: " + basicComboBox.getValue() + "\n"
                    + "Subject 1: " + subjectComboBox1.getValue() + "\n"
                    + "Subject 2: " + subjectComboBox2.getValue() + "\n"
                    + "Basic Course: " + basicCourseComboBox.getValue() + "\n"
                    + "Subject Course 1: " + subjectCourseComboBox1.getValue() + "\n"
                    + "Subject Course 2: " + subjectCourseComboBox2.getValue());

        } catch (Exception e) {
            e.printStackTrace();
            selectedArea.setText("Error saving selection: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
