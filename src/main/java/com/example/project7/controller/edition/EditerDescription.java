package com.example.project7.controller.edition;

import com.example.project7.model.Description;
import com.example.project7.model.RowTableSection;
import com.example.project7.model.Section;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mysql_connection.MySqlConnection;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EditerDescription implements Initializable {


    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TableView<RowTableSection> tableViewImages;

    @FXML
    private TableColumn<RowTableSection,String> cheminCol;

    @FXML
    private TableColumn<RowTableSection ,String> legendCol;

    @FXML
    private TableColumn<RowTableSection ,Void> actionCol;

    @FXML
    private Button cancelDescription;

    private Section section;

    private Description description;

    public void setSection(Section identifierSection) {
        this.section = identifierSection;
    }

    @FXML
    public void handleClickAddImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String fullPath = selectedFile.getAbsolutePath();
            String fileName = selectedFile.getName();

            int lastDotIndex = fileName.lastIndexOf(".");
            String legend = (lastDotIndex != -1) ? fileName.substring(0, lastDotIndex) : fileName;

            RowTableSection newRow = new RowTableSection(fullPath, legend,0);
            tableViewImages.getItems().add(newRow);
        }
    }

    @FXML
    public void handleClickAddDescription(ActionEvent event) {
        if (checkSectionExists(this.section.getIdSection())) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Section Exists");
            confirmationAlert.setHeaderText("La section existe déjà");
            confirmationAlert.setContentText("Section avec l'identifiant " + this.section.getIdSection() + " existe déjà, voulez vous l'écraser?");

            ButtonType modifyButton = new ButtonType("Modifier");
            ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

            confirmationAlert.getButtonTypes().setAll(modifyButton, cancelButton);

            // Handle user response
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == modifyButton) {
                    updateSection();
                }
            });
        } else {

            description.setTexte(descriptionTextArea.getText());
            ArrayList<String> imagePaths = new ArrayList<>();
            ArrayList<String> imageLegends = new ArrayList<>();
            for (RowTableSection row : tableViewImages.getItems()) {
                imagePaths.add(row.getChemin());
                imageLegends.add(row.getLegend());
            }
            description.setImages(imagePaths);
            description.setLegends(imageLegends);
            createSection();
            createDescription();
            this.section.getDevoir().getController().fetchAndUpdateTableView();
            Stage stage = (Stage) cancelDescription.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    public void handleClickCancelDescription(ActionEvent event) {
        EditerSection.cancelSection();
        Stage stage = (Stage) cancelDescription.getScene().getWindow();
        stage.close();    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.description = new Description();
        cheminCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getChemin()));
        cheminCol.setCellFactory(TextFieldTableCell.forTableColumn());
        cheminCol.setOnEditCommit(event -> {
            RowTableSection row = event.getRowValue();
            row.setChemin(event.getNewValue());
        });


        legendCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLegend()));
        legendCol.setCellFactory(TextFieldTableCell.forTableColumn());
        legendCol.setOnEditCommit(event -> {
            RowTableSection row = event.getRowValue();
            row.setLegend(event.getNewValue());
        });

        actionCol.setCellFactory(col -> new TableCell<RowTableSection, Void>() {
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
                    HBox buttons = new HBox(5, supprimerButton);
                    setGraphic(buttons);
                }
            }
        });


        tableViewImages.setItems(FXCollections.observableArrayList());
    }

    public void setSectionUpdating(Section section) {
        this.section = section;
        loadFieldFromSectionId(section.getIdSection());
    }

    private boolean checkSectionExists(String idSection) {
        String checkQuery = "SELECT COUNT(*) FROM Section WHERE idSection = ?";

        try (Connection connection = MySqlConnection.getOracleConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {

            checkStatement.setString(1, idSection);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Return true if the section exists
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error checking section existence: " + e.getMessage());
        }

        return false;
    }

    private void removeSection() {
        String deleteQuery = "DELETE FROM section WHERE idSection = ?";
        try (Connection connection = MySqlConnection.getOracleConnection();
             PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

            statement.setString(1, section.getIdSection());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error deleting section: " + e.getMessage());
        }
    }

    private void updateSection() {
        removeSection();
        handleClickAddDescription(null);
    }

    private void createSection() {
        String insertSectionQuery = "INSERT INTO Section (idSection, ordreSection, controleID) VALUES (?, ?, ?)";

        try (Connection connection = MySqlConnection.getOracleConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertSectionQuery)) {

            insertStatement.setString(1, this.section.getIdSection());
            insertStatement.setInt(2, this.section.getOrdreSection());
            insertStatement.setInt(3, this.section.getDevoir().getIdControle());

            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error inserting Section data: " + e.getMessage());
        }
    }

    private void createDescription() {
        String insertDescriptionQuery = "INSERT INTO description (texte, controleID) VALUES (?, ?)";
        String insertImageQuery = "INSERT INTO Description_Images (descriptionID, imagePath) VALUES (?, ?)";
        String insertLegendQuery = "INSERT INTO Description_Legends (descriptionID, legendText) VALUES (?, ?)";

        try (Connection connection = MySqlConnection.getOracleConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement insertDescriptionStmt = connection.prepareStatement(insertDescriptionQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                insertDescriptionStmt.setString(1, description.getTexte());
                insertDescriptionStmt.setString(2, this.section.getIdSection());

                int rowsAffected = insertDescriptionStmt.executeUpdate();
                if (rowsAffected > 0) {
                    try (ResultSet generatedKeys = insertDescriptionStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int descriptionID = generatedKeys.getInt(1);
                            description.setIdSection(String.valueOf(descriptionID));

                            try (PreparedStatement insertImageStmt = connection.prepareStatement(insertImageQuery)) {
                                for (String image : description.getImages()) {
                                    insertImageStmt.setInt(1, descriptionID);
                                    insertImageStmt.setString(2, image);
                                    insertImageStmt.addBatch();
                                }
                                insertImageStmt.executeBatch();
                            }

                            try (PreparedStatement insertLegendStmt = connection.prepareStatement(insertLegendQuery)) {
                                for (String legend : description.getLegends()) {
                                    insertLegendStmt.setInt(1, descriptionID);
                                    insertLegendStmt.setString(2, legend);
                                    insertLegendStmt.addBatch();
                                }
                                insertLegendStmt.executeBatch();
                            }

                            connection.commit();
                        }
                    }
                } else {
                    System.err.println("Échec de l'insertion de la description.");
                    connection.rollback();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'insertion : " + e.getMessage());
        }
    }

    private void loadFieldFromSectionId(String idSection) {
        String fetchQCUQuery = "SELECT * FROM description WHERE controleID = ?";
        String fetchImagesQuery = "SELECT imagePath FROM Description_Images WHERE descriptionID = ?";
        String fetchLegendsQuery = "SELECT legendText FROM Description_Legends WHERE descriptionID = ?";
        int idDescription = 0;
        try (Connection connection = MySqlConnection.getOracleConnection();
             PreparedStatement qcuStatement = connection.prepareStatement(fetchQCUQuery)){

            qcuStatement.setString(1, idSection);
            ResultSet resultSet = qcuStatement.executeQuery();
            if (resultSet.next()) {
                description = new Description();
                description.setIdSection(resultSet.getString("controleID"));
                description.setTexte(resultSet.getString("texte"));
                idDescription = resultSet.getInt("idDescription");
                descriptionTextArea.setText(description.getTexte());
                ArrayList<String> images = new ArrayList<>();
                try (PreparedStatement imageStatement = connection.prepareStatement(fetchImagesQuery)) {
                    imageStatement.setInt(1, idDescription);
                    ResultSet imageResultSet = imageStatement.executeQuery();
                    while (imageResultSet.next()) {
                        String imagePath = imageResultSet.getString("imagePath");
                        images.add(imagePath);
                    }
                }
                description.setImages(images);
                ArrayList<String> legends = new ArrayList<>();
                try (PreparedStatement legendStatement = connection.prepareStatement(fetchLegendsQuery)) {
                    legendStatement.setInt(1, idDescription);
                    ResultSet legendResultSet = legendStatement.executeQuery();
                    while (legendResultSet.next()) {
                        legends.add(legendResultSet.getString("legendText"));
                    }
                }
                description.setLegends(legends);
                ObservableList<RowTableSection> tableData = FXCollections.observableArrayList();
                for (int i = 0; i < images.size(); i++) {
                    String imagePath = images.get(i);
                    String legend = (i < legends.size()) ? legends.get(i) : "";
                    tableData.add(new RowTableSection(imagePath, legend, 0));
                }
                tableViewImages.setItems(tableData);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error loading QCU data: " + e.getMessage());
        }
    }

    private void handleDelete(int index) {
        tableViewImages.getItems().remove(index);
    }
}
