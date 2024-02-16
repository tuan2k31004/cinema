module com.example.cinema {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires lombok;
    requires de.jensd.fx.glyphs.fontawesome;
    requires mysql.connector.j;
    requires java.sql;
    requires org.controlsfx.controls;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;


    opens com.example.cinema to javafx.fxml;
    exports com.example.cinema;
    opens com.example.cinema.controller to javafx.fxml;
    exports com.example.cinema.controller;
    opens com.example.cinema.entity to org.hibernate.orm.core;
}