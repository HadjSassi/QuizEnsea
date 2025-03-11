package com.example.project7.model;

import java.util.ArrayList;

public class Description extends Section{

    private String texte;
    private ArrayList<String> images;
    private ArrayList<String> legends;
    private ArrayList<Double> widths;

    public Description(){
        texte = "";
        images = new ArrayList<>();
        legends = new ArrayList<>();
        widths = new ArrayList<>();
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

    public ArrayList<Double> getWidths() {
        return widths;
    }

    public void setWidths(ArrayList<Double> widths) {
        this.widths = widths;
    }

    @Override
    public String toString() {
        return "Description{" +
                "texte='" + texte + '\'' +
                ", images=" + images +
                ", legends=" + legends +
                ", widths=" + widths +
                "} " + super.toString();
    }
}
