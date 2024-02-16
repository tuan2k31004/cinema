package com.example.cinema.dao.impl;

import com.example.cinema.dao.MovieDao;
import com.example.cinema.entity.Movie;
import com.example.cinema.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovieDaoImpl extends CrudDaoImpl<Movie, Integer> implements MovieDao {
    @Override
    public boolean hasUpcomingScreening(Movie movie) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        boolean res = false;
        try {
            transaction = session.beginTransaction();
            String queryString = "select count(*) from Screening s where s.movie=:movie and s.start>=:now";
            Query<Long> query = session.createQuery(queryString, Long.class)
                    .setParameter("movie", movie)
                    .setParameter("now", LocalDateTime.now());
            res = query.uniqueResult() > 0;
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return res;
    }

    @Override
    public List<Movie> findMovieCurrent() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Movie> movies = new ArrayList<>();
        try {
            transaction = session.beginTransaction();
            String queryString = "select m from Movie m inner join Screening s on m = s.movie where s.start>:now";
            movies = session.createQuery(queryString, Movie.class)
                    .setParameter("now", LocalDateTime.now())
                    .getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return movies;
    }
}
