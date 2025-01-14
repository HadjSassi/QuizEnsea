package com.example.project7.controller.edition;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditerQCU implements Initializable {

    @FXML
    private TextField enonceQuestion;

    @FXML
    private TextField baremePos;

    @FXML
    private TextField reponseCorrect;

    @FXML
    private TextField baremeNegDefault;

    @FXML
    private Button ajouterMauvaiseReponse;

    @FXML
    private TableView<Void> incorrectTableView;

    @FXML
    private Button ajouterQcu;

    @FXML
    private Button cancelQcu;

    private boolean verifyQuesstion(){
        String question = enonceQuestion.getText().trim();

        if (question.isEmpty()) {
            if (!enonceQuestion.getStyleClass().contains("text-field-danger")) {
                enonceQuestion.getStyleClass().add("text-field-danger");
            }
            return false;
        } else {
            enonceQuestion.getStyleClass().removeAll("text-field-danger");
            return true;
        }
    }

    @FXML
    public void handleClicksAddWrongResponce(ActionEvent event) {
        //todo insert a new line in the incorrectTable View
    }

    @FXML
    public void handleClicksAddQCU(ActionEvent event) {
        //todo save in the database and return the previous interface which is the Editer Projet.
        boolean correctQuestion = verifyQuesstion();
    }

    @FXML
    public void handleClicksCancelQCU(ActionEvent event) {
        Stage stage = (Stage) cancelQcu.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        baremePos.setText("1");
        baremeNegDefault.setText("0");
        //todo maybe we need to initialize the tableview!
    }
}
