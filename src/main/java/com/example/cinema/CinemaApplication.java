package com.example.cinema;

import com.example.cinema.util.HibernateUtil;
import com.example.cinema.viewFactory.AccountViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class CinemaApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        AccountViewFactory.instance.show();
    }

    public static void main(String[] args) {
        HibernateUtil.getSessionFactory();
        launch();
    }
}