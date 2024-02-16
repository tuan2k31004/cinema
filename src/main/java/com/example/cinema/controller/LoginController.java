package com.example.cinema.controller;


import com.example.cinema.dao.UserDao;
import com.example.cinema.entity.Role;


import com.example.cinema.entity.User;
import com.example.cinema.util.PasswordEncode;
import com.example.cinema.viewFactory.AccountViewFactory;
import com.example.cinema.viewFactory.AppViewFactory;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable {
    @FXML
    private JFXComboBox<Role> cbRole;
    @FXML
    private JFXTextField tfUsername;
    @FXML
    private JFXPasswordField tfPassword;


    public void signup(MouseEvent mouseEvent) {
        AccountViewFactory.instance.setSelectedViewName("signup");
    }

    public void login(MouseEvent mouseEvent) {
        User user = User.builder()
                .password(PasswordEncode.encode(tfPassword.getText()))
                .username(tfUsername.getText())
                .role(cbRole.getValue())
                .build();
        UserDao.instance.checkUser(user).ifPresentOrElse(u -> {
            AccountViewFactory.instance.close();
            AppViewFactory.instance.show();
            AppViewFactory.instance.setUserCurrent(u);
        }, () -> AppViewFactory.instance.showError("Tài khoản hoặc mật khẩu không đúng"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbRole.setItems(FXCollections.observableArrayList(Role.values()));
        cbRole.getSelectionModel().select(Role.CLIENT);
    }
}
