package com.example.project7.controller.edition;

import com.example.project7.FxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;


import java.net.URL;
import java.util.ResourceBundle;

public class EditerSection implements Initializable {

    private static int numberOfSection = 0;

    private Object currentController;

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

    private AnchorPane parentPane;

    public void setParentPane(AnchorPane parentPane) {
        this.parentPane = parentPane;
    }

    private Object getCurrentController() {
        return currentController;
    }

    @FXML
    public void handleInputIdentifier(KeyEvent event) {

        String identifierText = identifiantSection.getText();
        updateSectionIdentifiant(identifierText);

        if (identifiantSection.getText().trim().equals("")) {
            String sectionId = "Section#" + numberOfSection;
            updateSectionIdentifiant(sectionId);
            identifiantSection.setText(sectionId);
        }

    }

    private void updateSectionIdentifiant(String identifierText) {
        Object controller = getCurrentController();  // Assume this method retrieves the current controller

        if (controller instanceof EditerQCU) {
            ((EditerQCU) controller).setIdentifiantSection(identifierText);
        } else if (controller instanceof EditerQCM) {
            ((EditerQCM) controller).setIdentifiantSection(identifierText);
        } else if (controller instanceof EditerQuestion) {
            ((EditerQuestion) controller).setIdentifiantSection(identifierText);
        } else if (controller instanceof EditerDescription) {
            ((EditerDescription) controller).setIdentifiantSection(identifierText);
        }
    }

    private void loadContentToSectionPane(String fxmlFileName) {
        try {
            FxmlLoader loader = new FxmlLoader();
            AnchorPane newContent = loader.getPane("editer_quiz/" + fxmlFileName);

            if (newContent != null) {
                sectionPane.getChildren().setAll(newContent);

                String sectionId = "Section#" + numberOfSection;

                currentController = loader.getController();
                updateSectionIdentifiant(sectionId);
                identifiantSection.setText(sectionId);

            } else {
                System.err.println("Le contenu pour " + fxmlFileName + " n'a pas pu être chargé.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de " + fxmlFileName + ".fxml");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleClicksAddQCM(ActionEvent event) {
        //todo save in the database and return the previous interface which is the Editer Projet.
        loadContentToSectionPane("_4_EditerQCM");
    }

    @FXML
    public void handleClicksAddQCU(ActionEvent event) {
        //todo save in the database and return the previous interface which is the Editer Projet.
        loadContentToSectionPane("_5_EditerQCU");
    }

    @FXML
    public void handleClicksAddFreeQuestion(ActionEvent event) {
        //todo save in the database and return the previous interface which is the Editer Projet.
        loadContentToSectionPane("_6_EditerQuestionLibre");
    }

    @FXML
    public void handleClicksAddFreeDescription(ActionEvent event) {
        //todo save in the database and return the previous interface which is the Editer Projet.
        loadContentToSectionPane("_7_EditerDescription");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numberOfSection++;
        loadContentToSectionPane("_5_EditerQCU");
    }

    public static int getNumberOfSection(){
        return numberOfSection;
    }

}
