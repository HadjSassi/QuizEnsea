package com.example.project7.model;

public class RowTableSection {
    private String idSection;
    private String type;
    private String question;
    private int ordre;
    private String chemin;
    private String legend;
    private double width;

    public RowTableSection(String idSection, String type, String question, int ordre) {
        this.idSection = idSection;
        this.type = type;
        this.question = question;
        this.ordre = ordre;
    }

    public RowTableSection(String chemin, String legend, int ordre) {
        this.chemin = chemin;
        this.legend = legend;
        this.ordre = ordre;
    }

    public RowTableSection(String chemin, String legend, int ordre, double width) {
        this.chemin = chemin;
        this.legend = legend;
        this.ordre = ordre;
        this.width = width;
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

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }
}