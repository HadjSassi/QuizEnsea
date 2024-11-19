package com.example.project7.controller;

import com.example.project7.FxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class InterfaceController implements Initializable {

    @FXML
    private AnchorPane anchorpane1;

    @FXML
    private BorderPane mainPane;

    @FXML
    private AnchorPane anchorpane3;

    @FXML
    private AnchorPane anchorpane2;

    @FXML
    public Label name;


    @FXML
    void handleClicksAccueil(ActionEvent event) {
        FxmlLoader object = new FxmlLoader();
        Parent view = object.getPane("Accueil");
        //view.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        anchorpane3.getChildren().removeAll();
        anchorpane3.getChildren().setAll(view);

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FxmlLoader object = new FxmlLoader();
        Parent view = object.getPane("Accueil");
        //view.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        anchorpane3.getChildren().removeAll();
        anchorpane3.getChildren().setAll(view);
    }


}
