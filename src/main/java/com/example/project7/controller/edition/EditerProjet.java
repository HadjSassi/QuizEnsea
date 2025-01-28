package com.example.project7.controller.edition;

import com.example.project7.FxmlLoader;
import com.example.project7.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
import java.util.Date;
import java.util.ResourceBundle;

public class EditerProjet implements Initializable {

    private static Section dernierSection;

    private Projet projet;

    private Controle devoir;

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
    private TableView<RowTableSection> tableSection;

    @FXML
    private TableColumn<RowTableSection, String> numCol;

    @FXML
    private TableColumn<RowTableSection, Integer> typeCol;

    @FXML
    private TableColumn<RowTableSection, Integer> enonceCol;

    @FXML
    private TableColumn<RowTableSection, Void> actionCol;

    private AnchorPane parentPane;

    public void setParentPane(AnchorPane parentPane) {
        this.parentPane = parentPane;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
        nomDevoir.setText(this.projet.getNomProjet());
        this.insertControleData();
        fetchAndUpdateTableView();
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
                devoir.setController(this);
                controller.setDevoir(devoir);

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
        devoir = new Controle();

        String checkControleQuery = "SELECT idControle FROM Controle WHERE projetId = ?";

        try (Connection connection = MySqlConnection.getOracleConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkControleQuery)) {
            checkStatement.setInt(1, projet.getIdProjet());

            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next()) {
                    int idControl = resultSet.getInt("idControle");
                    devoir.setIdControle(idControl);
                } else {
                    String insertControleQuery = "INSERT INTO Controle (nomDevoir, typeDevoir, fontDevoir, fontSize, formatQuestionNumber, formatQuestionTexte, projetId, creationDate) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_DATE)";

                    try (PreparedStatement insertStatement = connection.prepareStatement(insertControleQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        insertStatement.setString(1, nomDevoir.getText());
                        insertStatement.setString(2, typeDevoir.getText());
                        insertStatement.setString(3, "Times New Roman");
                        insertStatement.setInt(4, 15);
                        insertStatement.setInt(5, 1);
                        insertStatement.setString(6, "Question");
                        insertStatement.setInt(7, projet.getIdProjet());

                        int rowsAffected = insertStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    int idControl = generatedKeys.getInt(1);
                                    devoir.setIdControle(idControl);
                                }
                            }
                        } else {
                            System.err.println("Failed to insert Controle data.");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.err.println("Error inserting data into Controle table: " + e.getMessage());
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
        //todo remove these five following lines!
        projet = new Projet("Test1", "C:\\Users\\Hadj Sassi\\Desktop\\ENSEA\\2024-2025\\Project\\Nissrine", TypeProjet.BasicModel);
        projet.setIdProjet(1);
        projet.setDate(new Date());
        nomDevoir.setText(this.projet.getNomProjet());
        this.insertControleData();
        fetchAndUpdateTableView();


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


        numCol.setCellValueFactory(new PropertyValueFactory<>("idSection"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        enonceCol.setCellValueFactory(new PropertyValueFactory<>("question"));

        actionCol.setCellFactory(col -> new TableCell<RowTableSection, Void>() {
            private final Button moveUpButton = new Button("↑");
            private final Button modifierButton = new Button("i");
            private final Button supprimerButton = new Button("X");
            private final Button moveDownButton = new Button("↓");

            {
                moveUpButton.setOnAction(event -> handleMoveUp(getIndex()));

                modifierButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                modifierButton.setOnAction(event -> handleModify(getIndex()));

                supprimerButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                supprimerButton.setOnAction(event -> handleDelete(getIndex()));

                moveDownButton.setOnAction(event -> handleMoveDown(getIndex()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, moveUpButton, modifierButton, supprimerButton, moveDownButton);
                    setGraphic(buttons);
                }
            }
        });


        loadSectionData();
    }

    private void handleMoveUp(int index) {
        RowTableSection section = tableSection.getItems().get(index);
        if (section.getOrdre() > 1) {
            RowTableSection sectionToMoveWith = tableSection.getItems().get(index - 1);
            section.setOrdre(section.getOrdre() - 1);
            sectionToMoveWith.setOrdre(section.getOrdre() + 1);
            updateRowTableSection(section);
            updateRowTableSection(sectionToMoveWith);
            fetchAndUpdateTableView();
        }
    }

    private void updateRowTableSection(RowTableSection section) {
        String updateQuery = "UPDATE section SET ordreSection = ? WHERE idSection = ?";
        try (Connection connection = MySqlConnection.getOracleConnection();
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setInt(1, section.getOrdre());
            statement.setString(2, section.getIdSection());
            int rowsAffected = statement.executeUpdate();
            if (!(rowsAffected > 0)) {
                System.err.println("Failed to update section: " + section.getIdSection());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating section: " + e.getMessage());
        }
    }

    private void handleModify(int index) {
        RowTableSection section = tableSection.getItems().get(index);
        handleModify(section);
    }

    private void handleModify(RowTableSection section) {
        try {
            FxmlLoader object = new FxmlLoader();
            Parent view = object.getPane("editer_quiz/_3_EditerSection");

            Scene popupScene = new Scene(view);
            Stage popupStage = new Stage();

            popupStage.setTitle("Modifier Section QCU");
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.initOwner(terminer.getScene().getWindow());
            popupStage.setScene(popupScene);
            popupStage.setResizable(false);
            popupScene.getStylesheets().add(getClass().getResource("/com/example/project7/css/styles.css").toExternalForm());

            EditerSection controller = (EditerSection) object.getController();
            if (controller != null) {
                controller.setParentPane(parentPane);
                controller.setDevoir(devoir);
                devoir.setController(this);
                controller.loadSectionData(section);
            }

            popupStage.showAndWait();
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ouverture de la popup : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleDelete(int index) {
        RowTableSection section = tableSection.getItems().get(index);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete section : " + section.getIdSection() + "?");
        alert.setContentText("This action cannot be undone.");

        ButtonType confirm = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType cancel = new ButtonType("No", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(confirm, cancel);

        alert.showAndWait().ifPresent(response -> {
            if (response == confirm) {
                String deleteQuery = "DELETE FROM section WHERE idSection = ?";
                try (Connection connection = MySqlConnection.getOracleConnection();
                     PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

                    statement.setString(1, section.getIdSection());
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        tableSection.getItems().remove(index);
                    } else {
                        System.err.println("No section found to delete with idSection: " + section.getIdSection());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("Error deleting section: " + e.getMessage());
                }
            }
        });
    }

    private void handleMoveDown(int index) {
        RowTableSection section = tableSection.getItems().get(index);
        try {
            RowTableSection sectionToMoveWith = tableSection.getItems().get(index + 1);
            section.setOrdre(section.getOrdre() + 1);
            sectionToMoveWith.setOrdre(section.getOrdre() - 1);
            updateRowTableSection(section);
            updateRowTableSection(sectionToMoveWith);
            fetchAndUpdateTableView();
        }catch (IndexOutOfBoundsException e) {

        }

    }

    @FXML
    public void handleClicksSaveProject(ActionEvent event) {
        //todo save in the database and show pop up if the user wants to export it in pdf or latex

    }

    private void loadSectionData() {
        if (this.devoir != null) {
            String query = "SELECT section.idSection, qcm.isQCU, qcm.question AS question, section.ordreSection " +
                    "FROM section " +
                    "JOIN qcm ON section.idSection = qcm.sectionID " +
                    "WHERE section.controleID = ? " + // Filter by controleId
                    "ORDER BY section.ordreSection";

            ObservableList<RowTableSection> sectionData = FXCollections.observableArrayList();

            try (Connection connection = MySqlConnection.getOracleConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, devoir.getIdControle()); // Use the current controle ID

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String idSection = resultSet.getString("idSection");
                        String type = resultSet.getBoolean("isQCU") ? "QCU" : "QCM";
                        String question = resultSet.getString("question");
                        int ordre = resultSet.getInt("ordreSection");
                        sectionData.add(new RowTableSection(idSection, type, question, ordre));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error loading section data: " + e.getMessage());
            }

            tableSection.setItems(sectionData);
        }
    }

    public void fetchAndUpdateTableView() {
        // Define the query to fetch section data from the database
        String query = "SELECT section.idSection, qcm.isQCU, qcm.question AS question, section.ordreSection  " +
                "FROM section " +
                "JOIN qcm ON section.idSection = qcm.sectionID " +
                "WHERE section.controleID = ? " + // Filter by controleId
                "ORDER BY section.ordreSection";

        ObservableList<RowTableSection> sectionData = FXCollections.observableArrayList();

        // Execute the query and load data into the ObservableList
        try (Connection connection = MySqlConnection.getOracleConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, devoir.getIdControle()); // Use the current controle ID

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String idSection = resultSet.getString("idSection");
                    String type = resultSet.getBoolean("isQCU") ? "QCU" : "QCM";
                    String question = resultSet.getString("question");
                    int ordre = resultSet.getInt("ordreSection");
                    sectionData.add(new RowTableSection(idSection, type, question, ordre));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error loading section data: " + e.getMessage());
        }

        // Set the ObservableList to the TableView to refresh the data
        tableSection.setItems(sectionData);
    }

}


