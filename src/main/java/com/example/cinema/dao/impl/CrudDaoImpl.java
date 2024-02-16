package com.example.cinema.dao.impl;

import com.example.cinema.dao.CrudDao;
import com.example.cinema.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CrudDaoImpl<T, ID> implements CrudDao<T, ID> {
    @Override
    public Optional<T> save(T value) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        T newValue = null;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            newValue = session.merge(value);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }

        return Optional.ofNullable(newValue);
    }

    @Override
    public List<T> findAll(Class<T> type) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        List<T> list = new ArrayList<>();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            String queryString = "select t from " + type.getName() + " t";
            list.addAll(session.createQuery(queryString, type).getResultList());

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
    public void deleteById(ID id, Class<T> type) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            T t = session.get(type, id);
            if (t != null) {
                session.remove(t);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }

    @Override
    public boolean existsById(ID id, Class<T> type) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        boolean exists = false;
        try {
            transaction = session.beginTransaction();
            T t = session.get(type, id);
            if (t != null) {
                exists = true;
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return exists;
    }

    @Override
    public Optional<T> findById(ID id, Class<T> type) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        T t = null;
        try {
            transaction = session.beginTransaction();
            t = session.get(type, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return Optional.ofNullable(t);
    }
}


