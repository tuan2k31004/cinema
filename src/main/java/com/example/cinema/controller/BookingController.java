package com.example.cinema.controller;

import com.example.cinema.CinemaApplication;
import com.example.cinema.dao.FoodDao;
import com.example.cinema.dao.MovieDao;
import com.example.cinema.dao.ReservationDao;
import com.example.cinema.entity.*;
import com.example.cinema.viewFactory.AppViewFactory;
import com.example.cinema.viewFactory.MovieTicketViewFactory;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleNode;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringJoiner;

public class BookingController implements Initializable {
    @FXML
    private StackPane root;
    @FXML
    private ScrollPane listFood;
    @FXML
    private JFXButton btnPrev;
    @FXML
    private JFXButton btnOK;
    @FXML
    private JFXButton btnContinue;
    @FXML
    private FlowPane listSeat;
    @FXML
    private ImageView imgThumbnail;
    @FXML
    private Label lbNameMovie;
    @FXML
    private Label lbAuditorium;
    @FXML
    private Label lbTime;
    @FXML
    private Label lbSeats;
    @FXML
    private Label lbTotal;
    @FXML
    private Label lbFood;
    @FXML
    private Label lbPayment;
    @Setter
    private Screening screening;
    private final List<Seat> seatSelected = new ArrayList<>();
    private final ObservableList<Food> foods = FXCollections.observableArrayList();
    private final Reservation reservation = new Reservation();
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        foods.addListener((ListChangeListener<? super Food>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    int a = 0;
                    for (Food food : foods) {
                        a += food.getPrice();
                    }
                    lbFood.setText(String.valueOf(a));
                }
            }
        });
        getListFood();
        btnContinue.setOnAction(actionEvent -> {
            if (seatSelected.isEmpty()) {
                AppViewFactory.instance.showError("Vui lòng chọn ghế");
            } else {
                listFood.setVisible(true);
                btnPrev.setVisible(true);
                btnOK.setVisible(true);
            }
        });
        btnPrev.setOnAction(actionEvent -> {
            listFood.setVisible(false);
            btnPrev.setVisible(false);
            btnOK.setVisible(false);
        });
        btnOK.setOnAction(actionEvent -> {
            booking();
        });

        lbTotal.textProperty().addListener((observableValue, s, t1) -> {
            setLbPayment();
        });
        lbFood.textProperty().addListener((observableValue, s, t1) -> {
            setLbPayment();
        });
        Platform.runLater(() -> {
            imgThumbnail.setImage(new Image(screening.getMovie().getThumbnail()));
            lbNameMovie.setText(screening.getMovie().getName());
            lbAuditorium.setText(screening.getAuditorium().getName());
            lbTime.setText(screening.getStart().toLocalTime().toString() + " " + screening.getStart().toLocalDate().toString());
            switch (screening.getAuditorium().getType()) {
                case THREE_D, TWO_D -> {
                    listSeat.setPrefWidth(360);
                }
                case IMAX -> {
                    listSeat.setPrefWidth(520);
                }
                case VIP -> {
                    listSeat.setPrefWidth(190);
                }
            }
            screening.getAuditorium().getSeats().forEach(seat -> {
                JFXToggleNode jfxButton = new JFXToggleNode(seat.getTitle());
                jfxButton.getStyleClass().add("seat");

                jfxButton.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
                    if (seatSelected.size() == 8 && t1) {
                        jfxButton.setSelected(false);
                        AppViewFactory.instance.showError("Bạn chỉ được chọn tối đa 8 ghế");
                        return;
                    }
                    if (t1) {
                        seatSelected.add(seat);
                    } else {
                        seatSelected.remove(seat);
                    }
                    StringJoiner stringJoiner = new StringJoiner(", ");
                    seatSelected.forEach(s -> {
                        stringJoiner.add(s.getTitle());
                    });
                    lbSeats.setText(stringJoiner.toString());
                    lbTotal.setText(decimalFormat.format((long) seatSelected.size() * screening.getPrice()));
                });
                screening.getReservations().forEach(r -> {
                    r.getSeatsReserved().forEach(seatReserved -> {
                        if (seatReserved.getSeat().getId() == seat.getId()) {
                            jfxButton.setDisable(true);
                        }
                    });
                });
                listSeat.getChildren().add(jfxButton);
            });
        });
    }

    @SneakyThrows
    private void setLbPayment() {
        int a = decimalFormat.parse(lbTotal.getText()).intValue();
        int b = decimalFormat.parse(lbFood.getText()).intValue();
        lbPayment.setText(decimalFormat.format(a + b));
    }

    private void getListFood() {
        FoodDao.instance.findAll(Food.class).forEach(food -> {
            FXMLLoader fxmlLoader = new FXMLLoader(CinemaApplication.class.getResource("fxml/client_view/card_food.fxml"));
            try {
                Node node = fxmlLoader.load();
                ((CardFoodController) fxmlLoader.getController()).setFood(food);
                ((CardFoodController) fxmlLoader.getController()).setFoodList(foods);
                ((FlowPane) listFood.getContent()).getChildren().add(node);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void booking() {
        AppViewFactory.instance.showConfirmation("Bạn chắn chắn muốn đặt chứ?", () -> {
            AppViewFactory.instance.loadData(() -> {
                reservation.setScreening(screening);
                reservation.setUser(AppViewFactory.instance.getUserCurrent());
                List<SeatReserved> reserved = new ArrayList<>();
                seatSelected.forEach(seat -> {
                    reserved.add(SeatReserved.builder()
                            .reservation(reservation)
                            .seat(seat)
                            .build());
                });
                reservation.setFoods(foods);
                reservation.setSeatsReserved(reserved);
                ReservationDao reservationDao = ReservationDao.instance;
                Reservation reservation1 = reservationDao.save(reservation).orElseThrow();
                Movie movie = screening.getMovie();
                try {
                    movie.setRevenue(movie.getRevenue() + decimalFormat.parse(lbPayment.getText()).longValue());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                MovieDao.instance.save(movie);
                if (!MovieTicketViewFactory.instance.isUpcomingNull()) {
                    MovieTicketViewFactory.instance.getUpcoming().add(0, reservation1);
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Đặt vé thành công");
                alert.show();
                alert.setOnCloseRequest(dialogEvent -> {
                    ((Stage) lbTime.getScene().getWindow()).close();
                });
            }, root);
        });
    }
}
