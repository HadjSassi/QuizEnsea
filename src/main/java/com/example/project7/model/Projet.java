package com.example.project7.model;

import java.util.ArrayList;
import java.util.Date;

public class Projet {

    private int idProjet;
    private String nomProjet;
    private String localisationProjet;
    private TypeProjet typeProjet;
    private Date date;
    private ArrayList<Controle> devoir;

    public Projet(String nomProjet, String localisationProjet, TypeProjet typeProjet) {
        this.idProjet = 0;
        this.nomProjet = nomProjet;
        this.localisationProjet = localisationProjet;
        this.typeProjet = typeProjet;
        this.devoir = new ArrayList<>();
        this.date = new Date();
    }

    public Projet(int idProjet, String nomProjet, String localisationProjet, TypeProjet typeProjet, Date date) {
        this.idProjet = idProjet;
        this.nomProjet = nomProjet;
        this.localisationProjet = localisationProjet;
        this.typeProjet = typeProjet;
        this.date = date;
        this.devoir = new ArrayList<>();
    }

    public int getIdProjet() {
        return idProjet;
    }

    public String getNomProjet() {
        return nomProjet;
    }

    public String getLocalisationProjet() {
        return localisationProjet;
    }

    public TypeProjet getTypeProjet() {
        return typeProjet;
    }

    public Date getDate() {
        return date;
    }

    public ArrayList<Controle> getDevoir() {
        return devoir;
    }

    public void setIdProjet(int idProjet) {
        this.idProjet = idProjet;
    }

    public void setNomProjet(String nomProjet) {
        this.nomProjet = nomProjet;
    }

    public void setLocalisationProjet(String localisationProjet) {
        this.localisationProjet = localisationProjet;
    }

    public void setTypeProjet(TypeProjet typeProjet) {
        this.typeProjet = typeProjet;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDevoir(ArrayList<Controle> devoir) {
        this.devoir = devoir;
    }

    @Override
    public String toString() {
        return "Projet{" +
                "idProjet=" + idProjet +
                ", nomProjet='" + nomProjet + '\'' +
                ", localisationProjet='" + localisationProjet + '\'' +
                ", typeProjet=" + typeProjet +
                ", date=" + date +
                ", devoir=" + devoir +
                '}';
    }

}