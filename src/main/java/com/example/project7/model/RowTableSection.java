package com.example.project7.model;

public class RowTableSection {
    private String idSection;
    private String type;
    private String question;
    private int ordre;

    public RowTableSection(String idSection, String type, String question, int ordre) {
        this.idSection = idSection;
        this.type = type;
        this.question = question;
        this.ordre = ordre;
    }

    public String getIdSection() {
        return idSection;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }


}