package com.example.project7.model;

import java.util.ArrayList;

public class QCM  extends Section{
    private String question;
    private boolean isQCU;
    private ArrayList<Reponse> reponsesCorrecte;
    private ArrayList<Reponse> reponsesIncorrecte;

    public QCM(){
        this.question = "";
        this.isQCU = false;
        this.reponsesCorrecte = new ArrayList<>();
        this.reponsesIncorrecte = new ArrayList<>();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isQCU() {
        return isQCU;
    }

    public void setQCU(boolean QCU) {
        isQCU = QCU;
    }

    public ArrayList<Reponse> getReponsesCorrecte() {
        return reponsesCorrecte;
    }

    public void setReponsesCorrecte(ArrayList<Reponse> reponsesCorrecte) {
        this.reponsesCorrecte = reponsesCorrecte;
    }

    public ArrayList<Reponse> getReponsesIncorrecte() {
        return reponsesIncorrecte;
    }

    public void setReponsesIncorrecte(ArrayList<Reponse> reponsesIncorrecte) {
        this.reponsesIncorrecte = reponsesIncorrecte;
    }
}