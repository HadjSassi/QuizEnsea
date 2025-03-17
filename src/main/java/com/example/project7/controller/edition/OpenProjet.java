package com.example.project7.controller.edition;

import com.example.project7.FxmlLoader;
import com.example.project7.model.Projet;
import com.example.project7.model.TypeProjet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import sql_connection.SqlConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class OpenProjet implements Initializable {

    @FXML
    private TableView<Projet> projectTable;

    @FXML
    private TableColumn<Projet,String> nameCol;

    @FXML
    private TableColumn<Projet,String> localCol;

    @FXML
    private TableColumn<Projet, TypeProjet> typeCol;

    @FXML
    private TableColumn<Projet, Date> dateCol;

    @FXML
    private TableColumn<Projet, Void> actionCol;

    private AnchorPane parentPane;

    public void setParentPane(AnchorPane parentPane) {
        this.parentPane = parentPane;
    }


    @FXML
    public void openProject(ActionEvent event) {
        //get the selected projet from the tableview
        Projet currentProjet = projectTable.getSelectionModel().getSelectedItem();
        if (currentProjet != null) {
            FxmlLoader object = new FxmlLoader();
            Parent view = object.getPane("editer_quiz/_2_EditerProjet");
            EditerProjet controller = (EditerProjet) object.getController();
            if (controller != null) {
                controller.setProjet(currentProjet);
                controller.setParentPane(parentPane);
            }
            if (parentPane != null) {
                parentPane.getChildren().removeAll();
                parentPane.getChildren().setAll(view);
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("You need to select a project first");
            alert.setHeaderText("You need to select a project first to open ");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fetchAndUpdateTableView();
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nomProjet"));
        localCol.setCellValueFactory(new PropertyValueFactory<>("localisationProjet"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("typeProjet"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        actionCol.setCellFactory(col -> new TableCell<Projet, Void>() {
            private final Button supprimerButton = new Button("X");
            {

                supprimerButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                supprimerButton.setOnAction(event -> handleDelete(getIndex()));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(1, supprimerButton);
                    setGraphic(buttons);
                }
            }
        });
    }

    public void fetchAndUpdateTableView() {
        String query = "SELECT * FROM Projet order by creationDate desc";

        ObservableList<Projet> sectionData = FXCollections.observableArrayList();

        try (Connection connection = SqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int idProject = resultSet.getInt("idProjet");
                    String nomProjet = resultSet.getString("nomProjet");
                    String localisationProjet = resultSet.getString("localisationProjet");
                    String typeProjet = resultSet.getString("typeProjet");
                    Date dateProjet = resultSet.getDate("creationDate");


                    sectionData.add(new Projet(idProject, nomProjet, localisationProjet, typeProjet,dateProjet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error loading section data: " + e.getMessage());
        }

        projectTable.setItems(sectionData);
    }

    private void handleDelete(int index){
        Projet project = projectTable.getItems().get(index);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete project : " + project.getIdProjet() + "?");
        alert.setContentText("This action cannot be undone, but the files in "+ project.getLocalisationProjet()+" will remain");

        ButtonType confirm = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType cancel = new ButtonType("No", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(confirm, cancel);

        alert.showAndWait().ifPresent(response -> {
            if (response == confirm) {
                String deleteQuery = "DELETE FROM Projet WHERE idProjet = ?";
                try (Connection connection = SqlConnection.getConnection();
                     PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

                    statement.setInt(1, project.getIdProjet());
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        projectTable.getItems().remove(index);
                    } else {
                        System.err.println("No project found to delete with identifier: " + project.getIdProjet());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("Error deleting project: " + e.getMessage());
                }
            }
        });
    }

}
