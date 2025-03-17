package com.example.project7.controller.edition;

import com.example.project7.model.QuestionLibre;
import com.example.project7.model.Section;
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
import sql_connection.SqlConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;

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
            if (!scoringTotale.getStyleClass().contains("text-field-danger")) {
                scoringTotale.getStyleClass().add("text-field-danger");
                this.scoringTotale.setText("1");
            }
        } else {
            scoringTotale.getStyleClass().removeAll("text-field-danger");
        }
    }

    private void verifyNombreScore() {
        String element = nombreScore.getText().trim();

        if (element.isEmpty()) {
            if (!nombreScore.getStyleClass().contains("text-field-danger")) {
                nombreScore.getStyleClass().add("text-field-danger");
                this.nombreScore.setText("2");
            }
        } else {
            nombreScore.getStyleClass().removeAll("text-field-danger");
        }
    }

    private void verifyNombreLignes() {
        String element = nombreLignes.getText().trim();

        if (element.isEmpty()) {
            if (!nombreLignes.getStyleClass().contains("text-field-danger")) {
                nombreLignes.getStyleClass().add("text-field-danger");
                this.nombreLignes.setText("3");
            }
        } else {
            nombreLignes.getStyleClass().removeAll("text-field-danger");
        }
    }

    private void verifyTailleLigne() {
        String element = tailleLigne.getText().trim();

        if (element.isEmpty()) {
            if (!tailleLigne.getStyleClass().contains("text-field-danger")) {
                tailleLigne.getStyleClass().add("text-field-danger");
                this.tailleLigne.setText("0.5");
            }
        } else {
            tailleLigne.getStyleClass().removeAll("text-field-danger");
        }
    }

    private boolean verifyQuestion() {
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

    private void verifyRappel() {
        String question = rappelQuestion.getText().trim();

        if (question.isEmpty()) {
            if (!rappelQuestion.getStyleClass().contains("text-field-danger")) {
                rappelQuestion.getStyleClass().add("text-field-danger");
                this.rappelQuestion.setText(this.rappel);
            }
        } else {
            rappelQuestion.getStyleClass().removeAll("text-field-danger");
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
        this.rappel = "QuestionN°" + String.valueOf(x);
        this.rappelQuestion.setText(this.rappel);
        this.scoringTotale.setText("1");
        this.nombreScore.setText("2");
        this.nombreLignes.setText("3");
        this.tailleLigne.setText("0.5");
        this.questionLibre = new QuestionLibre();
        enonceQuestion.setWrapText(true);
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

        return false; // Default to false if there's an error
    }

    private void removeSection() {
        String deleteQuery = "DELETE FROM Section WHERE idSection = ?";
        try (Connection connection = SqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

            statement.setString(1, section.getIdSection());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error deleting section: " + e.getMessage());
        }
    }

    private void updateQuestion() {
        String updateQuery = "UPDATE QuestionLibre SET question = ?, scoreTotal = ?, nombreScore = ?, nombreLigne = ?, tailleLigne = ?, rappel = ? WHERE sectionID = ?";

        try (Connection connection = SqlConnection.getConnection();
             PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {

            // Prépare les valeurs en récupérant les données saisies par l'utilisateur
            updateStmt.setString(1, enonceQuestion.getText().trim());
            updateStmt.setFloat(2, Float.parseFloat(scoringTotale.getText().trim()));
            updateStmt.setInt(3, Integer.parseInt(nombreScore.getText().trim()));
            updateStmt.setInt(4, Integer.parseInt(nombreLignes.getText().trim()));
            updateStmt.setFloat(5, Float.parseFloat(tailleLigne.getText().trim()));
            updateStmt.setString(6, rappelQuestion.getText().trim());
            updateStmt.setString(7, section.getIdSection());

            int rowsAffected = updateStmt.executeUpdate();
            if (rowsAffected <= 0) {
                System.err.println("Aucun enregistrement mis à jour pour cette section.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de la mise à jour de la question: " + e.getMessage());
        }
    }


    private void updateSection() {
        updateQuestion();
        this.section.getDevoir().getController().fetchAndUpdateTableView();
        Stage stage = (Stage) cancelQuestion.getScene().getWindow();
        stage.close();
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
    public void handleInputFloat(KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        String currentText = textField.getText();

        // Keep only valid float characters (digits and a single dot)
        String sanitizedText = currentText.replaceAll("[^\\d.]", "");

        // Allow only one decimal point
        int firstDotIndex = sanitizedText.indexOf(".");
        if (firstDotIndex != -1) {
            sanitizedText = sanitizedText.substring(0, firstDotIndex + 1)
                    + sanitizedText.substring(firstDotIndex + 1).replaceAll("\\.", "");
        }

        textField.setText(sanitizedText);
        textField.positionCaret(sanitizedText.length());
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
            System.err.println("Error inserting Section data: " + e.getMessage());
        }
    }

    private void createQuestion() {
        String insertQCUQuery = "INSERT INTO QuestionLibre (question, scoreTotal,nombreScore, nombreLigne, tailleLigne, rappel, sectionID) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = SqlConnection.getConnection();
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
                System.err.println("Failed to insert QCU data.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error inserting QCU data: " + e.getMessage());
        }
    }

    public void setSectionUpdating(Section section) {
        this.section = section;
        this.enonceQuestion.setText(section.getIdSection());
        loadQuestionFromSectionId(section.getIdSection());
    }

    private void loadQuestionFromSectionId(String idSection) {
        String fetchQCUQuery = "SELECT * FROM QuestionLibre WHERE sectionID = ?";

        try (Connection connection = SqlConnection.getConnection();
             PreparedStatement qcuStatement = connection.prepareStatement(fetchQCUQuery)){

            qcuStatement.setString(1, idSection);
            ResultSet qcuResultSet = qcuStatement.executeQuery();
            if (qcuResultSet.next()) {
                questionLibre = new QuestionLibre();
                questionLibre.setIdSection(qcuResultSet.getString("sectionID"));
                questionLibre.setQuestion(qcuResultSet.getString("question"));
                questionLibre.setRappel(qcuResultSet.getString("rappel"));
                questionLibre.setTailleLigne(qcuResultSet.getFloat("tailleLigne"));
                questionLibre.setNombreLigne(qcuResultSet.getInt("nombreLigne"));
                questionLibre.setNombreScore(qcuResultSet.getInt("nombreScore"));
                questionLibre.setScoreTotal(qcuResultSet.getFloat("scoreTotal"));

                enonceQuestion.setText(questionLibre.getQuestion());
                rappelQuestion.setText(questionLibre.getRappel());
                tailleLigne.setText(String.valueOf(questionLibre.getTailleLigne()));
                nombreLignes.setText(String.valueOf(questionLibre.getNombreLigne()));
                nombreScore.setText(String.valueOf(questionLibre.getNombreScore()));
                scoringTotale.setText(String.valueOf(questionLibre.getScoreTotal()));

            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error loading QCU data: " + e.getMessage());
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
