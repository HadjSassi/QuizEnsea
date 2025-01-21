package com.example.project7.model;

import com.example.project7.controller.edition.EditerProjet;

import java.util.ArrayList;

public class Controle {
    private int idControle;
    private String nomDevoir;
    private TypeDevoir typeDevoir;
    private FontDevoir fontDevoir;
    private FormatQuestion formatQuestion;
    private ArrayList<Section> sections;
    private EditerProjet controller;

    public Controle() {
        this.nomDevoir = nomDevoir;
        this.typeDevoir = typeDevoir;
        this.fontDevoir = new FontDevoir();
        this.formatQuestion = new FormatQuestion();
        this.sections = new ArrayList<>();
    }

    public void setNomDevoir(String nomDevoir) {
        this.nomDevoir = nomDevoir;
    }

    public void setTypeDevoir(TypeDevoir typeDevoir) {
        this.typeDevoir = typeDevoir;
    }

    public FormatQuestion getFormatQuestion() {
        return formatQuestion;
    }

    public void setFormatQuestion(FormatQuestion formatQuestion) {
        this.formatQuestion = formatQuestion;
    }

    public FontDevoir getFontDevoir() {
        return fontDevoir;
    }

    public void setFontDevoir(FontDevoir fontDevoir) {
        this.fontDevoir = fontDevoir;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public void removeSection (Section section) {
        this.sections.remove(section);
    }

    public int getIdControle() {
        return idControle;
    }

    public void setIdControle(int idControle) {
        this.idControle = idControle;
    }

    public void setController(EditerProjet controller) {
        this.controller = controller;
    }
    public EditerProjet getController() {
        return controller;
    }
}