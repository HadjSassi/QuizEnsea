package com.example.project7.controller.edition;

import com.example.project7.FxmlLoader;
import com.example.project7.model.Projet;
import com.example.project7.model.Section;
import com.example.project7.model.TypeDevoir;
import com.example.project7.model.TypeNumero;
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
import mysql_connection.MySqlConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EditerProjet implements Initializable {

    private static Section dernierSection;

    private Projet projet;

    @FXML
    private Button terminer;

    @FXML
    private Button cancel;

    @FXML
    private TextField nomDevoir;

    @FXML
    private MenuButton typeDevoir;

    @FXML
    private DatePicker dateDevoir;

    @FXML
    private TextField formatQuestionText;

    @FXML
    private MenuButton formatQuestionNumber;

    @FXML
    private MenuButton ajouterSection;

    @FXML
    private TableView<RowTableSection> tableSection;

    private AnchorPane parentPane;

    public void setParentPane(AnchorPane parentPane) {
        this.parentPane = parentPane;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
        nomDevoir.setText(this.projet.getNomProjet());
        this.insertControleData();
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
            popupScene.getStylesheets().add(getClass().getResource("/com/example/project7/css/styles.css").toExternalForm());

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
    public void handleClicksCancelProject(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Are you sure to cancel the modifications ?");
        alert.setContentText("All modifications will be lost!");

        ButtonType buttonTypeYes = new ButtonType("Oui");
        ButtonType buttonTypeNo = new ButtonType("Non");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeYes) {
                FxmlLoader object = new FxmlLoader();
                Parent view = object.getPane("Home");
                parentPane.getChildren().removeAll();
                parentPane.getChildren().setAll(view);
            }
        });
    }

    private void insertControleData() {
        //todo you need to correct the Controle table you need to put two other field one of the formatOfQuesiton number and text


        String checkControleQuery = "SELECT COUNT(*) FROM Controle WHERE projetId = ?";

        try (Connection connection = MySqlConnection.getOracleConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkControleQuery)) {

            checkStatement.setInt(1, projet.getIdProjet());

            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next()) {
                    int rowCount = resultSet.getInt(1); // Get the count of rows found

                    if (rowCount == 0) {
                        String insertControleQuery = "INSERT INTO Controle (nomDevoir, typeDevoir, fontDevoir, fontSize, formatQuestionNumber, formatQuestionTexte, projetId, creationDate) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_DATE)";

                        try (PreparedStatement insertStatement = connection.prepareStatement(insertControleQuery)) {
                            insertStatement.setString(1, nomDevoir.getText());
                            insertStatement.setString(2, typeDevoir.getText());
                            insertStatement.setString(3, "Times New Roman");
                            insertStatement.setInt(4, 15);
                            insertStatement.setInt(5, 1);
                            insertStatement.setString(6, "Question");
                            insertStatement.setInt(7, projet.getIdProjet()); // Use the current projetId

                            int rowsAffected = insertStatement.executeUpdate();
                            if (!(rowsAffected > 0)) {
                                System.err.println("Failed to insert Controle data.");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.err.println("Error inserting data into Controle table: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Controle data already exists for this Projet.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error checking Controle table: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        dateDevoir.setValue(LocalDate.now());

        formatQuestionText.setText("Question :");


        for (TypeDevoir type : TypeDevoir.values()) {
            MenuItem menuItem = new MenuItem(type.getNomDevoir());
            menuItem.setOnAction(event -> typeDevoir.setText(type.getNomDevoir()));
            typeDevoir.getItems().add(menuItem);
        }

        if (!typeDevoir.getItems().isEmpty()) {
            typeDevoir.setText(typeDevoir.getItems().get(0).getText());
        }

        for (TypeNumero typeNumero : TypeNumero.values()) {
            MenuItem menuItem = new MenuItem(typeNumero.getValue());
            menuItem.setOnAction(event -> formatQuestionNumber.setText(typeNumero.getValue()));
            formatQuestionNumber.getItems().add(menuItem);
        }

        if (!formatQuestionNumber.getItems().isEmpty()) {
            formatQuestionNumber.setText(formatQuestionNumber.getItems().get(0).getText());
        }


    }

    @FXML
    public void handleClicksSaveProject(ActionEvent event) {
        //todo save in the database and show pop up if the user wants to export it in pdf or latex

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