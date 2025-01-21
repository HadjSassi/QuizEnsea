package com.example.project7.controller.edition;

import com.example.project7.model.Reponse;
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

import java.net.URL;
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

    private String identifierSection;

    public void setIdentifiantSection(String identifierSection) {
        this.identifierSection = identifierSection;
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

    private int verifyBaremePos() {
        if (baremePos.getText() == null || baremePos.getText().trim().equals("")) {
            return 1;
        }
        try {
            return Integer.parseInt(baremePos.getText().trim());
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private int verifyBaremeNeg() {
        if (baremeNegDefault.getText() == null || baremeNegDefault.getText().trim().equals("")) {
            return 0;
        }
        try {
            return Integer.parseInt(baremeNegDefault.getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @FXML
    public void handleInputNumber(KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        String currentText = textField.getText();

        String sanitizedText = currentText.replaceAll("[^\\d]", "");

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
        boolean correctQuestion = verifyQuesstion();
        boolean correctReponseCorrect = verifyReponseCorrect();
        int baremePos = verifyBaremePos();
        int baremeNeg = verifyBaremeNeg();
        ObservableList<Reponse> reponsesList = incorrectTableView.getItems();
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
}
