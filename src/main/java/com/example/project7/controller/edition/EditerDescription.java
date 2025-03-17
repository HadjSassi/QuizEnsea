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
import sql_connection.SqlConnection;

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
    private TableColumn<RowTableSection ,Void> witdthCol;

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
            confirmationAlert.setHeaderText("The section already exists");
            confirmationAlert.setContentText("Section with the identifier " + this.section.getIdSection() + " already exists, do you want to overwrite it?");

            ButtonType modifyButton = new ButtonType("Modify");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

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
            ArrayList<Double> imageWidths = new ArrayList<>();
            for (RowTableSection row : tableViewImages.getItems()) {
                imagePaths.add(row.getChemin());
                imageLegends.add(row.getLegend());
                imageWidths.add(row.getWidth());
            }
            description.setImages(imagePaths);
            description.setLegends(imageLegends);
            description.setWidths(imageWidths);
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

        // Width Column with ComboBox
        witdthCol.setCellFactory(col -> new TableCell<RowTableSection, Void>() {
            private final ComboBox<Double> widthComboBox = new ComboBox<>();

            {
                widthComboBox.getItems().addAll(2.0, 1.0, 0.75, 0.5, 0.25);
                widthComboBox.setOnAction(event -> {
                    RowTableSection row = getTableView().getItems().get(getIndex());
                    row.setWidth(widthComboBox.getValue());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    RowTableSection row = getTableView().getItems().get(getIndex());
                    if (row.getWidth() == 0) {
                        row.setWidth(1.0);
                    }
                    widthComboBox.setValue(row.getWidth());
                    setGraphic(widthComboBox);
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

        try (Connection connection = SqlConnection.getConnection();
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

    private void updateDescription() {
        String updateDescriptionQuery = "UPDATE Description SET texte = ? WHERE sectionID = ?";
        String deleteImagesQuery = "DELETE FROM Description_Images WHERE descriptionID = (SELECT idDescription FROM Description WHERE sectionID = ?)";
        String insertImageQuery = "INSERT INTO Description_Images (descriptionID, imagePath, legendText, imageWidth) VALUES (?, ?, ?, ?)";

        try (Connection connection = SqlConnection.getConnection()) {
            connection.setAutoCommit(false);
            // Mise à jour du texte de la description
            try (PreparedStatement updateStmt = connection.prepareStatement(updateDescriptionQuery)) {
                updateStmt.setString(1, descriptionTextArea.getText());
                updateStmt.setString(2, this.section.getIdSection());
                updateStmt.executeUpdate();
            }

            // Suppression des anciennes images
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteImagesQuery)) {
                deleteStmt.setString(1, this.section.getIdSection());
                deleteStmt.executeUpdate();
            }

            // Insertion des nouvelles images
            // On récupère l'idDescription correspondant à cette section
            int descriptionID = 0;
            String fetchDescriptionIdQuery = "SELECT idDescription FROM Description WHERE sectionID = ?";
            try (PreparedStatement fetchStmt = connection.prepareStatement(fetchDescriptionIdQuery)) {
                fetchStmt.setString(1, this.section.getIdSection());
                try (ResultSet rs = fetchStmt.executeQuery()) {
                    if (rs.next()) {
                        descriptionID = rs.getInt("idDescription");
                    }
                }
            }

            try (PreparedStatement insertImageStmt = connection.prepareStatement(insertImageQuery)) {
                for (int i = 0; i < tableViewImages.getItems().size(); i++) {
                    RowTableSection row = tableViewImages.getItems().get(i);
                    insertImageStmt.setInt(1, descriptionID);
                    insertImageStmt.setString(2, row.getChemin());
                    insertImageStmt.setString(3, row.getLegend());
                    insertImageStmt.setDouble(4, row.getWidth());
                    insertImageStmt.addBatch();
                }
                insertImageStmt.executeBatch();
            }

            connection.commit();
            // Vous pouvez rafraîchir la vue ou notifier l'utilisateur ici
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating the description: " + e.getMessage());
        }
    }

    private void updateSection() {
        updateDescription();
        this.section.getDevoir().getController().fetchAndUpdateTableView();
        Stage stage = (Stage) cancelDescription.getScene().getWindow();
        stage.close();
    }

    private void createSection() {
        String insertSectionQuery = "INSERT INTO Section (idSection, ordreSection, controleID) VALUES (?, ?, ?)";

        try (Connection connection = SqlConnection.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertSectionQuery)) {

            insertStatement.setString(1, this.section.getIdSection());
            insertStatement.setInt(2, this.section.getOrdreSection());
            insertStatement.setInt(3, this.section.getDevoir().getIdControle());

            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error during insertion: " + e.getMessage());
        }
    }

    private void createDescription() {
        String insertDescriptionQuery = "INSERT INTO Description (texte, sectionID) VALUES (?, ?)";
        String insertImageQuery = "INSERT INTO Description_Images (descriptionID, imagePath,legendText,imageWidth) VALUES (?, ?, ?, ?)";

        try (Connection connection = SqlConnection.getConnection()) {
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
                                for (int i = 0 ; i < description.getImages().size(); i++) {
                                    insertImageStmt.setInt(1, descriptionID);
                                    insertImageStmt.setString(2, description.getImages().get(i));
                                    insertImageStmt.setString(3, description.getLegends().get(i));
                                    insertImageStmt.setDouble(4, description.getWidths().get(i));
                                    insertImageStmt.addBatch();
                                }
                                insertImageStmt.executeBatch();
                            }


                            connection.commit();
                        }
                    }
                } else {
                    System.err.println("Failed to insert the description");
                    connection.rollback();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error during insertion: " + e.getMessage());
        }
    }

    private void loadFieldFromSectionId(String idSection) {
        String fetchQCUQuery = "SELECT * FROM Description WHERE sectionID = ?";
        String fetchImagesQuery = "SELECT imagePath,legendText,imageWidth FROM Description_Images WHERE descriptionID = ?";
        int idDescription = 0;
        try (Connection connection = SqlConnection.getConnection();
             PreparedStatement qcuStatement = connection.prepareStatement(fetchQCUQuery)){

            qcuStatement.setString(1, idSection);
            ResultSet resultSet = qcuStatement.executeQuery();
            if (resultSet.next()) {
                description = new Description();
                description.setIdSection(resultSet.getString("sectionID"));
                description.setTexte(resultSet.getString("texte"));
                idDescription = resultSet.getInt("idDescription");
                descriptionTextArea.setText(description.getTexte());
                ArrayList<String> images = new ArrayList<>();
                ArrayList<String> legends = new ArrayList<>();
                ArrayList<Double> widths = new ArrayList<>();
                try (PreparedStatement imageStatement = connection.prepareStatement(fetchImagesQuery)) {
                    imageStatement.setInt(1, idDescription);
                    ResultSet imageResultSet = imageStatement.executeQuery();
                    while (imageResultSet.next()) {
                        String imagePath = imageResultSet.getString("imagePath");
                        String imageLegend = imageResultSet.getString("legendText");
                        Double imageWidth = imageResultSet.getDouble("imageWidth");
                        images.add(imagePath);
                        legends.add(imageLegend);
                        widths.add(imageWidth);
                    }
                }
                description.setImages(images);
                description.setLegends(legends);
                description.setWidths(widths);

                ObservableList<RowTableSection> tableData = FXCollections.observableArrayList();
                for (int i = 0; i < images.size(); i++) {
                    String imagePath = images.get(i);
                    String legend = (i < legends.size()) ? legends.get(i) : "";
                    double imageWidth = (i < widths.size()) ? widths.get(i) : 1.0f;
                    tableData.add(new RowTableSection(imagePath, legend, 0,imageWidth));
                }
                tableViewImages.setItems(tableData);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement des données QCU: " + e.getMessage());
        }
    }

    private void handleDelete(int index) {
        tableViewImages.getItems().remove(index);
    }
}
