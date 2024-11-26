package com.example.project7.controller.edition;

import com.example.project7.FxmlLoader;
import com.example.project7.model.Section;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    private TableView<RowTableSection> tableSection;

    private AnchorPane parentPane;

    public void setParentPane(AnchorPane parentPane) {
        this.parentPane = parentPane;
    }


    @FXML
    public void handleClicksAddSection(ActionEvent event) {
        try {
            FxmlLoader object = new FxmlLoader();
            Parent view = object.getPane("editer_quiz/_3_EditerSection");

            Scene popupScene = new Scene(view);
            Stage popupStage = new Stage();

            popupStage.setTitle("Ajouter une Section");
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.initOwner(terminer.getScene().getWindow());
            popupStage.setScene(popupScene);
            popupStage.setResizable(false);

            EditerSection controller = (EditerSection) object.getController();
            if (controller != null) {
                controller.setParentPane(parentPane);
            }

            popupStage.showAndWait();
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ouverture de la popup : " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    public void handleClicksSaveProject(ActionEvent event) {
        //todo save in the database and show pop up if the user wants to export it in pdf or latex
    }


    @FXML
    public void handleClicksCancelProject(ActionEvent event) {
        //todo show a pop up for confirming the cancel and get back to the acceuil!!!!
        FxmlLoader object = new FxmlLoader();
        Parent view = object.getPane("Home");
        parentPane.getChildren().removeAll();
        parentPane.getChildren().setAll(view);
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