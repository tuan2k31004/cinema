package com.example.cinema.controller;


import com.example.cinema.dao.UserDao;
import com.example.cinema.entity.User;
import com.example.cinema.util.PasswordEncode;
import com.example.cinema.viewFactory.AppViewFactory;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {
    @FXML
    private JFXTextField tfOldPw;
    @FXML
    private JFXTextField tfNewPw;
    @FXML
    private JFXTextField tfConfirmPw;

    public void handleChangePw(MouseEvent mouseEvent) {
        if (tfOldPw.validate() && tfNewPw.validate() && tfConfirmPw.validate()) {
            UserDao.instance.findById(AppViewFactory.instance.getUserCurrent().getUsername(),
                    User.class).ifPresent(user -> {
                if (user.getPassword().equals(PasswordEncode.encode(tfOldPw.getText()))) {
                    if (UserDao.instance.updatePassword(PasswordEncode.encode(tfNewPw.getText()))) {
                        AppViewFactory.instance.showSuccess("Đổi mật khẩu thành công");
                        AppViewFactory.instance.closeDialog();
                    }
                } else {
                    AppViewFactory.instance.showError("Mật khẩu không chính xác");
                }
            });
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfOldPw.getValidators().add(new RequiredFieldValidator("Bắt buộc"));
        tfNewPw.getValidators().addAll(new RequiredFieldValidator("Bắt buộc"),
                new RegexValidator("mật khẩu phải lớn hơn 8 ký tự") {
                    {
                        setRegexPattern(".{8,}");
                    }
                });
        tfConfirmPw.getValidators().add(new ValidatorBase("mật khẩu không khớp") {
            @Override
            protected void eval() {
                hasErrors.set(!tfConfirmPw.getText().equals(tfNewPw.getText()));
            }
        });
    }
}
