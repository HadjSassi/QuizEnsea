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

import static mysql_connection.MySqlConnection.getOracleConnection;


public class Main extends Application {

    public static void main(String[] args) {

        //todo here to add the constraint to make the database and the necessary configuration before execution!
        /*String filePath = "./.init";
        if (!Files.exists(Paths.get(filePath))) {
            try {
                String query = "";

                //table abonnements (idAbonnmenet, creationDate)

                query = "create table Abonnement (idAbonnement int auto_increment primary key , creationDate date);";

                try (Connection connection = getOracleConnection();
                     Statement statement = connection.createStatement()) {

                    // Execute the SQL statement
                    statement.executeUpdate(query);

                    System.out.println("Table created successfully!");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //table lecteurs (cin, nom, prenom, credit, idAbonnement)

                query = "create table Lecteur(cin varchar(9) primary key, nom varchar(30) not null , prenom varchar(30) not null , credit numeric, idAbonnement numeric);";

                try (Connection connection = getOracleConnection();
                     Statement statement = connection.createStatement()) {

                    // Execute the SQL statement
                    statement.executeUpdate(query);

                    System.out.println("Table created successfully!");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //table clientFidele (cin, email, preferences)

                query = "create table ClientFidele (cin varchar(9) primary key , email varchar(30), preferences text);";

                try (Connection connection = getOracleConnection();
                     Statement statement = connection.createStatement()) {

                    // Execute the SQL statement
                    statement.executeUpdate(query);

                    System.out.println("Table created successfully!");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //table livres (codeUnique, titre, auteur, isbn)

                query = "create table Livre(codeunique int auto_increment primary key, titre varchar(50), auteur varchar(50), isbn long);\n";

                try (Connection connection = getOracleConnection();
                     Statement statement = connection.createStatement()) {

                    // Execute the SQL statement
                    statement.executeUpdate(query);

                    System.out.println("Table created successfully!");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //table romantique(codeUnique, history, personnagePrincipal)

                query = "create table Romantique(codeunique int primary key, history text, personnageprincipal varchar(50));";

                try (Connection connection = getOracleConnection();
                     Statement statement = connection.createStatement()) {

                    // Execute the SQL statement
                    statement.executeUpdate(query);

                    System.out.println("Table created successfully!");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //table Police(codeUnique, descriptif, detectivd, victime)

                query = "create table Police (codeunique int primary key, descriptif text, detective varchar(50), victime varchar(50));";

                try (Connection connection = getOracleConnection();
                     Statement statement = connection.createStatement()) {

                    // Execute the SQL statement
                    statement.executeUpdate(query);

                    System.out.println("Table created successfully!");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //table sciencesFiction (codeUnique, annee, espace)

                query = "create table SciencesFicition (codeunique int primary key, annee int ,espace varchar(50));";

                try (Connection connection = getOracleConnection();
                     Statement statement = connection.createStatement()) {

                    // Execute the SQL statement
                    statement.executeUpdate(query);

                    System.out.println("Table created successfully!");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //table detailsEmprunts (idDetail,codeunique, dateEmprunt, dateRetour)

                query = "create table DetailsEmprunts(idDetail int auto_increment primary key, codeunique int, dateEmprunt date, returned bool);";

                try (Connection connection = getOracleConnection();
                     Statement statement = connection.createStatement()) {

                    // Execute the SQL statement
                    statement.executeUpdate(query);

                    System.out.println("Table created successfully!");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //table emprunts (idAbonnement, iddetails)

                query = "create table Emprunts(idAbonnemen int, idDetails int);";

                try (Connection connection = getOracleConnection();
                     Statement statement = connection.createStatement()) {

                    // Execute the SQL statement
                    statement.executeUpdate(query);

                    System.out.println("Table created successfully!");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Create the file telling that everything is set up
                Files.createFile(Paths.get(filePath));
                System.out.println("File created.");
            } catch (Exception e) {
                // Handle the exception if unable to create the file
                System.err.println("Error creating the file: " + e.getMessage());
            }
        }*/
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
