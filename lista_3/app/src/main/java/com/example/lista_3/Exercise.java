package com.example.lista_3;

import java.io.Serializable;

public class Exercise implements Serializable {
    public String content;
    public int points;

    public Exercise(String content, int points) {
        this.content = content;
        this.points = points;
    }
}