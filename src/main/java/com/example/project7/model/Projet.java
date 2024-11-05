package com.example.project7.model;

import java.util.ArrayList;

public class Projet {

    private String nomProjet;
    private String localisationProjet;
    private TypeProjet typeProjet;
    private Controle devoir;

    public Projet(String nomProjet, String localisationProjet, TypeProjet typeProjet) {
        this.nomProjet = nomProjet;
        this.localisationProjet = localisationProjet;
        this.typeProjet = typeProjet;
        this.devoir = new Controle();
    }
}

class Controle {
    //todo mazelet n7ess elli mazelet fiha des méthodes nejem nzidouhom w nest7a9ouhom nerja3lek marra o5ra inchallah

    private String nomDevoir;
    private TypeDevoir typeDevoir;
    private FontDevoir fontDevoir;
    private FormatQuestion formatQuestion;
    private ArrayList<Section> sections;

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
}