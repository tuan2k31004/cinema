package com.example.cinema.dao;

import com.example.cinema.dao.impl.ScreeningDaoImpl;
import com.example.cinema.entity.Movie;
import com.example.cinema.entity.Reservation;
import com.example.cinema.entity.Screening;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScreeningDao extends CrudDao<Screening,Integer> {
    ScreeningDao instance = new ScreeningDaoImpl();
    Optional<String> findScreeningDuplicate(Screening screening);
    List<Screening> findScreeningByDateAndMovie(LocalDate localDate, Movie movie);
}
