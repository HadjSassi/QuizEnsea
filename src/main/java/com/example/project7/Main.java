package com.example.project7;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;

import static mysql_connection.DataBase.createDatabaseIfDoesNotExist;
import static mysql_connection.MySqlConnection.getOracleConnection;


public class Main extends Application {

    public static void main(String[] args) {

        createDatabaseIfDoesNotExist();
        launch(args);
    }

    public static Stage stage = null;
    private double x, y;
    Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Boarder.fxml"));
        window = primaryStage;
        this.stage = primaryStage;
        Scene scene = new Scene(root);
        window.setScene(scene);
        //set stage borderless
        window.initStyle(StageStyle.TRANSPARENT);
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                x = event.getSceneX();
                y = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - x);
                primaryStage.setY(event.getScreenY() - y);
            }
        });
        //primaryStage.setFullScreen(true);
        primaryStage.setMaximized(false);
        primaryStage.getIcons().add((new Image(getClass().getResource("/com/example/project7/images/ensea2.png").toURI().toString())));
        primaryStage.show();
        scene.setFill(Color.TRANSPARENT);
    }

}
