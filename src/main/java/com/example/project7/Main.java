package com.example.project7;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static sql_connection.DataBase.createDatabaseIfDoesNotExist;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage stage = null;
    private double x, y;
    Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception {
        createDatabaseIfDoesNotExist();
        Parent root = FXMLLoader.load(getClass().getResource("Boarder.fxml"));
        window = primaryStage;
        this.stage = primaryStage;
        Scene scene = new Scene(root);
        window.setScene(scene);
        //set stage borderless
        window.initStyle(StageStyle.TRANSPARENT);
        /*root.setOnMousePressed(new EventHandler<MouseEvent>() {
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
        });*/
        //primaryStage.setFullScreen(true);
        scene.getStylesheets().add(getClass().getResource("/com/example/project7/css/styles.css").toExternalForm());

        primaryStage.setMaximized(false);
        primaryStage.getIcons().add((new Image(getClass().getResource("/com/example/project7/images/ensea2.png").toURI().toString())));
        primaryStage.show();
        scene.setFill(Color.TRANSPARENT);
        //scene.getStylesheets().add(getClass().getResource("css/styles.css").toExternalForm());
        //Application.setUserAgentStylesheet(getClass().getResource("css/styles.css").toExternalForm());
    }

}
