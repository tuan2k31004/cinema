package com.example.cinema.util;

import com.example.cinema.entity.*;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {
    @Getter
    private static SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                configuration.configure();
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Movie.class);
                configuration.addAnnotatedClass(Screening.class);
                configuration.addAnnotatedClass(Seat.class);
                configuration.addAnnotatedClass(Auditorium.class);
                configuration.addAnnotatedClass(Food.class);
                configuration.addAnnotatedClass(Reservation.class);
                configuration.addAnnotatedClass(SeatReserved.class);


                sessionFactory = configuration.buildSessionFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

}
