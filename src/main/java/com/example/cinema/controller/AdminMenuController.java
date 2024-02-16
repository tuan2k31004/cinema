package com.example.cinema.controller;

import com.example.cinema.viewFactory.AppViewFactory;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
    private final AppViewFactory appViewFactory = AppViewFactory.instance;
    @FXML
    private JFXButton btnMovie;
    @FXML
    private JFXButton btnScreening;
    @FXML
    private JFXButton btnFood;
    @FXML
    private JFXButton btnAuditorium;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnMovie.setOnAction(mouseEvent -> {
            appViewFactory.setSelectedViewName("movie_management");
            appViewFactory.setButtonSelected(btnMovie);
        });
        btnScreening.setOnAction(mouseEvent -> {
            appViewFactory.setSelectedViewName("screening_management");
            appViewFactory.setButtonSelected(btnScreening);
        });
        btnFood.setOnAction(actionEvent -> {
            appViewFactory.setSelectedViewName("food_management");
            appViewFactory.setButtonSelected(btnFood);
        });
        btnAuditorium.setOnAction(actionEvent -> {
            appViewFactory.setSelectedViewName("auditorium_management");
            appViewFactory.setButtonSelected(btnAuditorium);
        });
        btnMovie.fire();
    }
}
