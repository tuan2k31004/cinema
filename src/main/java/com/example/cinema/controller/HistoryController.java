package com.example.cinema.controller;

import com.example.cinema.CinemaApplication;
import com.example.cinema.entity.Reservation;
import com.example.cinema.viewFactory.MovieTicketViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {
    @FXML
    private VBox listMovieUpcoming;
    @FXML
    private VBox listMovieWatched;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MovieTicketViewFactory.instance.getUpcoming().forEach(reservation -> {
            listMovieUpcoming.getChildren().add(getItem(reservation));
        });
        MovieTicketViewFactory.instance.getWatched().forEach(reservation -> {
            listMovieWatched.getChildren().add(getItem(reservation));
        });
    }

    @SneakyThrows
    private Node getItem(Reservation reservation) {
        FXMLLoader loader = new FXMLLoader(CinemaApplication.class.getResource("fxml/client_view/history_item.fxml"));
        Node node = loader.load();
        ((HistoryItemController) loader.getController()).setReservation(reservation);
        return node;
    }
}
