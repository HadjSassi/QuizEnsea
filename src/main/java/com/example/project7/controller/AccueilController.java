package com.example.project7.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class AccueilController implements Initializable {
    @FXML
    private Pagination p;

    private List<String> list = new ArrayList<String>();

    //ImageView imageView;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

//            list.add("/com/example/project7/images/1.jpg");
            list.add("images/1.jpg");
            list.add("images/2.jpg");
            list.add("images/3.jpg");
            list.add("images/4.jpg");
            list.add("images/5.jpg");
            list.add("images/6.jpg");
            list.add("images/7.jpg");


        Image images[] = new Image[list.size()];
        for (int i = 0; i < list.size(); i++) {
            try {
                images[i] = new Image( getClass().getResource("/com/example/project7/"+list.get(i)).toURI().toString());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        p.setPageFactory((Integer pageIndex) -> {
            ImageView im = new ImageView(images[pageIndex]);
            im.setFitHeight(515);
            im.setFitWidth(793);
            return im;
        });

        //imageview.setCursor(Cursor.CLOSED_HAND);
        Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            if (p.getCurrentPageIndex() < list.size() - 1) {
                int pos = (p.getCurrentPageIndex() + 1) % p.getPageCount();
                p.setCurrentPageIndex(pos);
            } else
                p.setCurrentPageIndex(0);
        }));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();
    }
}
