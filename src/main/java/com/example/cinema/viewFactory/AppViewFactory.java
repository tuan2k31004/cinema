package com.example.cinema.viewFactory;

import com.example.cinema.CinemaApplication;
import com.example.cinema.dao.AuditoriumDao;
import com.example.cinema.dao.FoodDao;
import com.example.cinema.dao.MovieDao;
import com.example.cinema.dao.ScreeningDao;
import com.example.cinema.entity.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSpinner;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.SneakyThrows;


public class AppViewFactory {
    public static AppViewFactory instance = new AppViewFactory();
    private Stage stage;

    private StringProperty selectedViewName;

    public StringProperty selectedViewNameProperty() {
        return selectedViewName;
    }

    public void setSelectedViewName(String selectedViewName) {
        this.selectedViewName.set(selectedViewName);
    }

    private ObjectProperty<JFXButton> buttonSelected;

    public void setButtonSelected(JFXButton buttonSelected) {
        this.buttonSelected.set(buttonSelected);
    }

    private ObjectProperty<User> userCurrent = new SimpleObjectProperty<>();

    public User getUserCurrent() {
        return userCurrent.get();
    }

    public ObjectProperty<User> userCurrentProperty() {
        return userCurrent;
    }

    public void setUserCurrent(User userCurrent) {
        this.userCurrent.set(userCurrent);
    }

    private ObservableList<Movie> movies;

    public ObservableList<Movie> getMovies() {
        if (movies == null) {
            movies = FXCollections.observableArrayList(MovieDao.instance.findAll(Movie.class));
        }
        return movies;
    }

    private ObservableList<Screening> screenings;

    public ObservableList<Screening> getScreenings() {
        if (screenings == null) {
            screenings = FXCollections.observableArrayList(ScreeningDao.instance.findAll(Screening.class));
        }
        return screenings;
    }


    private ObservableList<Food> foods;

    public ObservableList<Food> getFoods() {
        if (foods == null) {
            foods = FXCollections.observableArrayList(FoodDao.instance.findAll(Food.class));
        }
        return foods;
    }

    private ObservableList<Auditorium> auditoriums;

    public ObservableList<Auditorium> getAuditoriums() {
        if (auditoriums == null) {
            auditoriums = FXCollections.observableArrayList(AuditoriumDao.instance.findAll(Auditorium.class));
        }
        return auditoriums;
    }


    private AppViewFactory() {
        stage = new Stage();
        selectedViewName = new SimpleStringProperty();
        buttonSelected = new SimpleObjectProperty<>();
        buttonSelected.addListener((observableValue, jfxButton, t1) -> {
            t1.setStyle(" -fx-background-color: rgb(255, 51, 51)");
            if (jfxButton != null) {
                jfxButton.setStyle("");
            }
        });
    }

    @SneakyThrows
    public void show() {
        FXMLLoader loader = new FXMLLoader(CinemaApplication.class.getResource("fxml/app.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("APP");
        stage.setResizable(false);
        stage.show();
    }

    public void close() {
        stage.close();
        stage = new Stage();
        selectedViewName = new SimpleStringProperty();
    }


    public void showConfirmation(String message, Runnable ok) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                ok.run();
            }
        });
    }

    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private JFXDialog jfxDialog;

    public void showDialog(Region node, Runnable closed) {
        jfxDialog = new JFXDialog((StackPane) stage.getScene().getRoot(), node, JFXDialog.DialogTransition.CENTER);
        jfxDialog.show();
        jfxDialog.setOnDialogClosed(jfxDialogEvent -> {
            closed.run();
        });
    }

    public void loadData(Runnable runnable,StackPane stackPane) {
        if(stackPane==null){
            stackPane = (StackPane) stage.getScene().getRoot();
        }
        JFXSpinner jfxSpinner = new JFXSpinner();
        jfxSpinner.setRadius(30);
        JFXDialog jfxDialog1 = new JFXDialog(stackPane, jfxSpinner, JFXDialog.DialogTransition.CENTER);
        jfxDialog1.setOverlayClose(false);
        jfxDialog1.getContent().getParent().setStyle("-fx-background-color: transparent;-fx-effect: none");
        jfxDialog1.show();
        jfxDialog1.setOnDialogOpened(jfxDialogEvent -> {
            runnable.run();
            jfxDialog1.close();
        });
    }

    public void closeDialog() {
        jfxDialog.close();
    }


    private VBox menuAdmin;
    private VBox menuClient;
    private Pane movieManagementView;
    private Pane screeningManagementView;
    private Pane foodManagementView;
    private Pane auditoriumManagementView;
    private Pane screeningMoviesView;
    private Pane historyView;

    @SneakyThrows
    public Pane getHistoryView() {
        FXMLLoader loader = new FXMLLoader(CinemaApplication.class.getResource("fxml/client_view/history.fxml"));
        historyView = loader.load();
        return historyView;
    }

    @SneakyThrows
    public Pane getScreeningMoviesView() {
        FXMLLoader loader = new FXMLLoader(CinemaApplication.class.getResource("fxml/client_view/screening_movies.fxml"));
        screeningMoviesView = loader.load();
        return screeningMoviesView;
    }

    @SneakyThrows
    public VBox getMenuAdmin() {
        FXMLLoader loader = new FXMLLoader(CinemaApplication.class.getResource("fxml/admin_view/admin_menu.fxml"));
        menuAdmin = loader.load();
        return menuAdmin;
    }

    @SneakyThrows
    public VBox getMenuClient() {
        FXMLLoader loader = new FXMLLoader(CinemaApplication.class.getResource("fxml/client_view/client_menu.fxml"));
        menuClient = loader.load();
        return menuClient;
    }

    @SneakyThrows
    public Pane getMovieManagementView() {
        FXMLLoader loader = new FXMLLoader(CinemaApplication.class.getResource("fxml/admin_view/movie_management.fxml"));
        movieManagementView = loader.load();
        return movieManagementView;
    }

    @SneakyThrows
    public Pane getScreeningManagementView() {
        FXMLLoader loader = new FXMLLoader(CinemaApplication.class.getResource("fxml/admin_view/screening_management.fxml"));
        screeningManagementView = loader.load();
        return screeningManagementView;
    }

    @SneakyThrows
    public Pane getFoodManagementView() {
        FXMLLoader loader = new FXMLLoader(CinemaApplication.class.getResource("fxml/admin_view/food_management.fxml"));
        foodManagementView = loader.load();
        return foodManagementView;
    }

    @SneakyThrows
    public Pane getAuditoriumManagementView() {
        FXMLLoader loader = new FXMLLoader(CinemaApplication.class.getResource("fxml/admin_view/auditorium_management.fxml"));
        auditoriumManagementView = loader.load();
        return auditoriumManagementView;
    }
}
