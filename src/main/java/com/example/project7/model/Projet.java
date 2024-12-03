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
}