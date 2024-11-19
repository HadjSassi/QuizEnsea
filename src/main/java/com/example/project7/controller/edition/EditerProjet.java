package com.example.project7.controller.edition;

import com.example.project7.model.Section;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class EditerProjet implements Initializable {

    private static Section dernierSection;

    @FXML
    private Button terminer;

    @FXML
    private Button cancel;

    @FXML
    private TextField nomDevoir;

    @FXML
    private TextField typeDevoir;

    @FXML
    private DatePicker dateDevoir;

    @FXML
    private TextField formatQuestion;

    @FXML
    private MenuButton ajouterSection;

    @FXML
    private MenuItem ajouterQCU;

    @FXML
    private MenuItem ajouterQCM;

    @FXML
    private MenuItem ajouterQuestionLibre;

    @FXML
    private MenuItem ajouterDescription;

    @FXML
    private TableView<RowTableSection> tableSection;


    @FXML
    public void handleClicksAddSection(ActionEvent event) {
        //todo show the previous type of the project
    }

    @FXML
    public void handleClicksSaveProject(ActionEvent event) {
        //todo save in the database and show pop up if the user wants to export it in pdf or latex
    }


    @FXML
    public void handleClicksCancelProject(ActionEvent event) {
        //todo show a pop up for confirming the cancel
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

class RowTableSection {
    //todo i don't know if this is correct or not you need to verify the correct format
    private Integer numeroSection;
    private String typeSection;
    private String enonceSection;
    private ActionTableSection actionTableSection;
}

class ActionTableSection {
    //todo also i don't know if am correct here or not
    private Button supprimerSection;
    private Button modifierSection;
    private Button deplacerTopSection;
    private Button deplacerBasSection;
}