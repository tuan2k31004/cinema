package com.example.cinema.controller;

import com.example.cinema.entity.Food;
import com.example.cinema.entity.Reservation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import lombok.Setter;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HistoryItemController implements Initializable {
    @FXML
    private Label lbSeat;
    @FXML
    private Label lbFood;
    @FXML
    private Label lbAuditorium;
    @FXML
    private Label lbName;
    @FXML
    private Label lbTime;
    @FXML
    private Label lbPrice;
    @Setter
    private Reservation reservation;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            lbName.setText(reservation.getScreening().getMovie().getName());
            lbTime.setText(formatter.format(reservation.getScreening().getStart()));
            int total = reservation.getScreening().getPrice() * reservation.getSeatsReserved().size()
                    + reservation.getFoods().stream().mapToInt(Food::getPrice).sum();
            lbPrice.setText(decimalFormat.format(total));
            lbAuditorium.setText("Phòng: " + reservation.getScreening().getAuditorium().getName());
            StringJoiner stringJoiner = new StringJoiner(", ");
            getSetFood().forEach(stringJoiner::add);
            lbFood.setText("Đồ ăn: " + stringJoiner);

            StringJoiner stringJoiner1 = new StringJoiner(", ");
            reservation.getSeatsReserved().forEach(s -> stringJoiner1.add(s.getSeat().getTitle()));
            lbSeat.setText("Ghế: " + stringJoiner1);
        });
    }

    private Set<String> getSetFood() {
        List<Food> foods = reservation.getFoods();
        Map<String, Integer> map = new HashMap<String, Integer>();
        foods.forEach(food -> {
            if (map.containsKey(food.getName())) {
                int a = map.get(food.getName());
                map.put(food.getName(), a + 1);
            } else {
                map.put(food.getName(), 1);
            }
        });
        Set<String> set = new HashSet<String>();
        map.forEach((string, integer) -> {
            set.add(string + " x" + integer);
        });
        return set;
    }
}
