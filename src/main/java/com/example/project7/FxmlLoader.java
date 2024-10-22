package com.example.project7;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.net.URL;

public class FxmlLoader {
    private AnchorPane view;

    public AnchorPane getPane(String fileName){
        try {
            URL fileUrl = Main.class.getResource("" +fileName+".fxml");
            if(fileUrl == null){
                throw new java.io.FileNotFoundException("FXML file can't be found");
            }

            view = new FXMLLoader().load(fileUrl);
        }catch (Exception e){
            System.out.println("No page "+fileName+" please check sample.App.FxmlLoader.");
            e.printStackTrace();
        }
        return view;
    }
}
