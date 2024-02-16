package com.example.cinema.controller;

import com.example.cinema.CinemaApplication;
import com.example.cinema.dao.MovieDao;
import com.example.cinema.entity.Movie;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.ResourceBundle;

public class ScreeningMovieController implements Initializable {
    @FXML
    private FlowPane body;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MovieDao.instance.findMovieCurrent().forEach(movie -> {
            body.getChildren().add(getCardMovie(movie));
        });
    }

    @SneakyThrows
    private Node getCardMovie(Movie movie) {
        FXMLLoader fxmlLoader = new FXMLLoader(CinemaApplication.class.getResource("fxml/client_view/card_movie.fxml"));
        Node node = fxmlLoader.load();
        ((CardMovieController) fxmlLoader.getController()).setMovie(movie);
        return node;
    }
}
