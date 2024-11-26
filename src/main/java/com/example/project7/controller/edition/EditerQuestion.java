package com.example.project7.controller.edition;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditerQuestion implements Initializable {

    @FXML
    private TextField enonceQuestion;

    @FXML
    private TextField scoringTotale;

    @FXML
    private TextField nombreScore;

    @FXML
    private TextField nombreLignes;

    @FXML
    private TextField tailleLigne;

    @FXML
    private TextField rappelQuestion;

    @FXML
    private Button ajouterQuestion;

    @FXML
    private Button cancelQuestion;


    @FXML
    public void ajouterQuestion(ActionEvent event) {
        //todo addQuesiton to the database and get back to the previous interface
    }


    @FXML
    public void cancelQuestion(ActionEvent event) {
        Stage stage = (Stage) cancelQuestion.getScene().getWindow();
        stage.close();    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
