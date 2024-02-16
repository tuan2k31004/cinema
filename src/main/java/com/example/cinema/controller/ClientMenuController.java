package com.example.cinema.controller;

import com.example.cinema.viewFactory.AppViewFactory;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientMenuController implements Initializable {
    private final AppViewFactory appViewFactory = AppViewFactory.instance;
    @FXML
    private JFXButton btnMovie;
    @FXML
    private JFXButton btnHistory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnMovie.setOnAction(mouseEvent -> {
            appViewFactory.setSelectedViewName("screening_movies");
            appViewFactory.setButtonSelected(btnMovie);
        });
        btnHistory.setOnAction(mouseEvent -> {
            appViewFactory.setSelectedViewName("history");
            appViewFactory.setButtonSelected(btnHistory);
        });
        btnMovie.fire();
    }
}
