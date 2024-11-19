package com.example.project7.controller.edition;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class EditerDescription implements Initializable {


    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Button addImageButton;

    @FXML
    private TableView tableViewImages;

    @FXML
    private Button ajouterDescription;

    @FXML
    private Button cancelDescription;

    @FXML
    public void handleClickAddImage(ActionEvent event) {
        //todo open the explorer to select an image, and to save it in the tableview with empty legend that can be modified.
    }

    @FXML
    public void handleClickAddDescription(ActionEvent event) {
        //todo save the description in the database and quit
    }

    @FXML
    public void handleClickCancelDescription(ActionEvent event) {
        //todo cancel modification of the description and quit
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
