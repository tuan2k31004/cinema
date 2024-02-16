package com.example.cinema.controller;

import com.example.cinema.CinemaApplication;
import com.example.cinema.entity.Genre;
import com.example.cinema.entity.Movie;
import com.example.cinema.viewFactory.AppViewFactory;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringJoiner;

public class MovieManagementController implements Initializable {
    @FXML
    private JFXTextField tfSearch;
    @FXML
    private JFXTreeTableView<Movie> tbMovie;
    @FXML
    private JFXTreeTableColumn<Movie, String> colName;
    @FXML
    private JFXTreeTableColumn<Movie, String> colGenres;
    @FXML
    private JFXTreeTableColumn<Movie, String> colDirector;
    @FXML
    private JFXTreeTableColumn<Movie, Long> colRevenue;
    @FXML
    private JFXTreeTableColumn<Movie, Node> colEdit;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getName()));
        colGenres.setCellValueFactory(param -> {
            List<Genre> genres = param.getValue().getValue().getGenres();
            StringJoiner joiner = new StringJoiner(", ");
            genres.forEach(g -> joiner.add(g.getTitle()));
            return new SimpleStringProperty(joiner.toString());
        });
        colDirector.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getDirector()));
        colRevenue.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getValue().getRevenue()));
        colEdit.setCellValueFactory(param -> {
            FontAwesomeIconView edit = new FontAwesomeIconView() {
                {
                    setGlyphName("EDIT");
                    setSize("20");
                    setOnMouseClicked(mouseEvent -> {
                        showCRUDMovie(param.getValue().getValue());
                    });
                }
            };
            return new SimpleObjectProperty<>(edit);
        });


        tbMovie.setRoot(new RecursiveTreeItem<>(AppViewFactory.instance.getMovies(), RecursiveTreeObject::getChildren));
        tbMovie.setShowRoot(false);

        tfSearch.textProperty().addListener((observableValue, s, t1) -> {
            tbMovie.setPredicate(movieTreeItem -> {
                if (t1.isBlank()) return true;
                return movieTreeItem.getValue().getName().toLowerCase().contains(t1.toLowerCase());
            });
        });


    }

    public void addMovie(MouseEvent mouseEvent) {
        showCRUDMovie(null);
    }

    @SneakyThrows
    private void showCRUDMovie(Movie movie) {
        FXMLLoader loader = new FXMLLoader(CinemaApplication.class.getResource("fxml/admin_view/crud_movie.fxml"));
        StackPane stackPane = loader.load();
        ((CrudMovieController) loader.getController()).setMovieCurrent(movie);
        AppViewFactory.instance.showDialog(stackPane, () -> {
            tbMovie.refresh();
        });
    }
}
