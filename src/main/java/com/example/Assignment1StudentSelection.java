package com.example;

import java.util.ArrayList;
import java.util.List;

public class Assignment1StudentSelection {

    private String studentName;
    private String program;
    private String subject1;
    private String subject2;
    private List<String> activities; // store all added activities

    public Assignment1StudentSelection(String studentName, String program, String subject1, String subject2) {
        this.studentName = studentName;
        this.program = program;
        this.subject1 = subject1;
        this.subject2 = subject2;
        this.activities = new ArrayList<>();
    }

    public void addActivity(String activity) {
        activities.add(activity);
    }

    public List<String> getActivities() {
        return activities;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String act : activities) {
            sb.append(studentName).append(" - ").append(act).append("\n");
        }
        return sb.toString();
    }
}
