package com.example.project7.controller.edition;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class SupprimerSection implements Initializable {

    @FXML
    private Button confirmerSuppression;

    @FXML
    private Button cancelSuppression;

    @FXML
    public void confimerSuppression(ActionEvent event) {
        //todo supprimer de la base de donnée et modifie le table view des sections
    }

    @FXML
    public void cancelSuppression(ActionEvent event) {
        //todo do nothing!
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
