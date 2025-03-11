package com.example.project7.model;

import com.example.project7.controller.edition.EditerProjet;

import java.util.ArrayList;
import java.sql.Date;
public class Controle {
    private int idControle;
    private String nomDevoir;
    private TypeDevoir typeDevoir;
    private FontDevoir fontDevoir;
    private FormatQuestion formatQuestion;
    private int nombreExemplaire;
    private int randomSeed;
    private String examHeader;
    private String reponseHeader;
    private Date creationDate;
    private ArrayList<Section> sections;
    private EditerProjet controller;

    public Controle() {
        this.nomDevoir = "";
        this.typeDevoir = TypeDevoir.Controle_Continue;
        this.fontDevoir = new FontDevoir();
        this.formatQuestion = new FormatQuestion();
        this.sections = new ArrayList<>();
        this.nombreExemplaire = 1;
        this.randomSeed = 12345678;
        this.examHeader = "";
        this.reponseHeader = "";
        this.creationDate = new Date(1,1,2000);
    }

    public void setNomDevoir(String nomDevoir) {
        this.nomDevoir = nomDevoir;
    }

    public void setTypeDevoir(TypeDevoir typeDevoir) {
        this.typeDevoir = typeDevoir;
    }
    public void setTypeDevoir(String typeDevoir) {
        this.typeDevoir = TypeDevoir.getTypeDevoir(typeDevoir);
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

    public void setNombreExemplaire(int nombreExemplaire) {
        this.nombreExemplaire = nombreExemplaire;
    }

    public void setRandomSeed(int randomSeed) {
        this.randomSeed = randomSeed;
    }

    public void setExamHeader(String examHeader) {
        this.examHeader = examHeader;
    }

    public void setReponseHeader(String reponseHeader) {
        this.reponseHeader = reponseHeader;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getNomDevoir() {
        return nomDevoir;
    }

    public String getTypeDevoir() {
        return typeDevoir.getNomDevoir();
    }

    public int getNombreExemplaire() {
        return nombreExemplaire;
    }

    public int getRandomSeed() {
        return randomSeed;
    }

    public String getExamHeader() {
        return examHeader;
    }

    public String getReponseHeader() {
        return reponseHeader;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public String toString() {
        return "Controle{" +
                "idControle=" + idControle +
                ", nomDevoir='" + nomDevoir + '\'' +
                ", typeDevoir=" + typeDevoir +
                ", fontDevoir=" + fontDevoir +
                ", formatQuestion=" + formatQuestion +
                ", nombreExemplaire=" + nombreExemplaire +
                ", randomSeed=" + randomSeed +
                ", creationDate=" + creationDate +
                '}';
    }
}