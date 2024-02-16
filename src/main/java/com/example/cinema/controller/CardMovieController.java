package com.example.cinema.controller;

import com.example.cinema.CinemaApplication;
import com.example.cinema.entity.Movie;
import com.example.cinema.viewFactory.AppViewFactory;
import com.example.cinema.viewFactory.MovieTicketViewFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.StringJoiner;

public class CardMovieController implements Initializable {
    @FXML
    private Label lbName;
    @FXML
    private Label lbGenres;
    @FXML
    private ImageView imgThumbnail;
    @FXML
    private Label lbDuration;
    @Setter
    private Movie movie;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            lbName.setText(movie.getName());
            StringJoiner stringJoiner = new StringJoiner(", ");
            movie.getGenres().forEach(genre -> {
                stringJoiner.add(genre.getTitle());
            });
            lbGenres.setText(stringJoiner.toString());
            lbDuration.setText(String.format("%s phút", movie.getDuration()));
            Image image = new Image(movie.getThumbnail());
            imgThumbnail.setImage(image);
        });
    }

    public void showBookingMovieTicket(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(CinemaApplication.class
                .getResource("fxml/client_view/movie_ticket.fxml"));
        MovieTicketViewFactory.instance.setMovie(movie);
        AppViewFactory.instance.showDialog(loader.load(), () -> {
            MovieTicketViewFactory.instance.setScreenings(new ArrayList<>());
        });
    }
}
