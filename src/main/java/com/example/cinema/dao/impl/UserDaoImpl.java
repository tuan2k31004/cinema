package com.example.cinema.dao.impl;

import com.example.cinema.dao.UserDao;
import com.example.cinema.entity.Role;
import com.example.cinema.entity.User;
import com.example.cinema.util.HibernateUtil;
import com.example.cinema.viewFactory.AppViewFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Optional;

public class UserDaoImpl extends CrudDaoImpl<User,String> implements UserDao {
    @Override
    public Optional<User> checkUser(User user) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        User userResponse = null;
        try {
            transaction = session.beginTransaction();
            String queryString = "select u from User u where binary(u.username)=:username and u.password=:password and u.role=:role";
            Query<User> query = session.createQuery(queryString, User.class)
                    .setParameter("username",user.getUsername())
                    .setParameter("password", user.getPassword())
                    .setParameter("role",user.getRole());
            userResponse = query.uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return Optional.ofNullable(userResponse);
    }

    @Override
    public boolean updatePassword(String newPw) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

           String queryString = "update User u set u.password = :newPw where u.username = :username";
           session.createQuery(queryString)
                   .setParameter("newPw",newPw)
                   .setParameter("username", AppViewFactory.instance.getUserCurrent().getUsername())
                   .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } finally {
            session.close();
        }
        return true;
    }
}
