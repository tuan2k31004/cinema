package com.example.cinema.controller;

import com.example.cinema.dao.ScreeningDao;
import com.example.cinema.entity.Auditorium;
import com.example.cinema.entity.Movie;
import com.example.cinema.entity.Screening;
import com.example.cinema.viewFactory.AppViewFactory;
import com.jfoenix.controls.*;
import com.jfoenix.validation.IntegerValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import lombok.Setter;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CrudScreeningController implements Initializable {
    @FXML
    private JFXButton btnDelete;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXComboBox<Movie> cbMovie;
    @FXML
    private JFXComboBox<Auditorium> cbAuditorium;
    @FXML
    private JFXDatePicker dpDate;
    @FXML
    private JFXTimePicker tpTime;
    @FXML
    private JFXTextField tfPrice;
    @Setter
    private Screening currentScreening;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dpDate.setValue(LocalDate.now());
        tpTime.setValue(LocalTime.NOON);
        tpTime.set24HourView(true);

        cbMovie.setItems(AppViewFactory.instance.getMovies());
        cbMovie.getSelectionModel().selectFirst();
        cbAuditorium.setItems(AppViewFactory.instance.getAuditoriums());
        cbAuditorium.getSelectionModel().selectFirst();

        tfPrice.getValidators().addAll(new RequiredFieldValidator("Bắt buộc"), new IntegerValidator("giá vé phải là số nguyên"));

        tpTime.getValidators().add(new ValidatorBase("Thời gian đã trôi qua") {
            @Override
            protected void eval() {
                LocalDateTime localDateTime = LocalDateTime.of(dpDate.getValue(), tpTime.getValue());
                hasErrors.set(localDateTime.isBefore(LocalDateTime.now()));
            }
        });

        btnDelete.setOnAction(actionEvent -> {
            deleteScreening();
        });
        btnSave.setOnAction(actionEvent -> {
            saveScreening();

        });
        Platform.runLater(() -> {
            if (currentScreening != null) {
                btnDelete.setVisible(true);
                cbMovie.setDisable(true);
                cbMovie.getSelectionModel().select(currentScreening.getMovie());
                cbAuditorium.getSelectionModel().select(currentScreening.getAuditorium());
                tfPrice.setText(String.valueOf(currentScreening.getPrice()));
                dpDate.setValue(currentScreening.getStart().toLocalDate());
                tpTime.setValue(currentScreening.getStart().toLocalTime());
                btnSave.setText("Cập nhập");
            }
        });
    }

    private void deleteScreening() {
        AppViewFactory.instance.showConfirmation("Bạn có chắc xóa không?", () -> {
                    ScreeningDao.instance.deleteById(currentScreening.getId(), Screening.class);
                    AppViewFactory.instance.showSuccess("Đã xóa");
                    AppViewFactory.instance.closeDialog();
                    AppViewFactory.instance.getScreenings().remove(currentScreening);
                }
        );
    }

    private void saveScreening() {
        if (tpTime.validate() && tfPrice.validate()) {
            Screening screening = Screening.builder()
                    .movie(cbMovie.getValue())
                    .auditorium(cbAuditorium.getValue())
                    .start(LocalDateTime.of(dpDate.getValue(), tpTime.getValue()))
                    .end(LocalDateTime.of(dpDate.getValue(), tpTime.getValue()).plusMinutes(30 + cbMovie.getValue().getDuration()))
                    .price(Integer.parseInt(tfPrice.getText()))
                    .reservations(new ArrayList<>())
                    .build();
            if (currentScreening == null) {
                ScreeningDao.instance.findScreeningDuplicate(screening).ifPresentOrElse(s -> {
                    AppViewFactory.instance.showError("Trùng lịch chiếu với phim " + s);
                }, () -> {
                    ScreeningDao.instance.save(screening).ifPresent(s -> {
                        AppViewFactory.instance.showSuccess("Đã thêm");
                        AppViewFactory.instance.closeDialog();
                        AppViewFactory.instance.getScreenings().add(s);
                    });
                });
            } else {
                screening.setId(currentScreening.getId());
                ScreeningDao.instance.findScreeningDuplicate(screening).ifPresentOrElse(s -> {
                    AppViewFactory.instance.showError("Trùng lịch chiếu với phim " + s);
                }, () -> {
                    currentScreening.updateScreen(screening);
                    ScreeningDao.instance.save(screening).ifPresent(s -> {
                        AppViewFactory.instance.showSuccess("Đã cập nhập");
                        AppViewFactory.instance.closeDialog();
                    });
                });
            }
        }
    }
}
