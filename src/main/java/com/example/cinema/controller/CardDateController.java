package com.example.cinema.controller;

import com.example.cinema.viewFactory.MovieTicketViewFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.Setter;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CardDateController implements Initializable {
    @FXML
    private HBox parent;
    @FXML
    private Label lbDay;
    @FXML
    private Label lbMonth;
    @FXML
    private Label lbNameDay;
    @Setter
    private LocalDate localDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            lbDay.setText(localDate.format(DateTimeFormatter.ofPattern("dd")));
            lbMonth.setText(localDate.format(DateTimeFormatter.ofPattern("MM")));
            lbNameDay.setText(localDate.format(DateTimeFormatter.ofPattern("EEE")));
            parent.setUserData(localDate);
            if(localDate.isEqual(LocalDate.now())){
                MovieTicketViewFactory.instance.setDateSelected(parent);
            }
        });
        parent.setOnMouseClicked(mouseEvent -> {
            MovieTicketViewFactory.instance.setDateSelected(parent);
        });
    }

}
