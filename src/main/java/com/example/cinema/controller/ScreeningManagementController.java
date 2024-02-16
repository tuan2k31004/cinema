package com.example.cinema.controller;

import com.example.cinema.CinemaApplication;
import com.example.cinema.entity.Auditorium;
import com.example.cinema.entity.Screening;
import com.example.cinema.viewFactory.AppViewFactory;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import lombok.SneakyThrows;


import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ScreeningManagementController implements Initializable {
    @FXML
    private JFXTextField tfName;
    @FXML
    private JFXDatePicker dpDate;
    @FXML
    private JFXComboBox<Auditorium> cbAuditorium;
    @FXML
    private JFXTreeTableView<Screening> tbScreening;
    @FXML
    private JFXTreeTableColumn<Screening, String> colName;
    @FXML
    private JFXTreeTableColumn<Screening, String> colStart;
    @FXML
    private JFXTreeTableColumn<Screening, String> colEnd;
    @FXML
    private JFXTreeTableColumn<Screening, Integer> colSeatNo;
    @FXML
    private JFXTreeTableColumn<Screening, Node> colEdit;
    @FXML
    private JFXTreeTableColumn<Screening, Integer> colPrice;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbAuditorium.getItems().add(Auditorium.builder().name("Tất cả").build());
        cbAuditorium.getItems().addAll(AppViewFactory.instance.getAuditoriums());
        cbAuditorium.getSelectionModel().selectFirst();

        colName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getMovie().getName()));
        colStart.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getValue().getStart().toLocalTime().toString()));
        colEnd.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getEnd().toLocalTime().toString()));
        colSeatNo.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getValue().getAuditorium().getType().getCapacity()));
        colPrice.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getValue().getPrice()));
        colEdit.setCellValueFactory(param -> {
            FontAwesomeIconView edit = new FontAwesomeIconView() {
                {
                    setGlyphName("EDIT");
                    setSize("20");
                    setOnMouseClicked(mouseEvent -> {
                        showCrudScreening(param.getValue().getValue());
                    });
                }
            };
            return new SimpleObjectProperty<>(edit);
        });


        tfName.textProperty().addListener((observableValue, s, t1) -> {
            filterScreening();
        });
        dpDate.valueProperty().addListener((observableValue, localDate, t1) -> {
            filterScreening();
        });
        cbAuditorium.valueProperty().addListener((observableValue, auditorium, t1) -> {
            filterScreening();
        });

        Platform.runLater(() -> {
            dpDate.setValue(LocalDate.now());
        });
    }

    @SneakyThrows
    private void showCrudScreening(Screening screening) {
        FXMLLoader fxmlLoader = new FXMLLoader(CinemaApplication.class.getResource("fxml/admin_view/crud_screening.fxml"));
        VBox vBox = fxmlLoader.load();
        ((CrudScreeningController) fxmlLoader.getController()).setCurrentScreening(screening);
        vBox.setUserData(screening);
        AppViewFactory.instance.showDialog(vBox, this::filterScreening);
    }

    public void filterScreening() {
        ObservableList<Screening> filterList = AppViewFactory.instance.getScreenings().filtered(screening -> {
            boolean a = tfName.getText().isBlank() || screening.getMovie().getName().toLowerCase().contains(tfName.getText().toLowerCase());
            boolean b = cbAuditorium.getSelectionModel().getSelectedItem().getName().equals("Tất cả") || screening.getAuditorium().equals(cbAuditorium.getValue());
            boolean c = screening.getStart().toLocalDate().isEqual(dpDate.getValue());
            return a && b && c;
        });
        tbScreening.setShowRoot(false);
        tbScreening.setRoot(new RecursiveTreeItem<>(filterList, RecursiveTreeObject::getChildren));
    }

    public void showCrudScreening() {
        showCrudScreening(null);
    }
}
