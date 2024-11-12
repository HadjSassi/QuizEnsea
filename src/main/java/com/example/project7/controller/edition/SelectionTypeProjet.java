package com.example.project7.controller.edition;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SelectionTypeProjet implements Initializable {

    @FXML
    private Button create;

    @FXML
    private Button cancel;

    @FXML
    private TextField name;

    @FXML
    private TextField location;

    @FXML
    private MenuButton typeProject;

    @FXML
    public void handleClicksCreate(ActionEvent event) {
        //todo create a new project that will be inserted in the database, and switch to the next interface editer projet
    }


    @FXML
    public void handleClicksCancel(ActionEvent event) {
        //todo just return the previous interface which is the acceuil.
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
