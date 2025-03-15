package com.example.project7.model;

public class FontDevoir {

    private String nomPolice;
    private int sizePolice;

    public FontDevoir() {
        this.nomPolice = "courier";
        this.sizePolice = 12;
    }

    public String getNomPolice() {
        return nomPolice;
    }

    public void setNomPolice(String nomPolice) {
        this.nomPolice = nomPolice;
    }

    public int getSizePolice() {
        return sizePolice;
    }

    public void setSizePolice(int sizePolice) {
        this.sizePolice = sizePolice;
    }
}

