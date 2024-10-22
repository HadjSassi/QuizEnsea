package com.example.project7.model;

public enum TypeNumero {
    Arabe("Arabe"),
    Indian("Indian"),
    Roman("Roman"),
    Majuscule("Majuscule"),
    Miniscule("Miniscule");

    private final String nom;

    TypeNumero(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }
}
