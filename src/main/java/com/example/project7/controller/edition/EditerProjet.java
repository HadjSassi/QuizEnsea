package com.example.project7.controller.edition;

import com.example.project7.FxmlLoader;
import com.example.project7.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sql_connection.SqlConnection;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.ArrayList;


public class EditerProjet implements Initializable {

    private static Section dernierSection;

    private Projet projet;

    private Controle devoir;

    @FXML
    private Button terminer;

    @FXML
    private TextField nomDevoir;

    @FXML
    private TextArea examHeader;

    @FXML
    private TextField randomSeed;

    @FXML
    private TextArea reponseHeader;

    @FXML
    private MenuButton typeDevoir;

    @FXML
    private DatePicker dateDevoir;

    @FXML
    private TextField nombreExemplaire;

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
    public void handleInputNumber(KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        String currentText = textField.getText();

        String sanitizedText = currentText.replaceAll("[^\\d]", "");


        textField.setText(sanitizedText);
        textField.positionCaret(sanitizedText.length());
    }

    @FXML
    public void modifyExamHeader(ActionEvent events) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UTILITY);

        VBox popupVBox = new VBox(10);
        popupVBox.setPadding(new Insets(20));

        TextArea responseTextArea = new TextArea(examHeader.getText());
        responseTextArea.setWrapText(true);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveButton = new Button("Modify");
        saveButton.setOnAction(event -> {
            examHeader.setText(responseTextArea.getText());

            popupStage.close();
        });

        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> {
            popupStage.close();
        });

        buttonBox.getChildren().addAll(saveButton, closeButton);

        popupVBox.getChildren().addAll(responseTextArea, buttonBox);

        Scene popupScene = new Scene(popupVBox, 350, 250);
        popupStage.setScene(popupScene);
        popupStage.setTitle("Edit the exam header");
        popupStage.show();
    }

    @FXML
    public void modifyReponseHeader(ActionEvent events) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UTILITY);

        VBox popupVBox = new VBox(10);
        popupVBox.setPadding(new Insets(20));

        TextArea responseTextArea = new TextArea(reponseHeader.getText());
        responseTextArea.setWrapText(true);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveButton = new Button("Modify");
        saveButton.setOnAction(event -> {
            reponseHeader.setText(responseTextArea.getText());

            popupStage.close();
        });

        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> {
            popupStage.close();
        });

        buttonBox.getChildren().addAll(saveButton, closeButton);

        popupVBox.getChildren().addAll(responseTextArea, buttonBox);

        Scene popupScene = new Scene(popupVBox, 350, 250);
        popupStage.setScene(popupScene);
        popupStage.setTitle("Edit Response Header");
        popupStage.show();
    }

    @FXML
    public void handleClicksAddSection(ActionEvent event) {
        try {
            FxmlLoader object = new FxmlLoader();
            Parent view = object.getPane("editer_quiz/_3_EditerSection");

            Scene popupScene = new Scene(view);
            Stage popupStage = new Stage();

            popupStage.setTitle("Add Section");
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
            e.printStackTrace();
        }
    }

    @FXML
    public void handleClicksImportSection(ActionEvent event) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Select a Section");

        // TableView setup
        TableView<SectionRow> tableView = new TableView<>();
        TableColumn<SectionRow, Integer> idColumn = new TableColumn<>("ID");
        TableColumn<SectionRow, String> nameColumn = new TableColumn<>("Section Type");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        tableView.getColumns().addAll(idColumn, nameColumn);

        // Load data from the database
        tableView.setItems(getSectionsFromDatabase());

        // Select Button
        Button selectButton = new Button("Select");
        selectButton.setOnAction(e -> insertImportedSection(modalStage, tableView));

        // Layout
        VBox vbox = new VBox(10, tableView, selectButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));

        // Scene
        Scene scene = new Scene(vbox, 400, 300);
        modalStage.setScene(scene);
        modalStage.showAndWait();
    }

    private void insertImportedSection(Stage modalStage, TableView<SectionRow> tableView) {
        SectionRow selectedSection = tableView.getSelectionModel().getSelectedItem();
        if (selectedSection != null) {
            processInsertImportedSection(selectedSection);
            modalStage.close();
        }
    }

    private void processInsertImportedSection(SectionRow sectionRow) {
        String originalId = sectionRow.getId();
        String baseId = originalId;
        int pos = originalId.indexOf("__");
        if (pos != -1) {
            // On garde la partie avant "__"
            baseId = originalId.substring(0, pos);
        }
        String newSectionId = baseId + "__" + System.currentTimeMillis();
        String sectionType = sectionRow.getType();
        int newSectionOrdre = 0;
        try (Connection conn = SqlConnection.getConnection()) {
            String countQuery = "SELECT COUNT(*) as cnt FROM Section WHERE controleID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(countQuery)) {
                stmt.setInt(1, this.devoir.getIdControle());
                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        newSectionOrdre = resultSet.getInt("cnt") + 1;
                    }
                    String insertQuery = "INSERT INTO Section (idSection, controleID, ordreSection)" +
                            " values (?,?,?)";
                    try (PreparedStatement stmt2 = conn.prepareStatement(insertQuery)) {
                        stmt2.setString(1, newSectionId);
                        stmt2.setInt(2, this.devoir.getIdControle());
                        stmt2.setInt(3, newSectionOrdre);
                        stmt2.executeUpdate();
                    }
                }
            }
            if ("QCU".equals(sectionType) || "QCM".equals(sectionType)) {
                int newQcmID = -1;
                String insertQcmQuery = "INSERT INTO QCM (sectionID, isQCU, question) " +
                        "SELECT ?, isQCU, question FROM QCM WHERE sectionID = ?";

                try (PreparedStatement stmt = conn.prepareStatement(insertQcmQuery, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, newSectionId);
                    stmt.setString(2, sectionRow.getId());
                    stmt.executeUpdate();
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            newQcmID = generatedKeys.getInt(1);
                        }
                    }
                }

                String insertQcmResponsesQuery = "INSERT INTO QCM_Reponses (qcmID, reponse, score, isCorrect) " +
                        "SELECT ?, reponse, score, isCorrect FROM QCM_Reponses WHERE qcmID = ?";

                try (PreparedStatement stmt = conn.prepareStatement(insertQcmResponsesQuery)) {
                    stmt.setInt(1, newQcmID);
                    stmt.setInt(2, getOldQcmID(sectionRow.getId()));
                    stmt.executeUpdate();
                }

            } else if ("QuestionLibre".equals(sectionType)) {
                String insertQuestionQuery = "INSERT INTO QuestionLibre (sectionID, question, scoreTotal, nombreScore," +
                        "nombreLigne, tailleLigne,rappel) " +
                        "SELECT ?,question, scoreTotal, nombreScore,nombreLigne, tailleLigne,rappel " +
                        "FROM QuestionLibre WHERE sectionID = ?";

                try (PreparedStatement stmt = conn.prepareStatement(insertQuestionQuery)) {
                    stmt.setString(1, newSectionId);
                    stmt.setString(2, sectionRow.getId());
                    stmt.executeUpdate();
                }
            } else if ("Description".equals(sectionType)) {
                int newDescriptionId = -1;

                String insertDescriptionQuery = "INSERT INTO Description (sectionID, texte) " +
                        "SELECT ?, texte FROM Description WHERE sectionID = ?";

                try (PreparedStatement stmt = conn.prepareStatement(insertDescriptionQuery, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, newSectionId);
                    stmt.setString(2, sectionRow.getId());
                    stmt.executeUpdate();

                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            newDescriptionId = generatedKeys.getInt(1);
                        }
                    }

                    if (newDescriptionId != -1) {
                        String insertDescriptionImagesQuery = "INSERT INTO Description_Images (descriptionID, imagePath, legendText, imageWidth) " +
                                "SELECT ?, imagePath, legendText, imageWidth FROM Description_Images WHERE descriptionID = ?";

                        try (PreparedStatement stmt2 = conn.prepareStatement(insertDescriptionImagesQuery)) {
                            stmt2.setInt(1, newDescriptionId);
                            stmt2.setInt(2, getOldDescriptionID(sectionRow.getId()));
                            stmt2.executeUpdate();
                        }
                    }
                }
            }
            fetchAndUpdateTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<SectionRow> getSectionsFromDatabase() {
        ObservableList<SectionRow> sections = FXCollections.observableArrayList();
        String query = "SELECT idSection, " +
                "CASE WHEN QCM.isQCU IS NOT NULL THEN (CASE WHEN QCM.isQCU = 1 THEN 'QCU' ELSE 'QCM' END) " +
                "WHEN QuestionLibre.sectionID IS NOT NULL THEN 'QuestionLibre' " +
                "WHEN Description.sectionID IS NOT NULL THEN 'Description' " +
                "ELSE 'Unknown' END AS type " +
                "FROM Section " +
                "LEFT JOIN QCM ON Section.idSection = QCM.sectionID " +
                "LEFT JOIN QuestionLibre ON Section.idSection = QuestionLibre.sectionID " +
                "LEFT JOIN Description ON Section.idSection = Description.sectionID ";

        try (Connection conn = SqlConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                sections.add(new SectionRow(rs.getString("idSection"), rs.getString("type")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sections;
    }

    private int getOldQcmID(String sectionId) {
        String query = "SELECT idQCM FROM QCM WHERE sectionID = ?";
        try (Connection conn = SqlConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, sectionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idQCM");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return an invalid ID if not found
    }

    private int getOldDescriptionID(String sectionId) {
        String query = "SELECT idDescription FROM Description WHERE sectionID = ?";
        try (Connection conn = SqlConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, sectionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idDescription");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return invalid ID if not found
    }

    @FXML
    public void handleClicksCancelProject(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Are you sure to cancel the modifications ?");
        alert.setContentText("All modifications will be lost!");

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");

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

        String checkControleQuery = "SELECT idControle, nomDevoir, typeDevoir, nombreExemplaire, randomSeed, examHeader, reponseHeader, creationDate FROM Controle WHERE projetID = ?";

        try (Connection connection = SqlConnection.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkControleQuery)) {
            checkStatement.setInt(1, projet.getIdProjet());

            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next()) {
                    devoir.setIdControle(resultSet.getInt("idControle"));
                    devoir.setNomDevoir(resultSet.getString("nomDevoir"));
                    devoir.setTypeDevoir(resultSet.getString("typeDevoir"));
                    devoir.setNombreExemplaire(resultSet.getInt("nombreExemplaire"));
                    devoir.setRandomSeed(resultSet.getInt("randomSeed"));
                    devoir.setExamHeader(resultSet.getString("examHeader"));
                    devoir.setReponseHeader(resultSet.getString("reponseHeader"));
                    devoir.setCreationDate(resultSet.getDate("creationDate"));

                    this.nomDevoir.setText(devoir.getNomDevoir());
                    this.typeDevoir.setText(devoir.getTypeDevoir());
                    this.nombreExemplaire.setText(String.valueOf(devoir.getNombreExemplaire()));
                    this.randomSeed.setText(String.valueOf(devoir.getRandomSeed()));
                    this.examHeader.setText(devoir.getExamHeader());
                    this.reponseHeader.setText(devoir.getReponseHeader());
                    this.dateDevoir.setValue(devoir.getCreationDate().toLocalDate());

                } else {
                    intializeFirstValues();
                    String insertControleQuery = "INSERT INTO Controle (nomDevoir, typeDevoir, examHeader, reponseHeader, projetID, creationDate) " +
                            "VALUES (?, ?, ?, ?, ?, CURRENT_DATE)";

                    try (PreparedStatement insertStatement = connection.prepareStatement(insertControleQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        insertStatement.setString(1, nomDevoir.getText());
                        insertStatement.setString(2, typeDevoir.getText());
                        insertStatement.setString(3, examHeader.getText());
                        insertStatement.setString(4, reponseHeader.getText());
                        insertStatement.setInt(5, projet.getIdProjet());

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
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateDevoir.setValue(LocalDate.now());

        for (TypeDevoir type : TypeDevoir.values()) {
            MenuItem menuItem = new MenuItem(type.getNomDevoir());
            menuItem.setOnAction(event -> typeDevoir.setText(type.getNomDevoir()));
            typeDevoir.getItems().add(menuItem);
        }

        if (!typeDevoir.getItems().isEmpty()) {
            typeDevoir.setText(typeDevoir.getItems().get(0).getText());
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

        examHeader.setWrapText(true);
        reponseHeader.setWrapText(true);
        loadSectionData();
    }

    private void intializeFirstValues() {
        String examHeaderText =
                "Dans ce document, vous trouverez d'abord les questions puis ensuite les feuilles de réponses (à rendre). " +
                        "In this document, you will first find the questions then the pages for the answers.\n" +
                        "\t\t\t \n" +
                        "Les questions faisant apparaître le symbole :¨: peuvent présenter zéro, une ou plusieurs bonnes réponses. " +
                        "Les autres ont une unique bonne réponse. Des points négatifs sont affectés aux mauvaises réponses. " +
                        "La pondération des mauvaises réponses est nulle pour les premières fausses réponses mais ensuite la pondération (négative) " +
                        "des mauvaises réponses augmente avec le nombre de mauvaises réponses.\n" +
                        "\t\t\t \n" +
                        "Questions with the symbol :¨: may have zero, one or more correct answers. The others have a single correct answer. " +
                        "Negative points are assigned for wrong answers. The weight of wrong answers is zero for the first few wrong answers, " +
                        "but then the (negative) weight of wrong answers increases with the number of wrong answers.\n" +
                        "\t\t\t \n" +
                        "Un document ressource est distribué en plus de ce document. Vous devez utiliser les informations de ce document en priorité. " +
                        "A resource document is distributed in addition to this document. You should use the information in this document as a priority.\n" +
                        "\t\t\t ";

        String reponseHeaderText =
                "2 feuilles (4 pages) à détacher : seuls documents à rendre pour la partie Microprocesseur de cet examen. \n" +
                        "\t\t\t2 detachable sheets (4 pages): only documents to be returned for the Microprocessor exam.";

        this.examHeader.setText(examHeaderText);
        this.reponseHeader.setText(reponseHeaderText);
        this.randomSeed.setText("12345678");
        this.nombreExemplaire.setText("1");
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
        String updateQuery = "UPDATE Section SET ordreSection = ? WHERE idSection = ?";
        try (Connection connection = SqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setInt(1, section.getOrdre());
            statement.setString(2, section.getIdSection());
            int rowsAffected = statement.executeUpdate();
            if (!(rowsAffected > 0)) {
                System.err.println("Failed to update section: " + section.getIdSection());
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

            popupStage.setTitle("Modify QCU Section");
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
                String deleteQuery = "DELETE FROM Section WHERE idSection = ?";
                try (Connection connection = SqlConnection.getConnection();
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
        } catch (IndexOutOfBoundsException e) {

        }

    }

    private void loadSectionData() {
        if (this.devoir != null) {
            String query = "SELECT Section.idSection, QCM.isQCU, QCM.question AS question, Section.ordreSection AS ordreSection, 'QCU/QCM' AS type " +
                    "FROM Section " +
                    "JOIN QCM ON Section.idSection = QCM.sectionID " +
                    "WHERE Section.controleID = ? " +
                    "UNION " +
                    "SELECT Section.idSection, NULL AS isQCU, QuestionLibre.question AS question, Section.ordreSection AS ordreSection, 'QuestionLibre' AS type " +
                    "FROM Section " +
                    "JOIN QuestionLibre ON Section.idSection = QuestionLibre.sectionID " +
                    "WHERE Section.controleID = ? " +
                    "UNION " +
                    "SELECT Section.idSection, NULL AS isQCU, Section.idSection AS question, Section.ordreSection AS ordreSection, 'Description' AS type " +
                    "FROM Section " +
                    "JOIN Description ON Section.idSection = Description.sectionID " +
                    "WHERE Section.controleID = ? " +
                    "ORDER BY ordreSection";


            ObservableList<RowTableSection> sectionData = FXCollections.observableArrayList();

            try (Connection connection = SqlConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, devoir.getIdControle());
                statement.setInt(2, devoir.getIdControle());
                statement.setInt(3, devoir.getIdControle());

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String idSection = resultSet.getString("idSection");
                        String type = resultSet.getString("type");

                        // Determine the correct type
                        if (type.equals("QuestionLibre")) {
                            type = "QuestionLibre";
                        } else if (type.equals("QCU/QCM")) {
                            type = resultSet.getBoolean("isQCU") ? "QCU" : "QCM";
                        } else {
                            type = "Description"; // For descriptions
                        }

                        String question = resultSet.getString("question");
                        int ordre = resultSet.getInt("ordreSection");

                        sectionData.add(new RowTableSection(idSection, type, question, ordre));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            tableSection.setItems(sectionData);
        }
    }

    public void fetchAndUpdateTableView() {
        String query = "SELECT Section.idSection, QCM.isQCU, QCM.question AS question, Section.ordreSection AS ordreSection, 'QCU/QCM' AS type " +
                "FROM Section " +
                "JOIN QCM ON Section.idSection = QCM.sectionID " +
                "WHERE Section.controleID = ? " +
                "UNION " +
                "SELECT Section.idSection, NULL AS isQCU, QuestionLibre.question AS question, Section.ordreSection AS ordreSection, 'QuestionLibre' AS type " +
                "FROM Section " +
                "JOIN QuestionLibre ON Section.idSection = QuestionLibre.sectionID " +
                "WHERE Section.controleID = ? " +
                "UNION " +
                "SELECT Section.idSection, NULL AS isQCU, Section.idSection AS question, Section.ordreSection AS ordreSection, 'Description' AS type " +
                "FROM Section " +
                "JOIN Description ON Section.idSection = Description.sectionID " +
                "WHERE Section.controleID = ? " +
                "ORDER BY ordreSection";


        ObservableList<RowTableSection> sectionData = FXCollections.observableArrayList();

        // Execute the query and load data into the ObservableList
        try (Connection connection = SqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, devoir.getIdControle());
            statement.setInt(2, devoir.getIdControle());
            statement.setInt(3, devoir.getIdControle());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String idSection = resultSet.getString("idSection");
                    String type = resultSet.getString("type");

                    // Determine the correct type
                    if (type.equals("QuestionLibre")) {
                        type = "QuestionLibre";
                    } else if (type.equals("QCU/QCM")) {
                        type = resultSet.getBoolean("isQCU") ? "QCU" : "QCM";
                    } else {
                        type = "Description"; // For descriptions
                    }

                    String question = resultSet.getString("question");
                    int ordre = resultSet.getInt("ordreSection");

                    sectionData.add(new RowTableSection(idSection, type, question, ordre));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set the ObservableList to the TableView to refresh the data
        tableSection.setItems(sectionData);
    }

    private String generateHeader() {
        StringBuilder header = new StringBuilder();
        header.append("\\documentclass[a4paper]{article}\n");
        header.append("\\usepackage[utf8x]{inputenc}\n");
        header.append("\\usepackage[T1]{fontenc}\n");
        header.append("\\usepackage{graphics}\n");
        header.append("\\usepackage{float}\n");
        header.append("\\usepackage[francais,bloc,completemulti,ensemble]{automultiplechoice}\n");
        header.append("\\begin{document}\n");
        header.append("\t\\AMCrandomseed{" + randomSeed.getText().trim() + "}\n");
        header.append("\t\\def\\AMCformQuestion#1{{\\sc Question #1 :}}\n");
        header.append("\t\\setdefaultgroupmode{fixed}\n");
        return header.toString();
    }

    private String generateFooter() {
        StringBuilder footer = new StringBuilder();
        footer.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        footer.append("\n");
        footer.append("\t\\exemplaire{").append(formatLatex(nombreExemplaire.getText().trim())).append("}{\n");
        footer.append("\t\t\n");
        footer.append("\t\t\\noindent{\\large\\bf QUESTIONS  \\hfill ").append(formatLatex(typeDevoir.getText())).append(" of ").append(dateDevoir.getValue().format(DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy"))).append("}\n");
        footer.append("\t\t\n");
        footer.append("\t\t\\vspace*{.5cm}\n");
        footer.append("\t\t\\begin{minipage}{.4\\linewidth}\n");
        footer.append("\t\t\t\\centering\\large\\bf ").append(formatLatex(nomDevoir.getText().trim())).append(" \n");
        footer.append("\t\t\\end{minipage}\n");
        footer.append("\t\t\n");
        footer.append("\t\t\\begin{center}\\em\n");
        footer.append("\t\t\t\n");
        footer.append("\t\t\t").append(formatLatex(examHeader.getText())).append("\n");
        footer.append("\t\t\t\n");
        footer.append("\t\t\\end{center}\n");
        footer.append("\t\t\\vspace{1ex}\n");
        footer.append("\t\t\n");
        footer.append("\t\t\n");
        footer.append("\t\t\\restituegroupe{general}\n");
        footer.append("\t\t\n");
        footer.append("\t\t\\AMCcleardoublepage    \n");
        footer.append("\t\t\n");
        footer.append("\t\t\n");
        footer.append("\t\t\\AMCdebutFormulaire    \n");
        footer.append("\t\t\n");
        footer.append("\t\t\n");
        footer.append("\t\t{\\large\\bf ").append(formatLatex(nomDevoir.getText().trim())).append(" RESPONSES ").append(dateDevoir.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("} \n");
        footer.append("\t\t\\newline\n");
        footer.append("\t\t\\hfill \\champnom{\\fbox{    \n");
        footer.append("\t\t\t\t\\begin{minipage}{.5\\linewidth}\n");
        footer.append("\t\t\t\t\tNOM/NAME  Prénom/First name :\n");
        footer.append("\t\t\t\t\t\n");
        footer.append("\t\t\t\t\t\\vspace*{.5cm}\\dotfill\n");
        footer.append("\t\t\t\t\t\\vspace*{1mm}\n");
        footer.append("\t\t\t\t\\end{minipage}\n");
        footer.append("\t\t}}\n");
        footer.append("\t\t\\newline\n");
        footer.append("\t\t\n");
        footer.append("\t\tMerci de coder votre numéro d'étudiant à 5 chiffres en noircissant bien les cases:\n");
        footer.append("\t\t\\newline\n");
        footer.append("\t\tPlease code your 5-digit student number by filling in the boxes in black:\n");
        footer.append("\t\t\\newline\n");
        footer.append("\t\t\\AMCcodeHspace=2.5em\n");
        footer.append("\t\t\\AMCcodeGridInt[vertical=false]{etu}{5}\n");
        footer.append("\t\t\n");
        footer.append("\t\t\\begin{center}\n");
        footer.append("\t\t\t").append(formatLatex(reponseHeader.getText())).append("\n");
        footer.append("\t\t\\end{center}\n");
        footer.append("\t\t\n");
        footer.append("\t\t\n");
        footer.append("\t\t\\formulaire\n");
        footer.append("\t\t\n");
        footer.append("\t\t\\begin{center}\n");
        footer.append("\t\t\t\\bf\\em \n");
        footer.append("\t\t\\end{center} \n");
        footer.append("\t}\n");
        footer.append("\n");
        footer.append("\\end{document}\n");
        return footer.toString();
    }

    private void processQCM(Connection conn, String idSection, String question, String idSectionLatex, StringBuilder texcontentBuilder) throws SQLException {
        class ResponseLatex {
            String reponse;
            int score;
            boolean isCorrect;

            ResponseLatex(String reponse, int score, boolean isCorrect) {
                this.reponse = reponse;
                this.score = score;
                this.isCorrect = isCorrect;
            }
        }

        PreparedStatement psQcm = conn.prepareStatement("SELECT idQCM FROM QCM WHERE sectionID = ?");
        psQcm.setString(1, idSection);
        ResultSet rsQcm = psQcm.executeQuery();
        if (rsQcm.next()) {
            int qcmID = rsQcm.getInt("idQCM");
            rsQcm.close();
            psQcm.close();

            PreparedStatement psResponses = conn.prepareStatement("SELECT reponse, score, isCorrect FROM QCM_Reponses WHERE qcmID = ?");
            psResponses.setInt(1, qcmID);
            ResultSet rsResponses = psResponses.executeQuery();

            List<ResponseLatex> responseLatexList = new ArrayList<>();
            while (rsResponses.next()) {
                String rep = rsResponses.getString("reponse");
                int score = rsResponses.getInt("score");
                boolean isCorrect = rsResponses.getBoolean("isCorrect");
                responseLatexList.add(new ResponseLatex(rep, score, isCorrect));
            }
            rsResponses.close();
            psResponses.close();

            int maxCorrect = 0;
            int maxIncorrect = -100;
            for (ResponseLatex r : responseLatexList) {
                if (r.isCorrect) {
                    if (r.score > maxCorrect) maxCorrect = r.score;
                } else {
                    if (r.score > maxIncorrect) maxIncorrect = r.score;
                }
            }

            texcontentBuilder.append("\n\t\\element{general}{\n");
            texcontentBuilder.append("\t\\begin{question}{").append(removeLatexSpecialCharacters(idSectionLatex)).append("}")
                    .append("\\bareme{b=").append(maxCorrect)
                    .append(",m=").append(maxIncorrect).append("}\n");
            texcontentBuilder.append("\t\t").append(formatLatex(question)).append("\n");
            texcontentBuilder.append("\t\t\\begin{reponseshoriz}\n");

            for (ResponseLatex r : responseLatexList) {
                if (r.isCorrect) {
                    if (r.score == maxCorrect)
                        texcontentBuilder.append("\t\t\t\\bonne{").append(formatLatex(r.reponse)).append("}\n");
                    else
                        texcontentBuilder.append("\t\t\t\\bonne{").append(formatLatex(r.reponse)).append("}\\bareme{" + r.score + "}\n");
                } else {
                    if (r.score == maxIncorrect)
                        texcontentBuilder.append("\t\t\t\\mauvaise{").append(formatLatex(r.reponse)).append("}\n");
                    else
                        texcontentBuilder.append("\t\t\t\\mauvaise{").append(formatLatex(r.reponse)).append("}\\bareme{" + r.score + "}\n");

                }
            }
            texcontentBuilder.append("\t\t\\end{reponseshoriz}\n");
            texcontentBuilder.append("\t\\end{question}\n");
            texcontentBuilder.append("\t}\n");
        }
    }

    private void processFreeQuestion(Connection conn, String idSection, String question, String idSectionLatex, StringBuilder texcontentBuilder) throws SQLException {
        PreparedStatement psFreeQuestion = conn.prepareStatement("SELECT nombreLigne, tailleLigne, rappel, scoreTotal, nombreScore FROM QuestionLibre WHERE sectionID = ?");
        psFreeQuestion.setString(1, idSection);
        ResultSet rsFreeQuestion = psFreeQuestion.executeQuery();
        if (rsFreeQuestion.next()) {
            int nombreLigne = rsFreeQuestion.getInt("nombreLigne");
            double tailleLigne = rsFreeQuestion.getDouble("tailleLigne");
            String rappel = rsFreeQuestion.getString("rappel");
            int scoreTotal = rsFreeQuestion.getInt("scoreTotal");
            int nombreScore = rsFreeQuestion.getInt("nombreScore");

            texcontentBuilder.append("\n\t\\element{general}{\n");
            texcontentBuilder.append("\t\\begin{question}{").append(removeLatexSpecialCharacters(idSectionLatex)).append("}\n");
            texcontentBuilder.append("\t\t").append(formatLatex(question)).append("\n");
            texcontentBuilder.append("\t\t\\AMCOpen{lines=").append(nombreLigne)
                    .append(",lineheight=").append(tailleLigne)
                    .append("cm,question=\\texttt{").append(rappel).append("}}\n");
            texcontentBuilder.append("\t\t{\n");

            for (int i = 0; i < nombreScore; i++) {
                texcontentBuilder.append("\t\t\t\\mauvaise[").append(i).append("]{").append(i)
                        .append("}\\scoring{").append(scoreTotal).append("*").append(i)
                        .append("/").append(nombreScore).append("}\n");
            }

            texcontentBuilder.append("\t\t\t\\bonne[").append(nombreScore).append("]{").append(nombreScore)
                    .append("}\\scoring{").append(scoreTotal).append("}\n");
            texcontentBuilder.append("\t\t}\n");
            texcontentBuilder.append("\t\\end{question}\n");
            texcontentBuilder.append("\t}\n");
        }
    }

    private void processDescription(Connection conn, String idSection, StringBuilder texcontentBuilder) throws SQLException {
        PreparedStatement psDescription = conn.prepareStatement("SELECT idDescription, texte FROM Description WHERE sectionID = ?");
        psDescription.setString(1, idSection);
        ResultSet rsDescription = psDescription.executeQuery();

        while (rsDescription.next()) {
            int idDescription = rsDescription.getInt("idDescription");
            String texte = rsDescription.getString("texte");

            texcontentBuilder.append("\n\t\\element{general}{\n");
            if (!texte.trim().isEmpty())
                texcontentBuilder.append("\t\t").append(formatLatex(texte)).append("\\\\\n");

            // Process images and legends
            processImagesAndLegends(conn, idDescription, texcontentBuilder);

            texcontentBuilder.append("}\n");
        }
        rsDescription.close();
        psDescription.close();
    }

    private void processImagesAndLegends(Connection conn, int idDescription, StringBuilder texcontentBuilder) throws SQLException {
        PreparedStatement psImages = conn.prepareStatement("SELECT idImage, imagePath, legendText, imageWidth FROM Description_Images WHERE descriptionID = ?");
        psImages.setInt(1, idDescription);
        ResultSet rsImages = psImages.executeQuery();


        List<String> images = new ArrayList<>();
        List<String> legends = new ArrayList<>();
        List<Double> widths = new ArrayList<>();

        while (rsImages.next()) {
            images.add(rsImages.getString("imagePath"));
            legends.add(rsImages.getString("legendText"));
            widths.add(rsImages.getDouble("imageWidth"));
        }


        rsImages.close();
        psImages.close();

        for (int i = 0; i < images.size(); i++) {
            String imagePath = images.get(i).replace("\\", "/");
            texcontentBuilder.append("\\begin{figure}[H]\n");
            texcontentBuilder.append("    \\centering\n");
            texcontentBuilder.append("    \\includegraphics[width=").append(widths.get(i)).append("\\linewidth]{\\detokenize{").append(imagePath).append("}}\n");
            texcontentBuilder.append("    \\caption{").append(formatLatex(legends.get(i).replace("%", ""))).append("}\n");
            texcontentBuilder.append("\\end{figure}\n");
        }
    }

    private String verifyNomDevoir() {
        if (this.nomDevoir.getText().trim().isEmpty())
            nomDevoir.setText(this.projet.getNomProjet());
        //return this.nomDevoir.getText();
        return "Preremplie-ensemble";
    }

    private void verifyNombreExemplaire() {
        if (this.nombreExemplaire.getText().trim().isEmpty())
            this.nombreExemplaire.setText("1");
    }

    private void verifyRandomSeed() {
        if (this.randomSeed.getText().trim().isEmpty())
            this.randomSeed.setText("12345678");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void saveProject() {
        verifyNomDevoir();
        verifyRandomSeed();
        verifyNombreExemplaire();

        int exemplaire, seed;

        try {
            exemplaire = Integer.parseInt(nombreExemplaire.getText().trim());
        } catch (NumberFormatException e) {
            exemplaire = 1;
            nombreExemplaire.setText("1");
        }
        try {
            seed = Integer.parseInt(randomSeed.getText().trim());
        } catch (NumberFormatException e) {
            seed = 12345678;
            randomSeed.setText("12345678");
        }

        String updateQuery = "UPDATE Controle SET nomDevoir = ?, typeDevoir = ?, nombreExemplaire = ?, randomSeed = ?, " +
                "examHeader = ?, reponseHeader = ?, creationDate = ? WHERE idControle = ?";

        String updateProjectQuery = "UPDATE Projet SET creationDate = CURRENT_TIMESTAMP WHERE idProjet = ? ";

        try (Connection conn = SqlConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            pstmt.setString(1, nomDevoir.getText());
            pstmt.setString(2, typeDevoir.getText());
            pstmt.setInt(3, exemplaire);
            pstmt.setInt(4, seed);
            pstmt.setString(5, examHeader.getText());
            pstmt.setString(6, reponseHeader.getText());
            pstmt.setString(7, dateDevoir.getValue().toString());
            pstmt.setInt(8, this.devoir.getIdControle());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                showAlert("Success", "Exam well created.");
                this.devoir.setNomDevoir(nomDevoir.getText());
                this.devoir.setTypeDevoir(typeDevoir.getText());
                this.devoir.setNombreExemplaire(exemplaire);
                this.devoir.setRandomSeed(seed);
                this.devoir.setExamHeader(examHeader.getText());
                this.devoir.setReponseHeader(reponseHeader.getText());
                this.devoir.setCreationDate(Date.valueOf(dateDevoir.getValue().toString()));
            } else {
                showAlert("Error", "Cannot update the Exam");
            }

        } catch (SQLException e) {
            showAlert("Error", "Error happened when updating the Exam : " + e.getMessage());
            e.printStackTrace();
        }

        try (Connection conn = SqlConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateProjectQuery)) {

            pstmt.setInt(1, projet.getIdProjet());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows <= 0) {
                showAlert("Error", "Project update Error!");
            }

        } catch (SQLException e) {
            showAlert("Error", "Error happened when updating the Project " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String formatLatex(String text) {
        //todo check if there is something missing
        return text
                .replace("\\ ", "\\textbackslash{}") // Backslash
                .replace("&", "\\&")                // Ampersand
                .replace("%", "\\%")                // Pourcentage
                .replace("$", "\\$")                // Dollar
                .replace("#", "\\#")                // Dièse
                .replace("_", "\\_")                // Souligné
                .replace("{", "\\{")                // Accolade ouvrante
                .replace("}", "\\}")                // Accolade fermante
                .replace("~", "\\textasciitilde{}") // Tilde
                .replace("^", "\\textasciicircum{}") // Accent circonflexe
                .replace("\n", "\\newline ")         // Retour à la ligne
                .replace("<", "\\textless{}")       // Moins que
                .replace(">", "\\textgreater{}")    // Plus que
                .replace("€", "\\texteuro{}")       // Euro
                .replace("£", "\\pounds{}")         // Livre sterling
                .replace("¥", "\\textyen{}")        // Yen
                .replace("©", "\\textcopyright{}")  // Copyright
                .replace("®", "\\textregistered{}")  // Marque déposée
                .replace("™", "\\texttrademark{}")   // Marque de commerce
                .replace("–", "--")                  // Tiret long
                .replace("—", "---")                 // Tiret émis
                .replace("•", "\\textbullet{}")      // Puce
                .replace("«", "\\guillemotleft{}")   // Guillemot gauche
                .replace("»", "\\guillemotright{}")  // Guillemot droit
                .replace("‘", "`")                   // Apostrophe gauche
                .replace("’", "'")                   // Apostrophe droite
                .replace("“", "``")                  // Guillemet ouvrant
                .replace("”", "''")
                .replace(":¨:", "\\multiSymbole{}") // ♣
                ;                 // Guillemet fermant
    }

    private String removeLatexSpecialCharacters(String input) {

        // Define the regex pattern for LaTeX special characters
        String regex = "[\\\\{}#%&$^_~]";

        // Replace all occurrences of the special characters with an empty string
        return input.replaceAll(regex, "");
    }

    @FXML
    public void handleClicksSaveProject(ActionEvent event) {

        saveProject();

        ObservableList<RowTableSection> sections = tableSection.getItems();

        StringBuilder texcontentBuilder = new StringBuilder();

        texcontentBuilder.append(generateHeader());

        Connection conn = null;
        try {
            conn = SqlConnection.getConnection();
            for (RowTableSection row : sections) {
                String type = row.getType();
                String question = row.getQuestion();
                String idSection = row.getIdSection();
                String idSectionLatex = idSection.replace("#", " : ");

                if (type.equals("QCM") || type.equals("QCU")) {
                    processQCM(conn, idSection, question, idSectionLatex, texcontentBuilder);
                } else if (type.equals("QuestionLibre")) {
                    processFreeQuestion(conn, idSection, question, idSectionLatex, texcontentBuilder);
                } else if (type.equals("Description")) {
                    processDescription(conn, idSection, texcontentBuilder);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        texcontentBuilder.append(generateFooter());

        saveToFile(texcontentBuilder.toString());

        generatePDF();

        FxmlLoader object = new FxmlLoader();
        Parent view = object.getPane("Home");
        parentPane.getChildren().removeAll();
        parentPane.getChildren().setAll(view);

    }

    private void saveToFile(String content) {
        File directory = new File(this.projet.getLocalisationProjet() + "\\" + this.projet.getNomProjet());
        if (!directory.exists()) {
            directory.mkdir();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(directory, verifyNomDevoir() + ".tex")))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStream styleStream = getClass().getResourceAsStream("/com/example/project7/latex/automultiplechoice.sty")) {
            if (styleStream == null) {
                System.err.println("Resource automultiplechoice.sty not found.");
                return;
            }
            // Define the destination path (copy the file alongside the .tex file)
            File destinationFile = new File(directory, "automultiplechoice.sty");
            Files.copy(styleStream, destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generatePDF() {
        // Define the directory and .tex file
        File directory = new File(this.projet.getLocalisationProjet() + File.separator + this.projet.getNomProjet());
        File texFile = new File(directory, verifyNomDevoir() + ".tex");

        // Use the relative filename so the output is created in the same directory
        ProcessBuilder processBuilder = new ProcessBuilder("pdflatex", texFile.getName());
        processBuilder.directory(directory);
        processBuilder.redirectErrorStream(true);  // Merge standard error with standard output

        try {
            Process process = processBuilder.start();
            // Optionally, read the output from the pdflatex process
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("PDF generated successfully.");
                // Attempt to open the PDF file in the default PDF viewer
                File pdfFile = new File(directory, verifyNomDevoir() + ".pdf");
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    if (pdfFile.exists()) {
                        desktop.open(pdfFile);
                    } else {
                        System.err.println("PDF file not found: " + pdfFile.getAbsolutePath());
                    }
                } else {
                    System.err.println("Desktop is not supported on this platform.");
                }
            } else {
                System.err.println("PDF generation failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}