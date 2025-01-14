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

            //todo you need to remove the true || in the two next lines, they're just to make thnigs faster!!!!!
            if (true || insertProjectIntoDatabase(projectName, projectLocation, projectType)) {
                if (true || createProjectDirectory(projectLocation, projectName)) {
                    FxmlLoader object = new FxmlLoader();
                    Parent view = object.getPane("editer_quiz/_2_EditerProjet");
                    EditerProjet controller = (EditerProjet) object.getController();
                    Projet projet = new Projet(projectName, projectLocation, selectedType);
                    //todo remove this section it's just for the test !
                    projet.setIdProjet(1);
                    projet.setNomProjet("Test1");
                    projet.setLocalisationProjet("C:\\Users\\mahdi\\Desktop\\ENSEA\\2024-2025\\Project\\Nissrine");
                    projet.setTypeProjet(selectedType);
                    projet.setDate(new Date());
                    //todo the part ends here

                    if (controller != null) {
                        controller.setProjet(projet);
                        controller.setParentPane(parentPane);
                    }
                    if (parentPane != null) {
                        parentPane.getChildren().removeAll();
                        parentPane.getChildren().setAll(view);
                    }
                } else {
                    locationLabel.setText("Error occurred while creating the project folder!");
                }
            } else {
                nameLabel.setText("Error occurred while creating the project!");
            }
        }
    }

    private boolean insertProjectIntoDatabase(String projectName, String projectLocation, String projectType) {
        String query = "INSERT INTO Projet (nomProjet, localisationProjet, typeProjet) VALUES (?, ?, ?)";

        try (Connection connection = MySqlConnection.getOracleConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, projectName);
            preparedStatement.setString(2, projectLocation);
            preparedStatement.setString(3, projectType);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
        Parent view = object.getPane("Home");
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
            this.nameLabel.setText("Database error: Could not verify project name!");
            return true;
        }

        if (exists) {
            this.nameLabel.setText("Your project name already exists!");
        }
        return exists;
    }

    private boolean verifyProjectName() {
        if (name.getText().trim().isEmpty()) {
            this.nameLabel.setText("You need to enter the name of your project!");
            return false;
        }
        if (!Character.isLetter(name.getText().charAt(0))) {
            this.nameLabel.setText("Your project name should start with a letter!");
            return false;
        }
        this.nameLabel.setText("");
        return !findInDatabase();
    }

    private boolean verifyLocation() {
        String locationText = location.getText().trim();

        if (locationText.isEmpty()) {
            locationLabel.setText("You need to choose a project location!");
            return false;
        }
        java.io.File directory = new java.io.File(locationText);

        if (!directory.exists() || !directory.isDirectory()) {
            locationLabel.setText("The selected location is not valid or does not exist.");
            return false;
        }
        locationLabel.setText("");
        return true;
    }

    @FXML
    private void handleLocationBrowse(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        directoryChooser.setInitialDirectory(new java.io.File(System.getProperty("user.home")));

        java.io.File selectedDirectory = directoryChooser.showDialog(((Stage) name.getScene().getWindow()));

        if (selectedDirectory != null) {
            location.setText(selectedDirectory.getAbsolutePath());
        }
    }
}
