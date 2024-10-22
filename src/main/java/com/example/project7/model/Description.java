package com.example.project7.model;

import java.util.ArrayList;

public class Description extends Section{

    private String texte;
    private ArrayList<String> images;
    private ArrayList<String> legends;

    public Description(){
        texte = "";
        images = new ArrayList<>();
        legends = new ArrayList<>();
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<String> getLegends() {
        return legends;
    }

    public void setLegends(ArrayList<String> legends) {
        this.legends = legends;
    }
}
