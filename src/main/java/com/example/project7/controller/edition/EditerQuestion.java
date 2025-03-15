package com.example.project7.controller.edition;

import com.example.project7.model.QCM;
import com.example.project7.model.QuestionLibre;
import com.example.project7.model.Reponse;
import com.example.project7.model.Section;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mysql_connection.MySqlConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;

import static com.example.project7.laguage.en.StringLang.*;

public class EditerQuestion implements Initializable {

    @FXML
    private TextArea enonceQuestion;

    @FXML
    private TextField scoringTotale;

    @FXML
    private TextField nombreScore;

    @FXML
    private TextField nombreLignes;

    @FXML
    private TextField tailleLigne;

    @FXML
    private TextField rappelQuestion;

    @FXML
    private Button ajouterQuestion;

    @FXML
    private Button cancelQuestion;

    private Section section;

    private String rappel;

    QuestionLibre questionLibre;

    public void setSection(Section identifierSection) {
        this.section = identifierSection;
    }


    @FXML
    public void cancelQuestion(ActionEvent event) {
        EditerSection.cancelSection();
        Stage stage = (Stage) cancelQuestion.getScene().getWindow();
        stage.close();
    }

    private void verifyScoringTotal() {
        String element = scoringTotale.getText().trim();

        if (element.isEmpty()) {
            if (!scoringTotale.getStyleClass().contains(Textdanger.getValue())) {
                scoringTotale.getStyleClass().add(Textdanger.getValue());
                this.scoringTotale.setText(NbrValue1.getValue());
            }
        } else {
            scoringTotale.getStyleClass().removeAll(Textdanger.getValue());
        }
    }

    private void verifyNombreScore() {
        String element = nombreScore.getText().trim();

        if (element.isEmpty()) {
            if (!nombreScore.getStyleClass().contains(Textdanger.getValue())) {
                nombreScore.getStyleClass().add(Textdanger.getValue());
                this.nombreScore.setText("2");
            }
        } else {
            nombreScore.getStyleClass().removeAll(Textdanger.getValue());
        }
    }

    private void verifyNombreLignes() {
        String element = nombreLignes.getText().trim();

        if (element.isEmpty()) {
            if (!nombreLignes.getStyleClass().contains(Textdanger.getValue())) {
                nombreLignes.getStyleClass().add(Textdanger.getValue());
                this.nombreLignes.setText("3");
            }
        } else {
            nombreLignes.getStyleClass().removeAll(Textdanger.getValue());
        }
    }

    private void verifyTailleLigne() {
        String element = tailleLigne.getText().trim();

        if (element.isEmpty()) {
            if (!tailleLigne.getStyleClass().contains(Textdanger.getValue())) {
                tailleLigne.getStyleClass().add(Textdanger.getValue());
                this.tailleLigne.setText("0.5");
            }
        } else {
            tailleLigne.getStyleClass().removeAll(Textdanger.getValue());
        }
    }

    private boolean verifyQuestion() {
        String question = enonceQuestion.getText().trim();

        if (question.isEmpty()) {
            if (!enonceQuestion.getStyleClass().contains(Textdanger.getValue())) {
                enonceQuestion.getStyleClass().add(Textdanger.getValue());
            }
            return false;
        } else {
            enonceQuestion.getStyleClass().removeAll(Textdanger.getValue());
            return true;
        }
    }

    private void verifyRappel() {
        String question = rappelQuestion.getText().trim();

        if (question.isEmpty()) {
            if (!rappelQuestion.getStyleClass().contains(Textdanger.getValue())) {
                rappelQuestion.getStyleClass().add(Textdanger.getValue());
                this.rappelQuestion.setText(this.rappel);
            }
        } else {
            rappelQuestion.getStyleClass().removeAll(Textdanger.getValue());
        }
    }

    @FXML
    public void ajouterQuestion(ActionEvent event) {
        verifyRappel();
        verifyQuestion();
        verifyTailleLigne();
        verifyNombreLignes();
        verifyNombreScore();
        verifyScoringTotal();
        if (verifyQuestion()) {
            if (checkSectionExists(this.section.getIdSection())) {
                // Show confirmation alert
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle(SectionExists.getValue());
                confirmationAlert.setHeaderText( SectionAlreadyExist.getValue());
                confirmationAlert.setContentText(SectionId.getValue() + this.section.getIdSection() +Sectionexistedeja.getValue());

                ButtonType modifyButton = new ButtonType(Modifier.getValue());
                ButtonType cancelButton = new ButtonType(cancel.getValue(), ButtonBar.ButtonData.CANCEL_CLOSE);

                confirmationAlert.getButtonTypes().setAll(modifyButton, cancelButton);

                // Handle user response
                confirmationAlert.showAndWait().ifPresent(response -> {
                    if (response == modifyButton) {
                        updateSection();
                    }
                });
            } else {
                questionLibre.setRappel(this.rappelQuestion.getText().trim());
                questionLibre.setQuestion(this.enonceQuestion.getText().trim());
                questionLibre.setTailleLigne(Float.parseFloat(this.tailleLigne.getText().trim()));
                questionLibre.setNombreLigne(Integer.parseInt(this.nombreLignes.getText().trim()));
                questionLibre.setNombreScore(Integer.parseInt(this.nombreScore.getText().trim()));
                questionLibre.setScoreTotal(Float.parseFloat(scoringTotale.getText().trim()));
                createSection();
                createQuestion();
                this.section.getDevoir().getController().fetchAndUpdateTableView();
                Stage stage = (Stage) cancelQuestion.getScene().getWindow();
                stage.close();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Random random = new Random();
        int x = random.nextInt(999999999);
        this.rappel = QuesNmr.getValue() + String.valueOf(x);
        this.rappelQuestion.setText(this.rappel);
        this.scoringTotale.setText(NbrValue1.getValue());
        this.nombreScore.setText(NbrValue2.getValue());
        this.nombreLignes.setText(NbrValue3.getValue());
        this.tailleLigne.setText(NbrValue05.getValue());
        this.questionLibre = new QuestionLibre();
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
            System.err.println(ErrcheckExist.getValue() + e.getMessage());
        }

        return false; // Default to false if there's an error
    }

    private void removeSection() {
        String deleteQuery = "DELETE FROM section WHERE idSection = ?";
        try (Connection connection = MySqlConnection.getOracleConnection();
             PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

            statement.setString(1, section.getIdSection());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(ErrdltSec.getValue() + e.getMessage());
        }
    }

    private void updateSection() {
        removeSection();
        ajouterQuestion(null);
    }


    @FXML
    public void handleInputNumber(KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        String currentText = textField.getText();

        String sanitizedText = currentText.replaceAll(character.getValue(), espace.getValue());


        textField.setText(sanitizedText);
        textField.positionCaret(sanitizedText.length());
    }

    @FXML
    public void handleInputFloat(KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        String currentText = textField.getText();

        // Keep only valid float characters (digits and a single dot)
        String sanitizedText = currentText.replaceAll(character.getValue(), espace.getValue());

        // Allow only one decimal point
        int firstDotIndex = sanitizedText.indexOf(point.getValue());
        if (firstDotIndex != -1) {
            sanitizedText = sanitizedText.substring(0, firstDotIndex + 1)
                    + sanitizedText.substring(firstDotIndex + 1).replaceAll(slash.getValue(), espace.getValue());
        }

        textField.setText(sanitizedText);
        textField.positionCaret(sanitizedText.length());
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
            System.err.println(Errinsert.getValue() + e.getMessage());
        }
    }

    private void createQuestion() {
        String insertQCUQuery = "INSERT INTO questionlibre (question, scoreTotal,nombreScore, nombreLigne, tailleLigne, rappel, sectionID) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = MySqlConnection.getOracleConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertQCUQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

            insertStatement.setString(1, questionLibre.getQuestion());
            insertStatement.setFloat(2, questionLibre.getScoreTotal());
            insertStatement.setInt(3, questionLibre.getNombreScore());
            insertStatement.setInt(4, questionLibre.getNombreLigne());
            insertStatement.setFloat(5, questionLibre.getTailleLigne());
            insertStatement.setString(6, questionLibre.getRappel());
            insertStatement.setString(7, this.section.getIdSection());

            int rowsAffected = insertStatement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        String idQCU = String.valueOf(generatedKeys.getInt(1));
                        questionLibre.setIdSection(idQCU);
                    }
                }
            } else {
                System.err.println(  failInsrtQcudata.getValue());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(ErrInsrtQcudata.getValue() + e.getMessage());
        }
    }

    public void setSectionUpdating(Section section) {
        this.section = section;
        this.enonceQuestion.setText(section.getIdSection());
        loadQuestionFromSectionId(section.getIdSection());
    }

    private void loadQuestionFromSectionId(String idSection) {
        String fetchQCUQuery = "SELECT * FROM questionlibre WHERE sectionID = ?";

        try (Connection connection = MySqlConnection.getOracleConnection();
             PreparedStatement qcuStatement = connection.prepareStatement(fetchQCUQuery)){

            qcuStatement.setString(1, idSection);
            ResultSet qcuResultSet = qcuStatement.executeQuery();
            if (qcuResultSet.next()) {
                questionLibre = new QuestionLibre();
                questionLibre.setIdSection(qcuResultSet.getString(SecID.getValue()));
                questionLibre.setQuestion(qcuResultSet.getString(question.getValue()));
                questionLibre.setRappel(qcuResultSet.getString(RAPPEL.getValue()));
                questionLibre.setTailleLigne(qcuResultSet.getFloat( taillelign.getValue()));
                questionLibre.setNombreLigne(qcuResultSet.getInt(nbrligne.getValue()));
                questionLibre.setNombreScore(qcuResultSet.getInt(nbrscore.getValue()));
                questionLibre.setScoreTotal(qcuResultSet.getFloat(scoretotal.getValue()));

                enonceQuestion.setText(questionLibre.getQuestion());
                rappelQuestion.setText(questionLibre.getRappel());
                tailleLigne.setText(String.valueOf(questionLibre.getTailleLigne()));
                nombreLignes.setText(String.valueOf(questionLibre.getNombreLigne()));
                nombreScore.setText(String.valueOf(questionLibre.getNombreScore()));
                scoringTotale.setText(String.valueOf(questionLibre.getScoreTotal()));

            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(ErrloadingQCU.getValue()+ e.getMessage());
        }
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

        Button saveButton = new Button(Modifier.getValue());
        saveButton.setOnAction(event -> {
            enonceQuestion.setText(responseTextArea.getText());

            popupStage.close();
        });

        Button closeButton = new Button(close.getValue());
        closeButton.setOnAction(event -> {
            popupStage.close();
        });

        buttonBox.getChildren().addAll(saveButton, closeButton);

        popupVBox.getChildren().addAll(responseTextArea, buttonBox);

        Scene popupScene = new Scene(popupVBox, 350, 250);
        popupStage.setScene(popupScene);
        popupStage.setTitle(EdtQues.getValue());
        popupStage.show();
    }
}
