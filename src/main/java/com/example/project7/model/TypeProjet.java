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

    public static TypeProjet getTypeProjet(String nomProjet) {
        for (TypeProjet type : TypeProjet.values()) {
            if (type.getNomProjet().equalsIgnoreCase(nomProjet)) {
                return type;
            }
        }
        return null;
    }
}
