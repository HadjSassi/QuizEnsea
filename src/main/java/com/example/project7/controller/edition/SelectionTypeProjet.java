package com.example.project7.controller.edition;

import com.example.project7.FxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class SelectionTypeProjet implements Initializable {

    @FXML
    private Button create;

    @FXML
    private Button cancel;

    @FXML
    private TextField name;

    @FXML
    private TextField location;

    @FXML
    private MenuButton typeProject;

    private AnchorPane parentPane;

    public void setParentPane(AnchorPane parentPane) {
        this.parentPane = parentPane;
    }

    @FXML
    public void handleClicksCreate(ActionEvent event) {
        //todo not this only!!!!!
        FxmlLoader object = new FxmlLoader();
        Parent view = object.getPane("editer_quiz/_2_EditerProjet");
        EditerProjet controller = (EditerProjet) object.getController();
        if (controller != null) {
            controller.setParentPane(parentPane);
        }
        if (parentPane != null) {
            parentPane.getChildren().removeAll();
            parentPane.getChildren().setAll(view);
        }
    }


    @FXML
    public void handleClicksCancel(ActionEvent event) {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
