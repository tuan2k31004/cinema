package com.example.cinema.controller;

import com.example.cinema.CinemaApplication;
import com.example.cinema.entity.Screening;
import com.example.cinema.viewFactory.AppViewFactory;
import com.example.cinema.viewFactory.MovieTicketViewFactory;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.SneakyThrows;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class MovieTicketController implements Initializable {
    @FXML
    private FlowPane listTime;
    @FXML
    private FlowPane listNextDay;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (int i = 0; i < 14; i++) {
            LocalDate localDate = LocalDate.now().plusDays(i);
            FXMLLoader fxmlLoader = new FXMLLoader(CinemaApplication.class.getResource("fxml/client_view/card_date.fxml"));
            Node node = fxmlLoader.load();
            ((CardDateController) fxmlLoader.getController()).setLocalDate(localDate);
            listNextDay.getChildren().add(node);
        }
        MovieTicketViewFactory.instance.screeningsProperty().addListener((observableValue, screenings, t1) -> {
            listTime.getChildren().clear();
            t1.forEach(screening -> {
                LocalTime localTime = screening.getStart().toLocalTime();
                JFXButton jfxButton = new JFXButton(localTime.toString());
                jfxButton.getStyleClass().add("card_time");

                jfxButton.setOnAction(actionEvent -> {
                    showBooking(screening);
                });

                listTime.getChildren().add(jfxButton);
            });
        });
    }

    @SneakyThrows
    private void showBooking(Screening screening) {
        AppViewFactory.instance.closeDialog();
        FXMLLoader fxmlLoader = new FXMLLoader(CinemaApplication.class.getResource("fxml/client_view/booking.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ((BookingController) fxmlLoader.getController()).setScreening(screening);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOnCloseRequest(windowEvent -> {
            windowEvent.consume();
            AppViewFactory.instance.showConfirmation("Bạn chắc chắn muốn hủy vé", stage::close);
        });
        stage.showAndWait();
    }

}
