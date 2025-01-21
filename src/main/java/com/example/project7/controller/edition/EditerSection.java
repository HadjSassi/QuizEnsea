package com.example.project7.controller.edition;

import com.example.project7.FxmlLoader;
import com.example.project7.model.Controle;
import com.example.project7.model.Section;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import mysql_connection.MySqlConnection;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditerSection implements Initializable {

    private static int numberOfSection = 0;

    private Object currentController;

    private AnchorPane parentPane;

    private Controle devoir;

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

    public void setDevoir(Controle devoir) {
        this.devoir = devoir;
        updateSection( identifiantSection.getText());
        initializeNumberOfSection();
        numberOfSection++;
    }

    public void setParentPane(AnchorPane parentPane) {
        this.parentPane = parentPane;
    }

    private Object getCurrentController() {
        return currentController;
    }

    @FXML
    public void handleInputIdentifier(KeyEvent event) {

        updateSection( identifiantSection.getText());

        if (identifiantSection.getText().trim().equals("")) {
            String sectionId = "Section#" + numberOfSection;
            updateSection(sectionId);
            identifiantSection.setText(sectionId);
        }

    }

    private void updateSection(String identifierText) {
        Object controller = getCurrentController();
        Section section = new Section();
        section.setDevoir(this.devoir);
        section.setIdSection(identifierText);
        section.setOrdreSection(numberOfSection);

        if (controller instanceof EditerQCU) {
            ((EditerQCU) controller).setSection(section);
        } else if (controller instanceof EditerQCM) {
            ((EditerQCM) controller).setSection(section);
        } else if (controller instanceof EditerQuestion) {
            ((EditerQuestion) controller).setSection(section);
        } else if (controller instanceof EditerDescription) {
            ((EditerDescription) controller).setSection(section);
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
                updateSection(sectionId);
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
        loadContentToSectionPane("_5_EditerQCU");
    }

    private void initializeNumberOfSection() {
        String countQuery = "SELECT COUNT(*) AS sectionCount FROM section WHERE controleId = ?";
        numberOfSection = 0;

        try (Connection connection = MySqlConnection.getOracleConnection();
             PreparedStatement statement = connection.prepareStatement(countQuery)) {


            this.devoir.setIdControle(1);

            statement.setInt(1, this.devoir.getIdControle());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    numberOfSection = resultSet.getInt("sectionCount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving section count: " + e.getMessage());
        }
    }

    public static int getNumberOfSection() {
        return numberOfSection;
    }

    public static void cancelSection() {
        numberOfSection--;
    }

}
