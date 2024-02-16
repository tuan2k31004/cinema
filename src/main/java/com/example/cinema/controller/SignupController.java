package com.example.cinema.controller;


import com.example.cinema.dao.UserDao;
import com.example.cinema.entity.Role;
import com.example.cinema.entity.User;
import com.example.cinema.util.PasswordEncode;
import com.example.cinema.viewFactory.AccountViewFactory;
import com.example.cinema.viewFactory.AppViewFactory;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable {
    @FXML
    private JFXTextField tfUsername;
    @FXML
    private JFXTextField tfPassword;

    public void back(MouseEvent mouseEvent) {
        AccountViewFactory.instance.setSelectedViewName("login");
    }

    public void register(MouseEvent mouseEvent) {
        if (tfUsername.validate() && tfPassword.validate()) {
            User user = new User(tfUsername.getText(), PasswordEncode.encode(tfPassword.getText()), Role.CLIENT);
            if (UserDao.instance.existsById(user.getUsername(), User.class)) {
                AppViewFactory.instance.showError("Tên đăng nhập đã tồn tại");
            } else {
                UserDao.instance.save(user);
                AppViewFactory.instance.showSuccess("Đăng ký thành công");
                AccountViewFactory.instance.setSelectedViewName("login");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfPassword.getValidators().add(new RequiredFieldValidator());
        tfPassword.getValidators().add(new RegexValidator("mật khẩu phải lớn hơn 8 kí tự") {
            {
                setRegexPattern(".{8,}");
            }
        });
        tfPassword.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                tfPassword.validate();
            }
        });

        tfUsername.getValidators().add(new RequiredFieldValidator());
        tfUsername.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                tfUsername.validate();
            }
        });
    }
}
