package com.example.cinema.controller;

import com.example.cinema.CinemaApplication;
import com.example.cinema.viewFactory.AccountViewFactory;
import com.example.cinema.viewFactory.AppViewFactory;
import com.example.cinema.viewFactory.MovieTicketViewFactory;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MenuController implements Initializable {
    @FXML
    private Label lbGreet;
    @FXML
    private JFXButton btnChangePw;
    @FXML
    private VBox menu;
    private final AppViewFactory appViewFactory = AppViewFactory.instance;

    public void logout(MouseEvent mouseEvent) {
        appViewFactory.showConfirmation("Bạn có chắc chắn muốn đăng xuất không?", () -> {
            appViewFactory.close();
            MovieTicketViewFactory.instance.logout();
            AccountViewFactory.instance.show();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appViewFactory.userCurrentProperty().addListener((observableValue, user, t1) -> {
            menu.getChildren().clear();
            switch (t1.getRole()) {
                case ADMIN -> menu.getChildren().add(appViewFactory.getMenuAdmin());
                case CLIENT -> menu.getChildren().add(appViewFactory.getMenuClient());
            }
        });
        btnChangePw.setOnAction(actionEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(CinemaApplication.class.getResource("fxml/change_password.fxml"));
            try {
                AppViewFactory.instance.showDialog(fxmlLoader.load(), () -> {
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        Platform.runLater(() -> {
            lbGreet.setText("HELLO " + appViewFactory.getUserCurrent().getUsername().toUpperCase());
        });
    }
}
