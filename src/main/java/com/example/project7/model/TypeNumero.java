package com.example.project7.model;

import static com.example.project7.laguage.en.StringLang.*;

public enum TypeNumero {
    Arabe("Arabe"),
    Indian("Indian"),
    Roman("Roman"),
    Majuscule("Majuscule"),
    Miniscule("Miniscule"),;

    private final String value;

    TypeNumero(String nom) {
        this.value = nom;
    }

    public String getValue() {
        return value;
    }
}
