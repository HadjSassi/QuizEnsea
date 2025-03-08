package com.example.project7.controller.edition;

import com.example.project7.model.QCM;
import com.example.project7.model.Reponse;
import com.example.project7.model.Section;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.IntegerStringConverter;
import mysql_connection.MySqlConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditerQCU implements Initializable {

    @FXML
    private TextField enonceQuestion;

    @FXML
    private TextField baremePos;

    @FXML
    private TextField reponseCorrect;

    @FXML
    private TextField baremeNegDefault;

    @FXML
    private TableView<Reponse> incorrectTableView;

    @FXML
    private TableColumn<Reponse, String> responseColumn;

    @FXML
    private TableColumn<Reponse, Integer> scoreColumn;

    @FXML
    private TableColumn<Reponse, Void> actionColumn;

    @FXML
    private Button cancelQcu;

    private Section section;

    private QCM qcu;

    public void setSection(Section section) {
        this.section = section;
    }

    private boolean verifyQuesstion() {
        String question = enonceQuestion.getText().trim();

        if (question.isEmpty()) {
            if (!enonceQuestion.getStyleClass().contains("text-field-danger")) {
                enonceQuestion.getStyleClass().add("text-field-danger");
            }
            return false;
        } else {
            enonceQuestion.getStyleClass().removeAll("text-field-danger");
            return true;
        }
    }

    private boolean verifyReponseCorrect() {
        String question = reponseCorrect.getText().trim();

        if (question.isEmpty()) {
            if (!reponseCorrect.getStyleClass().contains("text-field-danger")) {
                reponseCorrect.getStyleClass().add("text-field-danger");
            }
            return false;
        } else {
            reponseCorrect.getStyleClass().removeAll("text-field-danger");
            return true;
        }
    }

    @FXML
    public void handleInputNumber(KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        String currentText = textField.getText();

        // Allow only digits and an optional leading "-"
        String sanitizedText = currentText.replaceAll("[^\\d-]", "");

        // Ensure "-" is only at the start and not repeated
        if (sanitizedText.length() > 1) {
            sanitizedText = sanitizedText.replaceAll("(?<!^)-", ""); // Remove "-" if not at the start
        }

        // Limit to 3 characters (including possible "-")
        if (sanitizedText.length() > 3) {
            sanitizedText = sanitizedText.substring(0, 3);
        }

        textField.setText(sanitizedText);
        textField.positionCaret(sanitizedText.length());
    }


    @FXML
    public void handleClicksAddWrongResponce(ActionEvent event) {
        String defaultResponse = "Mauvaise réponse";
        int defaultScore;
        try {
            defaultScore = Integer.parseInt(baremeNegDefault.getText());
        } catch (NullPointerException e) {
            defaultScore = 0;
        }
        ObservableList<Reponse> items = incorrectTableView.getItems();
        items.add(new Reponse(defaultResponse, defaultScore));
    }

    @FXML
    public void handleClicksAddQCU(ActionEvent event) {
        if (verifyQuesstion() && verifyReponseCorrect()) {
            if (checkSectionExists(this.section.getIdSection())) {
                // Show confirmation alert
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
                // Proceed to create new section, QCU, and responses as before
                createSection();
                createQCU();
                createQCUResponse();
                this.section.getDevoir().getController().fetchAndUpdateTableView();
                Stage stage = (Stage) cancelQcu.getScene().getWindow();
                stage.close();
            }
        }
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
        handleClicksAddQCU(null);
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

        return false; // Default to false if there's an error
    }

    private void createSection() {
        String insertSectionQuery = "INSERT INTO Section (idSection, ordreSection, controleID) VALUES (?, ?, ?)";

        try (Connection connection = MySqlConnection.getOracleConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertSectionQuery)) {

            insertStatement.setString(1, this.section.getIdSection());
            insertStatement.setInt(2, this.section.getOrdreSection());
            insertStatement.setInt(3, this.section.getDevoir().getIdControle());

            int rowsAffected = insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error inserting Section data: " + e.getMessage());
        }
    }

    private void createQCU() {
        String insertQCUQuery = "INSERT INTO QCM (question, isQcu, sectionID) VALUES (?, ?, ?)";

        try (Connection connection = MySqlConnection.getOracleConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertQCUQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

            insertStatement.setString(1, enonceQuestion.getText());
            insertStatement.setBoolean(2, true);
            insertStatement.setString(3, this.section.getIdSection());

            int rowsAffected = insertStatement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        String idQCU = String.valueOf(generatedKeys.getInt(1));
                        qcu = new QCM();
                        qcu.setQCU(true);
                        qcu.setIdSection(idQCU);
                    }
                }
            } else {
                System.err.println("Failed to insert QCU data.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error inserting QCU data: " + e.getMessage());
        }
    }

    private void createQCUResponse() {
        createQCUCorrectResponse();
        createQCUInCorrectResponse();
    }

    private void createQCUInCorrectResponse() {
        String insertIncorrectResponseQuery = "INSERT INTO QCM_Reponses (qcmID, reponse, score, isCorrect) VALUES (?, ?, ?, ?)";

        try (Connection connection = MySqlConnection.getOracleConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertIncorrectResponseQuery)) {

            for (Reponse response : incorrectTableView.getItems()) {
                insertStatement.setInt(1, Integer.parseInt(qcu.getIdSection()));
                insertStatement.setString(2, response.getResponse());
                insertStatement.setInt(3, response.getScore());
                insertStatement.setBoolean(4, false);

                insertStatement.addBatch();
            }

            insertStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error inserting incorrect responses: " + e.getMessage());
        }
    }

    private void createQCUCorrectResponse() {
        String insertCorrectResponseQuery = "INSERT INTO QCM_Reponses (qcmID, reponse, score, isCorrect) VALUES (?, ?, ?, ?)";

        try (Connection connection = MySqlConnection.getOracleConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertCorrectResponseQuery)) {

            insertStatement.setInt(1, Integer.parseInt(qcu.getIdSection()));
            insertStatement.setString(2, reponseCorrect.getText());
            insertStatement.setInt(3, Integer.parseInt(baremePos.getText()));
            insertStatement.setBoolean(4, true);

            insertStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error inserting correct response: " + e.getMessage());
        }
    }


    @FXML
    public void handleClicksCancelQCU(ActionEvent event) {
        EditerSection.cancelSection();
        Stage stage = (Stage) cancelQcu.getScene().getWindow();
        stage.close();
    }

    private void openEditPopup(Reponse reponse) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UTILITY);

        VBox popupVBox = new VBox(10);
        popupVBox.setPadding(new Insets(20));

        TextArea responseTextArea = new TextArea(reponse.getResponse());
        responseTextArea.setPrefSize(300, 150);

        TextField scoreTextField = new TextField(String.valueOf(reponse.getScore()));
        scoreTextField.setPromptText("Entrez le barème");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveButton = new Button("Enregistrer");
        saveButton.setOnAction(event -> {
            reponse.setResponse(responseTextArea.getText());
            reponse.setScore(Integer.parseInt(scoreTextField.getText()));

            incorrectTableView.refresh();

            popupStage.close();
        });

        Button closeButton = new Button("Fermer");
        closeButton.setOnAction(event -> {
            popupStage.close();
        });

        buttonBox.getChildren().addAll(saveButton, closeButton);

        popupVBox.getChildren().addAll(new Label("Modifier la réponse et le barème:"), responseTextArea, scoreTextField, buttonBox);

        Scene popupScene = new Scene(popupVBox, 350, 250);
        popupStage.setScene(popupScene);
        popupStage.setTitle("Modifier la réponse");
        popupStage.setOnShown(event -> {
            responseTextArea.requestFocus();
            responseTextArea.selectAll();
        });
        popupStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        baremePos.setText("1");
        baremeNegDefault.setText("0");

        responseColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getResponse()));
        responseColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        responseColumn.setOnEditCommit(event -> {
            Reponse reponse = event.getRowValue();
            reponse.setResponse(event.getNewValue());
        });


        scoreColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getScore()).asObject());
        scoreColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        scoreColumn.setOnEditCommit(event -> {
            Reponse reponse = event.getRowValue();
            reponse.setScore(event.getNewValue());
        });

        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");
            private final Button editButton = new Button("Modifier");


            {
                deleteButton.setOnAction(event -> {
                    Reponse reponse = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(reponse);
                });
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");

                editButton.setOnAction(event -> {
                    Reponse reponse = getTableView().getItems().get(getIndex());
                    openEditPopup(reponse);
                });
                editButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonBox = new HBox(10, editButton, deleteButton);
                    setGraphic(buttonBox);
                }
            }
        });


        incorrectTableView.setItems(FXCollections.observableArrayList());
    }

    public void setSectionUpdating(Section section) {
        this.section = section;
        this.enonceQuestion.setText(section.getIdSection());
        loadQCUFromSectionId(section.getIdSection());
    }

    private void loadQCUFromSectionId(String idSection) {
        String fetchQCUQuery = "SELECT * FROM QCM WHERE sectionID = ?";
        String fetchResponsesQuery = "SELECT * FROM QCM_Reponses WHERE qcmID = ?";

        try (Connection connection = MySqlConnection.getOracleConnection();
             PreparedStatement qcuStatement = connection.prepareStatement(fetchQCUQuery);
             PreparedStatement responseStatement = connection.prepareStatement(fetchResponsesQuery)) {

            // Fetch QCU details
            qcuStatement.setString(1, idSection);
            ResultSet qcuResultSet = qcuStatement.executeQuery();
            if (qcuResultSet.next()) {
                qcu = new QCM();
                qcu.setIdSection(qcuResultSet.getString("idQCM"));
                qcu.setQuestion(qcuResultSet.getString("question"));
                qcu.setQCU(qcuResultSet.getBoolean("isQcu"));

                enonceQuestion.setText(qcu.getQuestion());
            }

            // Fetch responses
            if (qcu != null) {
                ObservableList<Reponse> responses = FXCollections.observableArrayList();
                responseStatement.setInt(1, Integer.parseInt(qcu.getIdSection()));
                ResultSet responseResultSet = responseStatement.executeQuery();

                while (responseResultSet.next()) {
                    String responseText = responseResultSet.getString("reponse");
                    int score = responseResultSet.getInt("score");
                    boolean isCorrect = responseResultSet.getBoolean("isCorrect");

                    if (isCorrect) {
                        reponseCorrect.setText(responseText);
                        baremePos.setText(String.valueOf(score));
                    } else {
                        responses.add(new Reponse(responseText, score));
                    }
                }

                incorrectTableView.setItems(responses);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error loading QCU data: " + e.getMessage());
        }
    }

    public void modifyCorrectAnswer(ActionEvent events) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UTILITY);

        VBox popupVBox = new VBox(10);
        popupVBox.setPadding(new Insets(20));

        TextArea responseTextArea = new TextArea(reponseCorrect.getText());


        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveButton = new Button("Modifier");
        saveButton.setOnAction(event -> {
            reponseCorrect.setText(responseTextArea.getText());

            popupStage.close();
        });

        Button closeButton = new Button("Fermer");
        closeButton.setOnAction(event -> {
            popupStage.close();
        });

        buttonBox.getChildren().addAll(saveButton, closeButton);

        popupVBox.getChildren().addAll(responseTextArea, buttonBox);

        Scene popupScene = new Scene(popupVBox, 350, 250);
        popupStage.setScene(popupScene);
        popupStage.setTitle("Edit the correct response");
        popupStage.show();
    }

    public void modfiyQuestion(ActionEvent events) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UTILITY);

        VBox popupVBox = new VBox(10);
        popupVBox.setPadding(new Insets(20));

        TextArea responseTextArea = new TextArea(enonceQuestion.getText());


        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveButton = new Button("Modifier");
        saveButton.setOnAction(event -> {
            enonceQuestion.setText(responseTextArea.getText());

            popupStage.close();
        });

        Button closeButton = new Button("Fermer");
        closeButton.setOnAction(event -> {
            popupStage.close();
        });

        buttonBox.getChildren().addAll(saveButton, closeButton);

        popupVBox.getChildren().addAll(responseTextArea, buttonBox);

        Scene popupScene = new Scene(popupVBox, 350, 250);
        popupStage.setScene(popupScene);
        popupStage.setTitle("Edit the Question");
        popupStage.show();
    }
}
