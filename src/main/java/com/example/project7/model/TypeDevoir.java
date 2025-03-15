package com.example.project7.model;

import static com.example.project7.laguage.en.StringLang.*;

public enum TypeDevoir {

    Controle_Continue( "Contrôle Continu"),
    Examen_Finale("Examen Final"),
    Test_Evaluation("Évaluation"),
    Examen_TP("Examen TP"),
    Test_de_niveau("Test de Niveau"),;

    private final String nomDevoir;

    TypeDevoir(String nomDevoir) {
        this.nomDevoir = nomDevoir;
    }

    public static TypeDevoir getTypeDevoir(String typeDevoir) {
        for(TypeDevoir td : TypeDevoir.values()) {
            if(td.getNomDevoir().equals(typeDevoir)) {
                return td;
            }
        }
        return null;
    }

    public String getNomDevoir() {
        return nomDevoir;
    }

}
