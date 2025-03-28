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
import sql_connection.SqlConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditerQCM implements Initializable {

    @FXML
    private TextArea enonceQuestion;

    @FXML
    private TextField baremePosDefault;

    @FXML
    private TextField baremeNegDefault;

    @FXML
    private TableView<Reponse> correctTableView;

    @FXML
    private TableColumn<Reponse, String> responsePosColumn;

    @FXML
    private TableColumn<Reponse, Integer> scorePosColumn;

    @FXML
    private TableColumn<Reponse, Void> actionPosColumn;

    @FXML
    private TableView<Reponse> incorrectTableView;

    @FXML
    private TableColumn<Reponse, String> responseNegColumn;

    @FXML
    private TableColumn<Reponse, Integer> scoreNegColumn;

    @FXML
    private TableColumn<Reponse, Void> actionNegColumn;

    @FXML
    private Button cancelQcm;

    private Section section;

    private QCM qcm;

    public void setSection(Section identifierSection) {
        this.section = identifierSection;
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

    @FXML
    public void handleClicksAddGoodResponce(ActionEvent event) {
        String defaultResponse = "Correct Answer";
        int defaultScore;
        try {
            defaultScore = Integer.parseInt(baremePosDefault.getText());
        } catch (NullPointerException e) {
            defaultScore = 0;
        }
        ObservableList<Reponse> items = correctTableView.getItems();
        items.add(new Reponse(defaultResponse, defaultScore));    }

    @FXML
    public void handleClicksAddWrongResponce(ActionEvent event) {
        String defaultResponse = "Wrong Answer";
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
    public void handleClicksAddQCM(ActionEvent event) {
        if (verifyQuesstion()) {
            if (checkSectionExists(this.section.getIdSection())) {
                // Show confirmation alert
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Section Exists");
                confirmationAlert.setHeaderText("This section already exists");
                confirmationAlert.setContentText("Section with the identifier " + this.section.getIdSection() + " already exists, Would you like to overwrite it?");

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
                // Proceed to create new section, QCU, and responses as before
                createSection();
                createQCM();
                createQCUResponse();
                this.section.getDevoir().getController().fetchAndUpdateTableView();
                Stage stage = (Stage) cancelQcm.getScene().getWindow();
                stage.close();
            }
        }    }

    @FXML
    public void handleClicksCancelQCM(ActionEvent event) {
        EditerSection.cancelSection();
        Stage stage = (Stage) cancelQcm.getScene().getWindow();
        stage.close();    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        baremePosDefault.setText("1");
        baremeNegDefault.setText("0");

        responseNegColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getResponse()));
        responseNegColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        responseNegColumn.setOnEditCommit(event -> {
            Reponse reponse = event.getRowValue();
            reponse.setResponse(event.getNewValue());
        });


        scoreNegColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getScore()).asObject());
        scoreNegColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        scoreNegColumn.setOnEditCommit(event -> {
            Reponse reponse = event.getRowValue();
            reponse.setScore(event.getNewValue());
        });

        actionNegColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            private final Button editButton = new Button("Modify");


            {
                deleteButton.setOnAction(event -> {
                    Reponse reponse = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(reponse);
                });
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");

                editButton.setOnAction(event -> {
                    Reponse reponse = getTableView().getItems().get(getIndex());
                    openEditPopup(reponse, false);
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



        responsePosColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getResponse()));
        responsePosColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        responsePosColumn.setOnEditCommit(event -> {
            Reponse reponse = event.getRowValue();
            reponse.setResponse(event.getNewValue());
        });


        scorePosColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getScore()).asObject());
        scorePosColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        scorePosColumn.setOnEditCommit(event -> {
            Reponse reponse = event.getRowValue();
            reponse.setScore(event.getNewValue());
        });

        actionPosColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            private final Button editButton = new Button("Modify");


            {
                deleteButton.setOnAction(event -> {
                    Reponse reponse = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(reponse);
                });
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");

                editButton.setOnAction(event -> {
                    Reponse reponse = getTableView().getItems().get(getIndex());
                    openEditPopup(reponse, true);
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


        correctTableView.setItems(FXCollections.observableArrayList());

        enonceQuestion.setWrapText(true);

    }

    private void updateQCM() {
        String updateQcmQuery = "UPDATE QCM SET question = ? WHERE idQCM = ?";
        String deleteResponsesQuery = "DELETE FROM QCM_Reponses WHERE qcmID = ?";
        String insertResponseQuery = "INSERT INTO QCM_Reponses (qcmID, reponse, score, isCorrect) VALUES (?, ?, ?, ?)";

        try (Connection connection = SqlConnection.getConnection()) {
            connection.setAutoCommit(false);

            // Mise à jour du texte de la question dans la table qcm
            try (PreparedStatement updateStmt = connection.prepareStatement(updateQcmQuery)) {
                updateStmt.setString(1, enonceQuestion.getText());
                updateStmt.setInt(2, Integer.parseInt(qcm.getIdSection()));
                updateStmt.executeUpdate();
            }

            // Suppression des anciennes réponses
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteResponsesQuery)) {
                deleteStmt.setInt(1, Integer.parseInt(qcm.getIdSection()));
                deleteStmt.executeUpdate();
            }

            // Insertion des nouvelles réponses (correctes et incorrectes)
            try (PreparedStatement insertStmt = connection.prepareStatement(insertResponseQuery)) {
                // Réponses correctes
                for (Reponse response : correctTableView.getItems()) {
                    insertStmt.setInt(1, Integer.parseInt(qcm.getIdSection()));
                    insertStmt.setString(2, response.getResponse());
                    insertStmt.setInt(3, response.getScore());
                    insertStmt.setBoolean(4, true);
                    insertStmt.addBatch();
                }
                // Réponses incorrectes
                for (Reponse response : incorrectTableView.getItems()) {
                    insertStmt.setInt(1, Integer.parseInt(qcm.getIdSection()));
                    insertStmt.setString(2, response.getResponse());
                    insertStmt.setInt(3, response.getScore());
                    insertStmt.setBoolean(4, false);
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateSection() {
        updateQCM();
        this.section.getDevoir().getController().fetchAndUpdateTableView();
        Stage stage = (Stage) cancelQcm.getScene().getWindow();
        stage.close();
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
        }

        return false; // Default to false if there's an error
    }

    private void createSection() {
        String insertSectionQuery = "INSERT INTO Section (idSection, ordreSection, controleID) VALUES (?, ?, ?)";

        try (Connection connection = SqlConnection.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertSectionQuery)) {

            insertStatement.setString(1, this.section.getIdSection());
            insertStatement.setInt(2, this.section.getOrdreSection());
            insertStatement.setInt(3, this.section.getDevoir().getIdControle());

            int rowsAffected = insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createQCM() {
        String insertQCUQuery = "INSERT INTO QCM (question, isQCU, sectionID) VALUES (?, ?, ?)";

        try (Connection connection = SqlConnection.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertQCUQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

            insertStatement.setString(1, enonceQuestion.getText());
            insertStatement.setBoolean(2, false);
            insertStatement.setString(3, this.section.getIdSection());

            int rowsAffected = insertStatement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        String idQCU = String.valueOf(generatedKeys.getInt(1));
                        qcm = new QCM();
                        qcm.setQCU(false);
                        qcm.setIdSection(idQCU);
                    }
                }
            } else {
                System.err.println("Failed to insert QCU data.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createQCUResponse() {
        createQCUCorrectResponse();
        createQCUInCorrectResponse();
    }

    private void createQCUInCorrectResponse() {
        String insertIncorrectResponseQuery = "INSERT INTO QCM_Reponses (qcmID, reponse, score, isCorrect) VALUES (?, ?, ?, ?)";

        try (Connection connection = SqlConnection.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertIncorrectResponseQuery)) {

            for (Reponse response : incorrectTableView.getItems()) {
                insertStatement.setInt(1, Integer.parseInt(qcm.getIdSection()));
                insertStatement.setString(2, response.getResponse());
                insertStatement.setInt(3, response.getScore());
                insertStatement.setBoolean(4, false);

                insertStatement.addBatch();
            }

            insertStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createQCUCorrectResponse() {
        String insertCorrectResponseQuery = "INSERT INTO QCM_Reponses (qcmID, reponse, score, isCorrect) VALUES (?, ?, ?, ?)";

        try (Connection connection = SqlConnection.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertCorrectResponseQuery)) {

            for (Reponse response : correctTableView.getItems()) {
                insertStatement.setInt(1, Integer.parseInt(qcm.getIdSection()));
                insertStatement.setString(2, response.getResponse());
                insertStatement.setInt(3, response.getScore());
                insertStatement.setBoolean(4, true);

                insertStatement.addBatch();
            }

            insertStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openEditPopup(Reponse reponse, boolean isCorrect) {
        TableView<Reponse> tablevieuw ;
        if(isCorrect){
            tablevieuw = correctTableView;
        }else{
            tablevieuw = incorrectTableView;
        }


        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UTILITY);

        VBox popupVBox = new VBox(10);
        popupVBox.setPadding(new Insets(20));

        TextArea responseTextArea = new TextArea(reponse.getResponse());
        responseTextArea.setWrapText(true);
        responseTextArea.setPrefSize(300, 150);

        TextField scoreTextField = new TextField(String.valueOf(reponse.getScore()));
        scoreTextField.setPromptText("Enter the score");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            reponse.setResponse(responseTextArea.getText());
            reponse.setScore(Integer.parseInt(scoreTextField.getText()));

            tablevieuw.refresh();

            popupStage.close();
        });

        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> {
            popupStage.close();
        });

        buttonBox.getChildren().addAll(saveButton, closeButton);

        popupVBox.getChildren().addAll(new Label("Modify the answer and the score:"), responseTextArea, scoreTextField, buttonBox);

        Scene popupScene = new Scene(popupVBox, 350, 250);
        popupStage.setScene(popupScene);
        popupStage.setTitle("Modify the answer");
        popupStage.setOnShown(event -> {
            responseTextArea.requestFocus();
            responseTextArea.selectAll();
        });
        popupStage.show();
    }

    public void setSectionUpdating(Section section) {
        this.section = section;
        this.enonceQuestion.setText(section.getIdSection());
        loadQCMFromSectionId(section.getIdSection());
    }

    private void loadQCMFromSectionId(String idSection) {
        String fetchQCMQuery = "SELECT * FROM QCM WHERE sectionID = ?";
        String fetchResponsesQuery = "SELECT * FROM QCM_Reponses WHERE qcmID = ?";

        try (Connection connection = SqlConnection.getConnection();
             PreparedStatement qcuStatement = connection.prepareStatement(fetchQCMQuery);
             PreparedStatement responseStatement = connection.prepareStatement(fetchResponsesQuery)) {

            // Fetch QCM details
            qcuStatement.setString(1, idSection);
            ResultSet qcuResultSet = qcuStatement.executeQuery();
            if (qcuResultSet.next()) {
                qcm = new QCM();
                qcm.setIdSection(qcuResultSet.getString("idQCM"));
                qcm.setQuestion(qcuResultSet.getString("question"));
                qcm.setQCU(qcuResultSet.getBoolean("isQcu"));

                enonceQuestion.setText(qcm.getQuestion());
            }

            // Fetch responses
            if (qcm != null) {
                ObservableList<Reponse> responsesIncorrect = FXCollections.observableArrayList();
                ObservableList<Reponse> responsesCorrect = FXCollections.observableArrayList();
                responseStatement.setInt(1, Integer.parseInt(qcm.getIdSection()));
                ResultSet responseResultSet = responseStatement.executeQuery();

                while (responseResultSet.next()) {
                    String responseText = responseResultSet.getString("reponse");
                    int score = responseResultSet.getInt("score");
                    boolean isCorrect = responseResultSet.getBoolean("isCorrect");

                    if (isCorrect) {
                        responsesCorrect.add(new Reponse(responseText, score));
                    } else {
                        responsesIncorrect.add(new Reponse(responseText, score));
                    }
                }
                incorrectTableView.setItems(responsesIncorrect);
                correctTableView.setItems(responsesCorrect);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modfiyQuestion(ActionEvent events) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UTILITY);

        VBox popupVBox = new VBox(10);
        popupVBox.setPadding(new Insets(20));

        TextArea responseTextArea = new TextArea(enonceQuestion.getText());
        responseTextArea.setWrapText(true);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveButton = new Button("Modify");
        saveButton.setOnAction(event -> {
            enonceQuestion.setText(responseTextArea.getText());

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
        popupStage.setTitle("Edit the Question");
        popupStage.show();
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
}
