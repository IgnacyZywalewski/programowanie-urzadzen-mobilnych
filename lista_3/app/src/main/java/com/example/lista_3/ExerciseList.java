package com.example.lista_3;

import java.io.Serializable;
import java.util.List;

public class ExerciseList implements Serializable {
    public List<Exercise> exercises;
    public String subject;
    public double grade;
    public int listNumber;

    public ExerciseList(List<Exercise> exercises, String subject, double grade, int listNumber) {
        this.exercises = exercises;
        this.subject = subject;
        this.grade = grade;
        this.listNumber = listNumber;
    }
}
