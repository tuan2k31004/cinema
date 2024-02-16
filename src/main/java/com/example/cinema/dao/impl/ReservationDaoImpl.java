package com.example.cinema.dao.impl;

import com.example.cinema.dao.ReservationDao;
import com.example.cinema.entity.Reservation;
import com.example.cinema.util.HibernateUtil;
import com.example.cinema.viewFactory.AppViewFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDaoImpl extends CrudDaoImpl<Reservation, Integer> implements ReservationDao {
    @Override
    public List<Reservation> findUpcoming() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        List<Reservation> list = new ArrayList<>();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            String queryString = "select r from Reservation r where r.screening.start>:now and r.user.username = :id" +
                    " order by r.createdAt desc";
            list.addAll(session.createQuery(queryString)
                    .setParameter("id", AppViewFactory.instance.getUserCurrent().getUsername())
                    .setParameter("now", LocalDateTime.now())
                    .getResultList());

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return list;
    }

    @Override
    public List<Reservation> findWatched() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        List<Reservation> list = new ArrayList<>();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            String queryString = "select r from Reservation r where r.screening.start<=:now and r.user.username = :id" +
                    " order by r.createdAt desc";
            list.addAll(session.createQuery(queryString)
                    .setParameter("id", AppViewFactory.instance.getUserCurrent().getUsername())
                    .setParameter("now", LocalDateTime.now())
                    .getResultList());

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return list;
    }
}
