package com.example.project7.controller.edition;

import com.example.project7.FxmlLoader;
import com.example.project7.model.Controle;
import com.example.project7.model.RowTableSection;
import com.example.project7.model.Section;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import sql_connection.SqlConnection;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditerSection implements Initializable {

    private static int numberOfSection = 0;

    private Object currentController;

    private AnchorPane parentPane;

    private Controle devoir;

    @FXML
    private TextField identifiantSection;

    @FXML
    private SplitMenuButton typeSection;

    @FXML
    private Button ajouterSection;

    @FXML
    private Button cancelSection;

    @FXML
    private AnchorPane sectionPane;

    public void setDevoir(Controle devoir) {
        this.devoir = devoir;
        updateSection( identifiantSection.getText());
        initializeNumberOfSection();
        numberOfSection++;
        loadContentToSectionPane("_7_EditerDescription");
    }

    public void setParentPane(AnchorPane parentPane) {
        this.parentPane = parentPane;
    }

    private Object getCurrentController() {
        return currentController;
    }

    @FXML
    public void handleInputIdentifier(KeyEvent event) {
        updateSection( identifiantSection.getText());

        if (identifiantSection.getText().trim().equals("")) {
            String sectionId = "Section#" + numberOfSection + "_" + devoir.getIdControle() ;
            updateSection(sectionId);
            //identifiantSection.setText(sectionId);
        }

    }

    private void updateSection(String identifierText) {
        Object controller = getCurrentController();
        Section section = new Section();
        section.setDevoir(this.devoir);
        section.setIdSection(identifierText);
        section.setOrdreSection(numberOfSection);

        if (controller instanceof EditerQCU) {
            ((EditerQCU) controller).setSection(section);
        } else if (controller instanceof EditerQCM) {
            ((EditerQCM) controller).setSection(section);
        } else if (controller instanceof EditerQuestion) {
            ((EditerQuestion) controller).setSection(section);
        } else if (controller instanceof EditerDescription) {
            ((EditerDescription) controller).setSection(section);
        }
    }

    private void loadContentToSectionPane(String fxmlFileName) {
        try {
            FxmlLoader loader = new FxmlLoader();
            AnchorPane newContent = loader.getPane("editer_quiz/" + fxmlFileName);

            if (newContent != null) {
                sectionPane.getChildren().setAll(newContent);

                String currentId = identifiantSection.getText().trim();
                if (currentId.isEmpty()) {
                    currentId = "Section#" + numberOfSection + "_" + devoir.getIdControle();
                }
                currentController = loader.getController();
                updateSection(currentId);
                identifiantSection.setText(currentId);

            } else {
                System.err.println("Content of " + fxmlFileName + " couldn't be loaded.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleClicksAddQCM(ActionEvent event) {
        loadContentToSectionPane("_4_EditerQCM");
        typeSection.setText("QCM");

    }

    @FXML
    public void handleClicksAddQCU(ActionEvent event) {
        loadContentToSectionPane("_5_EditerQCU");
        typeSection.setText("QCU");
    }

    @FXML
    public void handleClicksAddFreeQuestion(ActionEvent event) {
        loadContentToSectionPane("_6_EditerQuestionLibre");
        typeSection.setText("Free Question");
    }

    @FXML
    public void handleClicksAddFreeDescription(ActionEvent event) {
        loadContentToSectionPane("_7_EditerDescription");
        typeSection.setText("Description");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    private void initializeNumberOfSection() {
        String fetchSectionsQuery = "SELECT idSection FROM Section WHERE controleID = ? ORDER BY ordreSection";
        String updateOrderQuery = "UPDATE Section SET ordreSection = ? WHERE idSection = ?";

        try (Connection connection = SqlConnection.getConnection();
             PreparedStatement fetchStatement = connection.prepareStatement(fetchSectionsQuery);
             PreparedStatement updateStatement = connection.prepareStatement(updateOrderQuery)) {

            fetchStatement.setInt(1, this.devoir.getIdControle());
            try (ResultSet resultSet = fetchStatement.executeQuery()) {
                int newOrder = 1;
                // Start iterating through the sections
                while (resultSet.next()) {
                    String idSection = resultSet.getString("idSection");

                    // Prepare the update statement
                    updateStatement.setInt(1, newOrder); // Set the new ordreSection value
                    updateStatement.setString(2, idSection); // Set the idSection value

                    // Add the update to batch
                    updateStatement.addBatch();

                    // Increment the order
                    newOrder++;
                }

                // Execute the batch update to save everything in one go
                updateStatement.executeBatch();
                numberOfSection = newOrder-1;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void cancelSection() {
        numberOfSection--;
    }

    public void handleClicksModifyQCM(RowTableSection selection) {
        loadContentToSectionPane("_4_EditerQCM");
        if (currentController instanceof EditerQCM) {
            EditerQCM controller = (EditerQCM) currentController;

            if (identifiantSection.getText() != null && !identifiantSection.getText().trim().isEmpty()) {
                Section section = new Section();
                section.setIdSection(selection.getIdSection());
                section.setDevoir(this.devoir);
                this.identifiantSection.setText(section.getIdSection());
                controller.setSectionUpdating(section);
            }
        } else {
            System.err.println("Current controller is not an instance of EditerQCU.");
        }
    }

    public void handleClicksModifyQCU(RowTableSection selection) {
        loadContentToSectionPane("_5_EditerQCU");
        if (currentController instanceof EditerQCU) {
            EditerQCU controller = (EditerQCU) currentController;

            if (identifiantSection.getText() != null && !identifiantSection.getText().trim().isEmpty()) {
                Section section = new Section();
                section.setIdSection(selection.getIdSection());
                section.setDevoir(this.devoir);
                this.identifiantSection.setText(section.getIdSection());
                controller.setSectionUpdating(section);
            }
        } else {
            System.err.println("Current controller is not an instance of EditerQCU.");
        }
    }

    public void handleClicksModifyFreeQuestion(RowTableSection selection) {
        loadContentToSectionPane("_6_EditerQuestionLibre");
        if (currentController instanceof EditerQuestion) {
            EditerQuestion controller = (EditerQuestion) currentController;

            if (identifiantSection.getText() != null && !identifiantSection.getText().trim().isEmpty()) {
                Section section = new Section();
                section.setIdSection(selection.getIdSection());
                section.setDevoir(this.devoir);
                this.identifiantSection.setText(section.getIdSection());
                controller.setSectionUpdating(section);
            }
        } else {
            System.err.println("Current controller is not an instance of EditerQCU.");
        }
    }

    public void handleClicksModifyFreeDescription(RowTableSection selection) {
        loadContentToSectionPane("_7_EditerDescription");
        if (currentController instanceof EditerDescription) {
            EditerDescription controller = (EditerDescription) currentController;

            if (identifiantSection.getText() != null && !identifiantSection.getText().trim().isEmpty()) {
                Section section = new Section();
                section.setIdSection(selection.getIdSection());
                section.setDevoir(this.devoir);
                this.identifiantSection.setText(section.getIdSection());
                controller.setSectionUpdating(section);
            }
        } else {
            System.err.println("Current controller is not an instance of EditerQCU.");
        }
    }

    public void loadSectionData(RowTableSection section) {
        if (section != null) {
            switch (section.getType()){
                case "QCU":
                    handleClicksModifyQCU(section);
                    break;
                case "QCM":
                    handleClicksModifyQCM(section);
                    break;
                case "QuestionLibre":
                    handleClicksModifyFreeQuestion(section);
                    break;
                case "Description":
                    handleClicksModifyFreeDescription(section);
                    break;
                default:
                    System.out.println("OUCHY!!");

            }
        }
    }

    @FXML
    private void ProjectTypeButton(ActionEvent event){
        typeSection.show();
    }
}
