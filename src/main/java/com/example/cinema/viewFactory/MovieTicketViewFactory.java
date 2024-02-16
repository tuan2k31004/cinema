package com.example.cinema.viewFactory;


import com.example.cinema.dao.ReservationDao;
import com.example.cinema.dao.ScreeningDao;
import com.example.cinema.entity.Movie;
import com.example.cinema.entity.Reservation;
import com.example.cinema.entity.Screening;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

import java.time.LocalDate;
import java.util.List;


public class MovieTicketViewFactory {
    public static MovieTicketViewFactory instance = new MovieTicketViewFactory();
    private final ObjectProperty<Node> dateSelected = new SimpleObjectProperty<>();

    public void setDateSelected(Node dateSelected) {
        this.dateSelected.set(dateSelected);
    }

    private final ObjectProperty<List<Screening>> screenings = new SimpleObjectProperty<>();

    public ObjectProperty<List<Screening>> screeningsProperty() {
        return screenings;
    }

    public void setScreenings(List<Screening> screenings) {
        this.screenings.set(screenings);
    }

    private final ObjectProperty<Movie> movie = new SimpleObjectProperty<>();

    public void setMovie(Movie movie) {
        this.movie.set(movie);
    }

    private List<Reservation> upcoming;
    private List<Reservation> watched;

    public List<Reservation> getUpcoming() {
        if (upcoming == null) {
            upcoming = ReservationDao.instance.findUpcoming();
        }
        return upcoming;
    }

    public boolean isUpcomingNull() {
        return upcoming == null;
    }

    public List<Reservation> getWatched() {
        if (watched == null) {
            watched = ReservationDao.instance.findWatched();
        }
        return watched;
    }

    public void logout() {
        upcoming = null;
        watched = null;
    }

    private MovieTicketViewFactory() {
        dateSelected.addListener((observableValue, node, t1) -> {
            if (node != null) {
                node.setStyle("");
            }
            t1.setStyle("-fx-border-color: black;-fx-border-radius: 5");
            AppViewFactory.instance.loadData(() -> {
                screenings.set(ScreeningDao.instance.
                        findScreeningByDateAndMovie((LocalDate) t1.getUserData(), movie.get()));
            }, null);
        });
    }
}
