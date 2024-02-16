package com.example.cinema.dao;

import com.example.cinema.dao.impl.ReservationDaoImpl;
import com.example.cinema.entity.Reservation;

import java.util.List;

public interface ReservationDao extends CrudDao<Reservation, Integer> {
    ReservationDao instance = new ReservationDaoImpl();
    List<Reservation> findUpcoming();
    List<Reservation> findWatched();
}
