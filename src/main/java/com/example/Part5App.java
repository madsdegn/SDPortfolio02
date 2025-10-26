// Assignment 5 - Bachelor Program Activity Selector
// Mads Degn, Daniel Holst Pedersen
// 28/10-25
package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

public class Assignment5App extends Application {

    // Database file
    private static final String DB_URL = "jdbc:sqlite:database.db";

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

    // Database connection helper
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

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
            String program = basicComboBox.getValue();
            if (program != null) {
                loadSubjectModules(program);
                loadBasicCourses(program, basicCourseComboBox);
            }
        });

        // When subject module 1 is chosen, load its courses
        subjectComboBox1.setOnAction(e -> {
            String selected = subjectComboBox1.getValue();
            if (selected != null) {
                loadCoursesForModule(selected, subjectCourseComboBox1);
            }
        });

        // When subject module 2 is chosen, load its courses
        subjectComboBox2.setOnAction(e -> {
            String selected = subjectComboBox2.getValue();
            if (selected != null) {
                loadCoursesForModule(selected, subjectCourseComboBox2);
            }
        });
    }

    // Loaders from DB
    // Load all basic programs from the database into the basicComboBox
    private void loadPrograms() {
        basicComboBox.getItems().clear();
        String sql = "SELECT name FROM basic_programs";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                basicComboBox.getItems().add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Load subject modules for a given program into both subject module ComboBoxes
    private void loadSubjectModules(String programName) {
        subjectComboBox1.getItems().clear();
        subjectComboBox2.getItems().clear();
        String sql = "SELECT sm.name FROM subject_modules sm "
                + "JOIN basic_programs bp ON sm.basic_program_id = bp.id "
                + "WHERE bp.name = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, programName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String module = rs.getString("name");
                subjectComboBox1.getItems().add(module);
                subjectComboBox2.getItems().add(module);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Load all courses for a given subject module into the provided ComboBox
    private void loadCoursesForModule(String moduleName, ComboBox<String> comboBox) {
        comboBox.getItems().clear();
        String sql = "SELECT c.name FROM courses c "
                + "JOIN subject_modules sm ON c.subject_module_id = sm.id "
                + "WHERE sm.name = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, moduleName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                comboBox.getItems().add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Load all basic courses for a given program into the provided ComboBox
    private void loadBasicCourses(String programName, ComboBox<String> comboBox) {
        comboBox.getItems().clear();
        String sql = "SELECT c.name FROM courses c "
                + "JOIN basic_programs bp ON c.basic_program_id = bp.id "
                + "WHERE bp.name = ? AND c.is_basic = 1";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, programName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                comboBox.getItems().add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add activity and save to database
    // Called when the user clicks one of the "Add Activity" buttons
    // Ensures the student exists in the students table (INSERT OR IGNORE)
    // Inserts the chosen activity into the selections table
    // Refreshes the TextArea to show all activities for that student
    private void addActivity(ComboBox<String> comboBox) {
        String studentName = nameField.getText();
        String program = basicComboBox.getValue();
        String subject1 = subjectComboBox1.getValue();
        String subject2 = subjectComboBox2.getValue();
        String activity = comboBox.getValue();

        // Validate input: must have a student name and an activity selected
        if (studentName == null || studentName.isBlank() || activity == null) {
            selectedArea.appendText("Please enter your name and select an activity first.\n");
            return;
        }

        try (Connection conn = connect()) {

            // Insert student if not already in the database
            String insertStudent = "INSERT OR IGNORE INTO students(name, basic_program_id, subject1_id, subject2_id) "
                    + "VALUES (?, (SELECT id FROM basic_programs WHERE name=?), "
                    + "(SELECT id FROM subject_modules WHERE name=?), "
                    + "(SELECT id FROM subject_modules WHERE name=?))";
            try (PreparedStatement ps = conn.prepareStatement(insertStudent)) {
                ps.setString(1, studentName);
                ps.setString(2, program);
                ps.setString(3, subject1);
                ps.setString(4, subject2);
                ps.executeUpdate();
            }

            // Insert the selected activity for this student
            String insertSelection = "INSERT INTO selections(student_id, course_id) "
                    + "VALUES ((SELECT id FROM students WHERE name=?), "
                    + "(SELECT id FROM courses WHERE name=?))";
            try (PreparedStatement ps = conn.prepareStatement(insertSelection)) {
                ps.setString(1, studentName);
                ps.setString(2, activity);
                ps.executeUpdate();
            }

            // Update the TextArea with all activities for this student
            selectedArea.setText(getStudentActivities(studentName));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method: fetch all activities for a given student from the database
    // Returns a string with one line per activity
    private String getStudentActivities(String studentName) {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT c.name FROM selections sel "
                + "JOIN students s ON sel.student_id = s.id "
                + "JOIN courses c ON sel.course_id = c.id "
                + "WHERE s.name = ?";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sb.append(studentName).append(" - ").append(rs.getString("name")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        launch(); // Launches the JavaFX application
    }
}
