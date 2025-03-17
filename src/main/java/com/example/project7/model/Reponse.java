package com.example.project7.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Reponse {
    private SimpleStringProperty response;
    private SimpleIntegerProperty score;

    public Reponse() {
        this.response = new SimpleStringProperty("");
        this.score = new SimpleIntegerProperty(0);
    }


    public Reponse(String response, int score) {
        this.response = new SimpleStringProperty(response);
        this.score = new SimpleIntegerProperty(score);
    }

    public String getResponse() {
        return response.get();
    }

    public void setResponse(String response) {
        this.response.set(response);
    }

    public int getScore() {
        return score.get();
    }

    public void setScore(int score) {
        this.score.set(score);
    }
}
