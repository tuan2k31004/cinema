package com.example.cinema.dao;

import com.example.cinema.dao.impl.AuditoriumDaoImpl;
import com.example.cinema.entity.Auditorium;
import com.example.cinema.entity.Movie;


public interface AuditoriumDao extends CrudDao<Auditorium,Integer> {
    AuditoriumDao instance = new AuditoriumDaoImpl();
    boolean hasUpcomingScreening(Auditorium auditorium);
}


