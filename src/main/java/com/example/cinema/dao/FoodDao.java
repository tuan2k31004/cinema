package com.example.cinema.dao;

import com.example.cinema.dao.impl.FoodDaoImpl;
import com.example.cinema.entity.Food;
import com.example.cinema.entity.Reservation;

public interface FoodDao extends CrudDao<Food,Integer> {
    FoodDao instance = new FoodDaoImpl();
}

