package com.example.cinema.controller;

import com.example.cinema.viewFactory.AccountViewFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AccountController implements Initializable {
    private final AccountViewFactory accountViewFactory = AccountViewFactory.instance;
    @FXML
    private StackPane body;
    @FXML
    private StackPane slideshow;
    private List<String> urls = List.of(
            "https://www.cgv.vn/media/catalog/product/cache/1/thumbnail/190x260/2e2b8cd282892c71872b9e67d2cb5039/7/0/700x1000_19_.jpg",
            "https://www.cgv.vn/media/catalog/product/cache/1/thumbnail/190x260/2e2b8cd282892c71872b9e67d2cb5039/7/0/700x1000-exorcist_18_.jpg",
            "https://www.cgv.vn/media/catalog/product/cache/1/thumbnail/190x260/2e2b8cd282892c71872b9e67d2cb5039/7/0/700x1000-wolfoo.jpg",
            "https://www.cgv.vn/media/catalog/product/cache/1/thumbnail/190x260/2e2b8cd282892c71872b9e67d2cb5039/p/r/pr_poster_-_main_-_cobweb_1_.jpg",
            "https://www.cgv.vn/media/catalog/product/cache/1/thumbnail/190x260/2e2b8cd282892c71872b9e67d2cb5039/4/7/470wx700h_1_1_.jpg"
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        urls.forEach(s -> {
            ImageView imageView = new ImageView();
            imageView.setFitWidth(280);
            imageView.setFitHeight(400);
            Image image = new Image(s);
            imageView.setImage(image);
            imageView.setSmooth(true);
            slideshow.getChildren().add(imageView);
        });

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            slideshow.getChildren().get(0).toFront();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        accountViewFactory.selectedViewNameProperty().addListener((observableValue, s, t1) -> {
            body.getChildren().clear();
            switch (t1) {
                case "signup" -> body.getChildren().add(accountViewFactory.getSignupView());
                case "login" -> body.getChildren().add(accountViewFactory.getLoginView());
            }
        });
        accountViewFactory.setSelectedViewName("login");
    }

}
