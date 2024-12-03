package com.example.project7.model;

public enum TypeProjet {

    BasicModel("Basic Model");

    private final String nomProjet;

    TypeProjet(String nomProjet) {
        this.nomProjet = nomProjet;
    }

    public String getNomProjet() {
        return nomProjet;
    }
}
