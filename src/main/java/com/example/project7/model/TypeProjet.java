package com.example.project7.model;

public enum TypeProjet {

    ExempleProjet1("Projet Exemple 1"),
    ExempleProjet2("Projet Exemple 2"),
    ExempleProjet3("Projet Exemple 3"),
    ExempleProjet4("Projet Exemple 4"),
    ExempleProjet5("Projet Exemple 5"),
    ExempleProjet6("Projet Exemple 6"),
    ExempleProjet7("Projet Exemple 7"),
    ExempleProjet8("Projet Exemple 8"),
    ExempleProjet9("Projet Exemple 9"),
    ExempleProjet10("Projet Exemple 10");

    private final String nomProjet;

    TypeProjet(String nomProjet) {
        this.nomProjet = nomProjet;
    }

    public String getNomProjet() {
        return nomProjet;
    }
}
