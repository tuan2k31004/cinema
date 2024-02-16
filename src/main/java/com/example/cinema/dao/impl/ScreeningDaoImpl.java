package com.example.cinema.dao.impl;

import com.example.cinema.dao.ScreeningDao;
import com.example.cinema.entity.Movie;
import com.example.cinema.entity.Reservation;
import com.example.cinema.entity.Screening;
import com.example.cinema.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ScreeningDaoImpl extends CrudDaoImpl<Screening, Integer> implements ScreeningDao {

    @Override
    public Optional<String> findScreeningDuplicate(Screening screening) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        String name = null;
        try {
            transaction = session.beginTransaction();

            String queryString = "select s.movie.name from Screening s where s.auditorium=:auditorium and" +
                    " ((s.start>=:start and s.start<=:end) or (s.end>=:start and s.end<=:end) or" +
                    " (s.start<=:start and s.end>=:end)) and s.id!=:id";
            Query<String> query = session.createQuery(queryString, String.class)
                    .setParameter("auditorium", screening.getAuditorium())
                    .setParameter("start", screening.getStart())
                    .setParameter("end", screening.getEnd())
                    .setParameter("id", screening.getId());
            name = query.uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return Optional.ofNullable(name);
    }

    @Override
    public List<Screening> findScreeningByDateAndMovie(LocalDate localDate, Movie movie) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Screening> screens = new ArrayList<>();
        try {
            transaction = session.beginTransaction();

            String queryString = "select s from Screening s where s.movie=:movie and s.start>=:start and s.start<:end" +
                    " and s.start>=:now order by s.start";
            screens = session.createQuery(queryString, Screening.class)
                    .setParameter("start", localDate.atStartOfDay())
                    .setParameter("end", localDate.atTime(LocalTime.MAX))
                    .setParameter("movie", movie)
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
        return screens;
    }
}
