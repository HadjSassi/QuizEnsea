package com.example.project7.model;

import static com.example.project7.laguage.en.StringLang.Deuxpoints;
import static com.example.project7.laguage.en.StringLang.question;

public class FormatQuestion {
    private String premierTexte;
    private Boolean isNumerated;
    private TypeNumero numeroIncremente;
    private String DeuxiemeTexte;

    public FormatQuestion() {
        this.premierTexte = "question";
        this.isNumerated = true;
        this.numeroIncremente = TypeNumero.Arabe;
        this.DeuxiemeTexte = " : ";
    }

    public String getPremierTexte() {
        return premierTexte;
    }

    public void setPremierTexte(String premierTexte) {
        this.premierTexte = premierTexte;
    }

    public Boolean getNumerated() {
        return isNumerated;
    }

    public void setNumerated(Boolean numerated) {
        isNumerated = numerated;
    }

    public TypeNumero getNumeroIncremente() {
        return numeroIncremente;
    }

    public void setNumeroIncremente(TypeNumero numeroIncremente) {
        this.numeroIncremente = numeroIncremente;
    }

    public String getDeuxiemeTexte() {
        return DeuxiemeTexte;
    }

    public void setDeuxiemeTexte(String deuxiemeTexte) {
        DeuxiemeTexte = deuxiemeTexte;
    }

}
