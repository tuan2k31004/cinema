package com.example.cinema.dao;


import com.example.cinema.dao.impl.MovieDaoImpl;
import com.example.cinema.entity.Movie;

import java.util.List;

public interface MovieDao extends CrudDao<Movie,Integer> {
    MovieDao instance = new MovieDaoImpl();
    boolean hasUpcomingScreening(Movie movie);
    List<Movie> findMovieCurrent();
}
