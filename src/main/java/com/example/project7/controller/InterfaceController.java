package com.example.project7.controller;

import com.example.project7.FxmlLoader;
import com.example.project7.controller.edition.OpenProjet;
import com.example.project7.controller.edition.SelectionTypeProjet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;


public class InterfaceController implements Initializable {

    @FXML
    private AnchorPane anchorpane3;

    @FXML
    public Label name;

    @FXML
    void handleClicksAccueil(ActionEvent event) {
        FxmlLoader object = new FxmlLoader();
        Parent view = object.getPane("Home");
        anchorpane3.getChildren().removeAll();
        anchorpane3.getChildren().setAll(view);
    }

    @FXML
    void handleClicksNew(ActionEvent event) {
        FxmlLoader object = new FxmlLoader();
        Parent view = object.getPane("editer_quiz/_1_SelectionTypeDeProjet");
        SelectionTypeProjet controller = (SelectionTypeProjet) object.getController();
        if (controller != null) {
            controller.setParentPane(anchorpane3);
        }
        anchorpane3.getChildren().removeAll();
        anchorpane3.getChildren().setAll(view);
    }


    @FXML
    void handleClicksOpen(ActionEvent event) {
        FxmlLoader object = new FxmlLoader();
        Parent view = object.getPane("openinterf");
        OpenProjet controller = (OpenProjet) object.getController();
        if (controller != null) {
            controller.setParentPane(anchorpane3);
        }
        anchorpane3.getChildren().removeAll();
        anchorpane3.getChildren().setAll(view);
    }


    @FXML
    void handleClicksSetting(ActionEvent event) {
        FxmlLoader object = new FxmlLoader();
        Parent view = object.getPane("SettingInterf");
        anchorpane3.getChildren().removeAll();
        anchorpane3.getChildren().setAll(view);
    }


    @FXML
    void handleClicksHelp(ActionEvent event) {
        FxmlLoader object = new FxmlLoader();
        Parent view = object.getPane("HelpInterf");
        anchorpane3.getChildren().removeAll();
        anchorpane3.getChildren().setAll(view);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FxmlLoader object = new FxmlLoader();
        Parent view = object.getPane("Home");
        anchorpane3.getChildren().removeAll();
        anchorpane3.getChildren().setAll(view);
    }

}
