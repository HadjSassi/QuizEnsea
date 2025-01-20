package com.example.project7.model;

public enum TypeDevoir {

    Controle_Continue("Contrôle Continu"),
    Examen_Finale("Examen Final"),
    Test_Evaluation("Évaluation"),
    Examen_TP("Examen TP"),
    Test_de_niveau("Test de Niveau");

    private final String nomDevoir;

    TypeDevoir(String nomDevoir) {
        this.nomDevoir = nomDevoir;
    }

    public String getNomDevoir() {
        return nomDevoir;
    }
}
