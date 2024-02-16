package com.example.cinema.controller;


import com.example.cinema.dao.MovieDao;
import com.example.cinema.entity.Genre;
import com.example.cinema.entity.Movie;
import com.example.cinema.viewFactory.AppViewFactory;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import lombok.Setter;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class CrudMovieController implements Initializable {

    @FXML
    private JFXTextField tfNameMovie;
    @FXML
    private JFXTextField tfDirector;
    @FXML
    private JFXTextField tfDuration;
    @FXML
    private CheckComboBox<Genre> cbGenres;
    @FXML
    private JFXTextField tfRevenue;
    @FXML
    private JFXTextArea taDescription;
    @FXML
    private ImageView imgThumbnail;
    @FXML
    private JFXTextField tfThumbnail;
    @FXML
    private Label lbErrorGenres;
    @FXML
    private JFXButton btnDelete;
    private final JFXTextField tfTempGenres = new JFXTextField();
    @Setter
    private Movie movieCurrent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbGenres.getItems().addAll(Genre.values());


        tfDuration.getValidators().add(new NumberValidator("Thời lượng phải là số (phút)"));
        addRequiredFieldValidator(tfNameMovie);
        addRequiredFieldValidator(tfDirector);
        addRequiredFieldValidator(tfDuration);


        taDescription.getValidators().add(new RequiredFieldValidator("Bắt buộc"));
        taDescription.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                taDescription.validate();
            }
        });


        cbGenres.getCheckModel().getCheckedItems().addListener((InvalidationListener) observable -> {
            if (cbGenres.getCheckModel().getCheckedItems().isEmpty()) {
                tfTempGenres.setText(null);
            } else {
                tfTempGenres.setText("ok");
            }
        });
        tfTempGenres.getValidators().add(new RequiredFieldValidator() {
            {
                hasErrors.addListener((observableValue, aBoolean, t1) -> {
                    if (t1) {
                        lbErrorGenres.setText("Bắt buộc");
                    } else {
                        lbErrorGenres.setText("");
                    }
                });
            }
        });
        tfTempGenres.textProperty().addListener((observableValue, s, t1) -> {
            tfTempGenres.validate();
        });


        tfThumbnail.textProperty().addListener((observableValue, s, t1) -> {
            try {
                Image image = new Image(t1, imgThumbnail.getFitWidth(), imgThumbnail.getFitHeight(), true, false);
                if (image.isError()) {
                    imgThumbnail.setImage(null);
                } else {
                    imgThumbnail.setImage(image);
                }
            } catch (Exception e) {
                imgThumbnail.setImage(null);
            }
            tfThumbnail.validate();
        });
        tfThumbnail.getValidators().add(new ValidatorBase("URL không hợp lệ") {
            @Override
            protected void eval() {
                if (imgThumbnail.getImage() == null) {
                    hasErrors.set(true);
                } else {
                    hasErrors.set(false);
                }
            }
        });

        Platform.runLater(() -> {
            if (movieCurrent != null) {
                btnDelete.setVisible(true);
                tfNameMovie.setText(movieCurrent.getName());
                taDescription.setText(movieCurrent.getDescription());
                tfDuration.setText(String.valueOf(movieCurrent.getDuration()));
                tfThumbnail.setText(movieCurrent.getThumbnail());
                tfDirector.setText(movieCurrent.getDirector());
                tfRevenue.setText(String.valueOf(movieCurrent.getRevenue()));
                movieCurrent.getGenres().forEach(genre -> {
                    cbGenres.getCheckModel().check(genre);
                });
            }
        });
    }

    private void addRequiredFieldValidator(JFXTextField jfxTextField) {
        jfxTextField.getValidators().add(new RequiredFieldValidator("Bắt buộc"));
        jfxTextField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                jfxTextField.validate();
            }
        });
    }

    public void saveMovie(MouseEvent mouseEvent) {
        if (tfNameMovie.validate() && tfDirector.validate()
                && tfDuration.validate() && tfTempGenres.validate()
                && taDescription.validate() && tfThumbnail.validate()) {
            Movie movie = Movie.builder()
                    .name(tfNameMovie.getText())
                    .revenue(Long.parseLong(tfRevenue.getText()))
                    .genres(cbGenres.getCheckModel().getCheckedItems())
                    .director(tfDirector.getText())
                    .duration(Integer.parseInt(tfDuration.getText()))
                    .thumbnail(tfThumbnail.getText())
                    .description(taDescription.getText())
                    .build();
            if (movieCurrent == null) {
                MovieDao.instance.save(movie).ifPresent(m -> {
                    AppViewFactory.instance.showSuccess("Thêm thành công");
                    AppViewFactory.instance.getMovies().add(m);
                    AppViewFactory.instance.closeDialog();
                });
            } else {
                movieCurrent.updateMovie(movie);
                MovieDao.instance.save(movieCurrent);
                AppViewFactory.instance.showSuccess("Đã cập nhập");
                AppViewFactory.instance.getScreenings().forEach(screening -> {
                    if (screening.getMovie().equals(movieCurrent)) {
                        screening.getMovie().updateMovie(movieCurrent);
                    }
                });
                AppViewFactory.instance.closeDialog();
            }
        }
    }


    public void deleteMovie(MouseEvent mouseEvent) {
        if (MovieDao.instance.hasUpcomingScreening(movieCurrent)) {
            AppViewFactory.instance.showError("Phim đang có lịch chiếu");
        } else {
            AppViewFactory.instance.showConfirmation("Bạn có chắc xóa " + movieCurrent.getName() + " không?", () -> {
                MovieDao.instance.deleteById(movieCurrent.getId(), Movie.class);
                AppViewFactory.instance.showSuccess("Đã xóa");
                AppViewFactory.instance.getMovies().remove(movieCurrent);
                AppViewFactory.instance.getScreenings().removeIf(screening -> screening.getMovie().equals(movieCurrent));
                AppViewFactory.instance.closeDialog();
            });
        }
    }

    public void close(MouseEvent mouseEvent) {
        AppViewFactory.instance.closeDialog();
    }
}
