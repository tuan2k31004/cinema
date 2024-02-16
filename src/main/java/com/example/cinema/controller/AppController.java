package com.example.cinema.controller;


import com.example.cinema.viewFactory.AppViewFactory;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {
    private final AppViewFactory appViewFactory = AppViewFactory.instance;
    @FXML
    private Label lbTitle;
    @FXML
    public JFXDrawer drawer;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private Pane pBody;
    @FXML
    private Pane pMenu;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hamburger.setOnMouseClicked(mouseEvent -> {
            if (drawer.isClosed()) {
                pMenu.setVisible(true);
                drawer.open();
            } else {
                drawer.close();
            }
        });
        drawer.setOnDrawerClosed(jfxDrawerEvent -> pMenu.setVisible(false));
        drawer.setOnMouseClicked(Event::consume);
        pMenu.setOnMouseClicked(mouseEvent -> {
            if (drawer.isOpened()) {
                drawer.close();
            }
        });
        appViewFactory.selectedViewNameProperty().addListener((observableValue, s, t1) -> {
            AppViewFactory.instance.loadData(() -> {
                pBody.getChildren().clear();
                switch (t1) {
                    case "movie_management" -> {
                        pBody.getChildren().add(appViewFactory.getMovieManagementView());
                        lbTitle.setText("Quản lý phim");
                    }
                    case "screening_management" -> {
                        pBody.getChildren().add(appViewFactory.getScreeningManagementView());
                        lbTitle.setText("Quản lý lịch chiếu");
                    }
                    case "food_management" -> {
                        pBody.getChildren().add(appViewFactory.getFoodManagementView());
                        lbTitle.setText("Quản lý đồ ăn");
                    }
                    case "auditorium_management" -> {
                        pBody.getChildren().add(appViewFactory.getAuditoriumManagementView());
                        lbTitle.setText("Quản lý phòng chiếu");
                    }
                    case "screening_movies" -> {
                        pBody.getChildren().add(appViewFactory.getScreeningMoviesView());
                        lbTitle.setText("Phim đang chiếu");
                    }
                    case "history" -> {
                        pBody.getChildren().add(appViewFactory.getHistoryView());
                        lbTitle.setText("Vé của tôi");
                    }
                }
            }, null);
        });
    }
}
