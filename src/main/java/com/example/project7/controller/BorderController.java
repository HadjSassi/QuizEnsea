package com.example.project7.controller;

import com.example.project7.FxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ResourceBundle;


public class BorderController implements Initializable {
    private double xOffSet = 0;
    private double yOffSet = 0;


    Stage stage;
    @FXML
    private AnchorPane all;
    @FXML
    private AnchorPane  border;

    @FXML
    private AnchorPane mainscreen;

    @FXML
    void handleClicksClose(ActionEvent event) {
        stage = (Stage) all.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleClicksMinimize(ActionEvent event) {
        stage = (Stage) all.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FxmlLoader object = new FxmlLoader();
        AnchorPane view = object.getPane("Barre");
        mainscreen.getChildren().removeAll();
        mainscreen.getChildren().setAll(view);
    }

}
