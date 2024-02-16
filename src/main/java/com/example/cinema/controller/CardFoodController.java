package com.example.cinema.controller;

import com.example.cinema.entity.Food;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import lombok.Setter;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

public class CardFoodController implements Initializable {
    @FXML
    private ImageView imgThumbnail;
    @FXML
    private Label lbNameFood;
    @FXML
    private Label lbDescription;
    @FXML
    private Label lbPrice;
    @FXML
    private Label lbQuantity;
    @Setter
    private Food food;
    @Setter
    private List<Food> foodList;
    private IntegerProperty a = new SimpleIntegerProperty(0);
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###");

    public void increase(MouseEvent mouseEvent) {
        a.setValue(a.get() + 1);
        foodList.add(food);
    }

    public void decrease(MouseEvent mouseEvent) {
        if (a.get() > 0) {
            a.setValue(a.get() - 1);
            foodList.remove(food);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        a.addListener((observableValue, number, t1) -> {
            lbQuantity.setText(t1.toString());
        });
        Platform.runLater(() -> {
            imgThumbnail.setImage(new Image(food.getThumbnail()));
            lbNameFood.setText(food.getName());
            lbDescription.setText(food.getDescription());
            lbPrice.setText(decimalFormat.format(food.getPrice()));
        });
    }
}
