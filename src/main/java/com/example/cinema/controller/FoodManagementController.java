package com.example.cinema.controller;

import com.example.cinema.dao.FoodDao;
import com.example.cinema.entity.Food;
import com.example.cinema.viewFactory.AppViewFactory;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class FoodManagementController implements Initializable {

    @FXML
    private ImageView imgThumbnail;
    @FXML
    private JFXTextField tfName;
    @FXML
    private JFXTextField tfPrice;
    @FXML
    private JFXTextArea taDescription;
    @FXML
    private JFXButton btnRemove;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private JFXTreeTableView<Food> tbFood;
    @FXML
    private JFXTreeTableColumn<Food, String> colName;
    @FXML
    private JFXTreeTableColumn<Food, Integer> colPrice;

    private final ObjectProperty<Food> foodCurrent = new SimpleObjectProperty<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfName.getValidators().add(new RequiredFieldValidator("Bắt buộc"));
        tfPrice.getValidators().addAll(new RequiredFieldValidator("Bắt buộc"), new NumberValidator("giá tiền phải là số (đ)"));
        taDescription.getValidators().add(new RequiredFieldValidator("Bắt buộc"));
        imgThumbnail.imageProperty().addListener((observableValue, image, t1) -> {
            validatorImg();
        });

        btnCancel.setOnMouseClicked(mouseEvent -> {
            tbFood.getSelectionModel().clearSelection();
        });
        btnSave.setOnMouseClicked(mouseEvent -> {
            save();
        });
        btnRemove.setOnMouseClicked(mouseEvent -> {
            deleteFood();
        });


        colName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getName()));
        colPrice.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getValue().getPrice()));


        tbFood.setShowRoot(false);
        tbFood.setRoot(new RecursiveTreeItem<>(AppViewFactory.instance.getFoods(), RecursiveTreeObject::getChildren));

        foodCurrent.addListener((observableValue, food, t1) -> {
            if (t1 != null) {
                imgThumbnail.setImage(new Image(t1.getThumbnail()));
                tfName.setText(t1.getName());
                tfPrice.setText(String.valueOf(t1.getPrice()));
                taDescription.setText(t1.getDescription());
                btnRemove.setVisible(true);
                btnCancel.setVisible(true);
                btnSave.setText("Cập nhập");
            } else {
                cleanup();
                btnRemove.setVisible(false);
                btnCancel.setVisible(false);
                btnSave.setText("Thêm");
            }
        });

        tbFood.getSelectionModel().selectedItemProperty().addListener((observableValue, foodTreeItem, t1) -> {
            if (t1 != null) {
                foodCurrent.set(t1.getValue());
            } else {
                foodCurrent.set(null);
            }
        });
    }

    private void deleteFood() {
        AppViewFactory.instance.showConfirmation("Bạn có chắc xóa " + foodCurrent.get().getName() + " không?",
                () -> {
                    FoodDao.instance.deleteById(foodCurrent.get().getId(), Food.class);
                    AppViewFactory.instance.showSuccess("Đã xóa");
                    AppViewFactory.instance.getFoods().remove(foodCurrent.get());
                });
        tbFood.getSelectionModel().clearSelection();
    }

    private void cleanup() {
        imgThumbnail.setImage(null);
        tfName.setText("");
        tfPrice.setText("");
        taDescription.setText("");
    }


    private boolean validatorImg() {
        if (imgThumbnail.getImage() == null) {
            imgThumbnail.getParent().setStyle("-fx-border-color: red;-fx-border-radius: 10");
        } else {
            imgThumbnail.getParent().setStyle("-fx-border-color: black;-fx-border-radius: 10");
        }
        return imgThumbnail.getImage() != null;
    }

    private void save() {
        if (validatorImg() && tfName.validate()
                && tfPrice.validate() && taDescription.validate()) {
            Food food = new Food(tfName.getText(), taDescription.getText(),
                    imgThumbnail.getImage().getUrl(),
                    Integer.parseInt(tfPrice.getText()));
            if (foodCurrent.get() == null) {
                FoodDao.instance.save(food).ifPresent(f -> {
                    AppViewFactory.instance.showSuccess("Thêm thành công");
                    AppViewFactory.instance.getFoods().add(f);
                });
                cleanup();
            } else {
                foodCurrent.get().updateFood(food);
                FoodDao.instance.save(foodCurrent.get());
                AppViewFactory.instance.showSuccess("Đã cập nhập");
                tbFood.refresh();
                tbFood.getSelectionModel().clearSelection();
            }
        }
    }


    public void upload(MouseEvent mouseEvent) {
        VBox vBox = new VBox();
        JFXTextField textField = new JFXTextField();
        JFXButton button = new JFXButton("OK");
        button.setStyle("-fx-background-color: blue;-fx-text-fill: #ffffff;-fx-pref-height: 30;-fx-pref-width: 80");
        textField.setPromptText("URL");
        textField.setPrefWidth(200);
        textField.setPrefHeight(40);
        vBox.getChildren().addAll(textField, button);
        vBox.setSpacing(20);
        vBox.setStyle("-fx-padding: 20");
        button.setOnAction(actionEvent -> {
            try {
                Image image = new Image(textField.getText());
                if (image.isError()) {
                    AppViewFactory.instance.showError("URL không hợp lệ");
                } else {
                    imgThumbnail.setImage(image);
                    AppViewFactory.instance.closeDialog();
                }
            } catch (Exception e) {
                AppViewFactory.instance.showError("URL không hợp lệ");
            }

        });
        AppViewFactory.instance.showDialog(vBox, () -> {
        });
    }
}
