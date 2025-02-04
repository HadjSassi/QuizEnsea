package com.example.project7.controller.edition;

import javafx.beans.property.SimpleStringProperty;

public class SectionRow {
    private final SimpleStringProperty id;
    private final SimpleStringProperty type;

    public SectionRow(String id, String name) {
        this.id = new SimpleStringProperty(id);
        this.type = new SimpleStringProperty(name);
    }

    public String  getId() {
        return id.get();
    }

    public String getType() {
        return type.get();
    }
}

