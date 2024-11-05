package com.example.project7.model;

public class Section {
    private String idSection;
    private int ordreSection;
    private static int numberOfSections = 0 ;

    public Section() {
        this.ordreSection = ++numberOfSections;
        this.idSection = Integer.toString(ordreSection);
    }

    public String getIdSection() {
        return idSection;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public int getOrdreSection() {
        return ordreSection;
    }

    public void setOrdreSection(int ordreSection) {
        this.ordreSection = ordreSection;
    }


}
