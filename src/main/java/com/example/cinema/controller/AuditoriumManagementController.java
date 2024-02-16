package com.example.cinema.controller;

import com.example.cinema.dao.AuditoriumDao;
import com.example.cinema.entity.Auditorium;
import com.example.cinema.entity.RoomType;
import com.example.cinema.viewFactory.AppViewFactory;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class AuditoriumManagementController implements Initializable {
    @FXML
    private JFXTextField tfName;
    @FXML
    private JFXComboBox<RoomType> cbType;
    @FXML
    private JFXTreeTableView<Auditorium> tbAuditorium;
    @FXML
    private JFXTreeTableColumn<Auditorium, String> colName;
    @FXML
    private JFXTreeTableColumn<Auditorium, String> colType;
    @FXML
    private JFXTreeTableColumn<Auditorium, Node> colDelete;

    public void addAuditorium(MouseEvent mouseEvent) {
        if (tfName.validate()) {
            Auditorium auditorium = new Auditorium(tfName.getText(), cbType.getValue());
            AuditoriumDao.instance.save(auditorium).ifPresent(f -> {
                AppViewFactory.instance.showSuccess("Thêm thành công");
                AppViewFactory.instance.getAuditoriums().add(f);
            });

            tfName.setText("");
            cbType.getSelectionModel().selectFirst();
        }
    }

    private void deleteAuditorium(Auditorium auditorium){
        if (AuditoriumDao.instance.hasUpcomingScreening(auditorium)) {
            AppViewFactory.instance.showError("Phòng đang có lịch chiếu");
        } else {
            AppViewFactory.instance.showConfirmation("Bạn có chắc xóa " + auditorium.getName() + " không?", () -> {
                AuditoriumDao.instance.deleteById(auditorium.getId(), Auditorium.class);
                AppViewFactory.instance.getScreenings()
                        .removeIf(screening -> screening.getAuditorium().equals(auditorium));
                AppViewFactory.instance.showSuccess("Đã xóa");
                AppViewFactory.instance.getAuditoriums().remove(auditorium);
            });
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfName.getValidators().add(new RequiredFieldValidator("Bắt buộc"));

        cbType.setItems(FXCollections.observableArrayList(RoomType.values()));
        cbType.getSelectionModel().selectFirst();

        colName.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getValue().getName()));
        colType.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getValue().getType().getTitle()));
        colDelete.setCellValueFactory(param -> {
            FontAwesomeIconView delete = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
            delete.setSize("20");
            delete.setOnMouseClicked(mouseEvent -> {
                Auditorium auditorium = param.getValue().getValue();
                deleteAuditorium(auditorium);
            });
            return new SimpleObjectProperty<>(delete);
        });
        tbAuditorium.setShowRoot(false);
        tbAuditorium.setRoot(new RecursiveTreeItem<>(AppViewFactory.instance.getAuditoriums(), RecursiveTreeObject::getChildren));
    }
}
