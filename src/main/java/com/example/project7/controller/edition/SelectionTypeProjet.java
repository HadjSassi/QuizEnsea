package com.example.project7.controller.edition;

import com.example.project7.FxmlLoader;
import com.example.project7.model.Projet;
import com.example.project7.model.TypeProjet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import mysql_connection.MySqlConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.ResourceBundle;

import static com.example.project7.laguage.en.StringLang.*;
import static com.example.project7.laguage.en.StringLang.userHome;

public class SelectionTypeProjet implements Initializable {

    @FXML
    private TextField name;

    @FXML
    private TextField location;

    @FXML
    private Label nameLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private MenuButton typeProject;

    private AnchorPane parentPane;

    public void setParentPane(AnchorPane parentPane) {
        this.parentPane = parentPane;
    }

    private TypeProjet getTypeProjetByName(String name) {
        for (TypeProjet type : TypeProjet.values()) {
            if (type.getNomProjet().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    @FXML
    public void handleClicksCreate(ActionEvent event) {
        boolean correctName = verifyProjectName();
        boolean correctLocation = verifyLocation();

        if (correctName && correctLocation) {
            String projectName = name.getText().trim();
            String projectLocation = location.getText().trim();
            String projectType = typeProject.getText();
            TypeProjet selectedType = getTypeProjetByName(projectType);
            int idProjet = insertProjectIntoDatabase(projectName, projectLocation, projectType);
            if (idProjet != -1) {
                if (createProjectDirectory(projectLocation, projectName)) {
                    FxmlLoader object = new FxmlLoader();
                    Parent view = object.getPane(EdtQuizetPrjt.getValue());
                    EditerProjet controller = (EditerProjet) object.getController();
                    Projet projet = new Projet(idProjet,projectName, projectLocation, selectedType, new Date());

                    if (controller != null) {
                        controller.setProjet(projet);
                        controller.setParentPane(parentPane);
                    }
                    if (parentPane != null) {
                        parentPane.getChildren().removeAll();
                        parentPane.getChildren().setAll(view);
                    }
                } else {
                    locationLabel.setText(ErrCreatingFolder.getValue());
                }
            } else {
                nameLabel.setText(ErrCreatingPrjt.getValue());
            }
        }
    }

    private int insertProjectIntoDatabase(String projectName, String projectLocation, String projectType) {
        String query = "INSERT INTO Projet (nomProjet, localisationProjet, typeProjet) VALUES (?, ?, ?)";
        int generatedId = -1;

        try (Connection connection = MySqlConnection.getOracleConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, projectName);
            preparedStatement.setString(2, projectLocation);
            preparedStatement.setString(3, projectType);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getInt(1);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    private boolean createProjectDirectory(String location, String projectName) {
        java.io.File projectFolder = new java.io.File(location + java.io.File.separator + projectName);

        if (!projectFolder.exists()) {
            boolean created = projectFolder.mkdirs();

            return created;
        }
        return true;
    }

    @FXML
    public void handleClicksCancel(ActionEvent event) {
        FxmlLoader object = new FxmlLoader();
        Parent view = object.getPane(Home.getValue());
        parentPane.getChildren().removeAll();
        parentPane.getChildren().setAll(view);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (TypeProjet type : TypeProjet.values()) {
            MenuItem menuItem = new MenuItem(type.getNomProjet());
            menuItem.setOnAction(event -> typeProject.setText(type.getNomProjet()));
            typeProject.getItems().add(menuItem);
        }
        if (!typeProject.getItems().isEmpty()) {
            typeProject.setText(typeProject.getItems().get(0).getText());
        }

    }

    private boolean findInDatabase() {
        boolean exists = false;
        String projectName = name.getText().trim();
        String query = "SELECT COUNT(*) FROM Projet WHERE nomProjet = ?";
        try (Connection connection = MySqlConnection.getOracleConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, projectName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    exists = resultSet.getInt(1) > 0;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            this.nameLabel.setText(ErrVerfNamePrj.getValue());
            return true;
        }

        if (exists) {
            this.nameLabel.setText(NameAlreadyExists.getValue());
        }
        return exists;
    }

    private boolean verifyProjectName() {
        if (name.getText().trim().isEmpty()) {
            this.nameLabel.setText(EnterNamePrjt.getValue());
            return false;
        }
        if (!name.getText().trim().matches("[a-zA-Z0-9 _-]+")) {  // i let this bec the name of the project works like that
            this.nameLabel.setText(NoSpecCHAR.getValue());
            return false;
        }
        if (!Character.isLetter(name.getText().charAt(0))) {
            this.nameLabel.setText(StartWithLETTER.getValue());
            return false;
        }
        this.nameLabel.setText(espace.getValue());
        return !findInDatabase();
    }

    private boolean verifyLocation() {
        String locationText = location.getText().trim();

        if (locationText.isEmpty()) {
            locationLabel.setText(ChoosePrjtPATH.getValue());
            return false;
        }
        java.io.File directory = new java.io.File(locationText);

        if (!directory.exists() || !directory.isDirectory()) {
            locationLabel.setText( PathNOTFound.getValue());
            return false;
        }
        locationLabel.setText(espace.getValue());
        return true;
    }

    @FXML
    private void handleLocationBrowse(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        directoryChooser.setInitialDirectory(new java.io.File(System.getProperty(userHome.getValue())));

        java.io.File selectedDirectory = directoryChooser.showDialog(((Stage) name.getScene().getWindow()));

        if (selectedDirectory != null) {
            location.setText(selectedDirectory.getAbsolutePath());
        }
    }
}
