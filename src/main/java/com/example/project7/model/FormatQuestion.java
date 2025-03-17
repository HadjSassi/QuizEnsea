package com.example.project7.model;

public class FormatQuestion {
    private String premierTexte;
    private Boolean isNumerated;
    private TypeNumero numeroIncremente;
    private String DeuxiemeTexte;

    public FormatQuestion() {
        this.premierTexte = "Question";
        this.isNumerated = true;
        this.numeroIncremente = TypeNumero.Arabe;
        this.DeuxiemeTexte = ":";
    }

    public String getPremierTexte() {
        return premierTexte;
    }

    public void setPremierTexte(String premierTexte) {
        this.premierTexte = premierTexte;
    }

    public TypeNumero getNumeroIncremente() {
        return numeroIncremente;
    }

    public void setNumeroIncremente(TypeNumero numeroIncremente) {
        this.numeroIncremente = numeroIncremente;
    }

}
