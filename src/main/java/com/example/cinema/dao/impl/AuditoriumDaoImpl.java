package com.example.cinema.dao.impl;

import com.example.cinema.dao.AuditoriumDao;
import com.example.cinema.entity.Auditorium;
import com.example.cinema.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;

public class AuditoriumDaoImpl extends CrudDaoImpl<Auditorium, Integer> implements AuditoriumDao {
    @Override
    public boolean hasUpcomingScreening(Auditorium auditorium) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        boolean res = false;
        try {
            transaction = session.beginTransaction();
            String queryString = "select count(*) from Screening s where s.auditorium=:auditorium and s.start>=:now";
            Query<Long> query = session.createQuery(queryString, Long.class)
                    .setParameter("auditorium", auditorium)
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
}
