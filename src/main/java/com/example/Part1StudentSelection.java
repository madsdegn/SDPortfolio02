// Assignment 1 - Student Selection Model
// Mads Degn, Daniel Holst Pedersen
// 28/10/25
package com.example;

import java.util.ArrayList;
import java.util.List;

public class Assignment1StudentSelection {

    // Fields
    private String studentName;   // The name of the student
    private String program;       // The basic program
    private String subject1;      // The subject module 1
    private String subject2;      // The subject module 2

    // A list to store all activities the student has added
    private List<String> activities;

    // Constructor for creating a new student selection
    // Initializes the student name, program, and subject modules
    // Also creates an empty list to hold activities
    public Assignment1StudentSelection(String studentName, String program, String subject1, String subject2) {
        this.studentName = studentName;
        this.program = program;
        this.subject1 = subject1;
        this.subject2 = subject2;
        this.activities = new ArrayList<>(); // start with no activities
    }

    // Add a new activity to the student list
    public void addActivity(String activity) {
        activities.add(activity);
    }

    // Get all activities the student has added so far
    public List<String> getActivities() {
        return activities;
    }

    // Convert the student's selections into a readable string. Each activity is shown on its own line
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String act : activities) {
            sb.append(studentName).append(" - ").append(act).append("\n");
        }
        return sb.toString();
    }
}
