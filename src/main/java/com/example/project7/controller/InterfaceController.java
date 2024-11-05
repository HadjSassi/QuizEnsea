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
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class InterfaceController implements Initializable {

    @FXML
    private javafx.scene.control.Button g1;
    @FXML
    private javafx.scene.control.Button g2;
    @FXML
    private javafx.scene.control.Button g3;
    @FXML
    private javafx.scene.control.Button g4;
    @FXML
    private javafx.scene.control.Button g5;
    @FXML
    private ImageView i1;
    @FXML
    private ImageView i2;
    @FXML
    private ImageView i3;
    @FXML
    private ImageView i4;
    @FXML
    private ImageView i5;

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
    public ImageView img;

    Stage stage;

    @FXML
    void handleClicksAccueil(ActionEvent event) {
        FxmlLoader object = new FxmlLoader();
        Parent view = object.getPane("Accueil");
        //view.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        anchorpane3.getChildren().removeAll();
        anchorpane3.getChildren().setAll(view);

    }

    private double x, y;
    Stage window;

    @FXML
    void handleClicksLivre(ActionEvent event) {
        FxmlLoader object = new FxmlLoader();
        AnchorPane view = object.getPane("livre/LivreConsultation");
        view.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        anchorpane3.getChildren().removeAll();
        anchorpane3.getChildren().setAll(view);
    }

    @FXML
    void handleClicksLecteur(ActionEvent event) {
        FxmlLoader object = new FxmlLoader();
        Parent view = object.getPane("lecteur/LecteurConsultation");
        anchorpane3.getChildren().removeAll();
        anchorpane3.getChildren().setAll(view);
    }

    @FXML
    void handleClicksEmprunts(ActionEvent event) {
             /*FxmlLoader object = new FxmlLoader();
        Parent view = object.getPane("settings");
        anchorpane1.getChildren().removeAll();
        anchorpane1.getChildren().setAll(view);*/

        FxmlLoader object = new FxmlLoader();
        AnchorPane view = object.getPane("emprunt/EmpruntConsultation");
        view.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
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
