package com.example.project7.model;

public class QuestionLibre  extends Section{
    private String question;
    private float scoreTotal;
    private int nombreScore;
    private int nombreLigne;
    private float tailleLigne;
    private String rappel;

    public QuestionLibre(){
        this.question = "";
        this.scoreTotal = 5;
        this.nombreScore = 5;
        this.nombreLigne = 10;
        this.tailleLigne = 0.3f;
        this.rappel = "";
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public float getScoreTotal() {
        return scoreTotal;
    }

    public void setScoreTotal(float scoreTotal) {
        this.scoreTotal = scoreTotal;
    }

    public int getNombreScore() {
        return nombreScore;
    }

    public void setNombreScore(int nombreScore) {
        this.nombreScore = nombreScore;
    }

    public int getNombreLigne() {
        return nombreLigne;
    }

    public void setNombreLigne(int nombreLigne) {
        this.nombreLigne = nombreLigne;
    }

    public float getTailleLigne() {
        return tailleLigne;
    }

    public void setTailleLigne(float tailleLigne) {
        this.tailleLigne = tailleLigne;
    }

    public String getRappel() {
        return rappel;
    }

    public void setRappel(String rappel) {
        this.rappel = rappel;
    }
}
