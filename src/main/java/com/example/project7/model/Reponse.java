package com.example.project7.model;

public class Reponse {
    private String textReponse;
    private int baremeReponse;

    public Reponse(){
        textReponse = "";
        baremeReponse = 0;
    }

    public String getTextReponse() {
        return textReponse;
    }

    public void setTextReponse(String textReponse) {
        this.textReponse = textReponse;
    }

    public int getBaremeReponse() {
        return baremeReponse;
    }

    public void setBaremeReponse(int baremeReponse) {
        this.baremeReponse = baremeReponse;
    }
}
