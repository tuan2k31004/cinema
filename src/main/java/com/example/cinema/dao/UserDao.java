package com.example.cinema.dao;

import com.example.cinema.dao.impl.UserDaoImpl;
import com.example.cinema.entity.Role;
import com.example.cinema.entity.User;

import java.util.Optional;

public interface UserDao extends CrudDao<User,String> {
    UserDao instance = new UserDaoImpl();
    Optional<User> checkUser(User user);
    boolean updatePassword(String newPw);
}
