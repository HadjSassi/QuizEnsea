package com.example.project7.controller.edition;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class EditerSection implements Initializable {

    @FXML
    private TextField identifiantSection;

    @FXML
    private SplitMenuButton typeSection;

    @FXML
    private Button ajouterSection;

    @FXML
    private Button cancelSection;

    @FXML
    private AnchorPane sectionPane;

    @FXML
    public void handleClicksAddSection(ActionEvent event) {
        //todo save in the database and return the previous interface which is the Editer Projet.
    }

    @FXML
    public void handleClicksCancel(ActionEvent event) {
        //todo just return the previous interface which is the Editer Projet.
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
