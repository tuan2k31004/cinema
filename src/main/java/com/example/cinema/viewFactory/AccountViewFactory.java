package com.example.cinema.viewFactory;

import com.example.cinema.CinemaApplication;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.SneakyThrows;


public class AccountViewFactory {
    public static AccountViewFactory instance = new AccountViewFactory();
    private BorderPane loginView;
    private BorderPane signupView;
    private StringProperty selectedViewName;
    private final Stage stage;

    public StringProperty selectedViewNameProperty() {
        return selectedViewName;
    }

    public void setSelectedViewName(String selectedViewName) {
        this.selectedViewName.set(selectedViewName);
    }

    private AccountViewFactory() {
        stage = new Stage();
        selectedViewName = new SimpleStringProperty("");
    }

    @SneakyThrows
    public void show() {
        FXMLLoader loader = new FXMLLoader(CinemaApplication.class.getResource("fxml/account_view/account.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Tài khoản");
        stage.setResizable(false);
        stage.show();
    }

    public void close() {
        stage.close();
        selectedViewName = new SimpleStringProperty("");
    }

    @SneakyThrows
    public BorderPane getLoginView() {
        FXMLLoader loader = new FXMLLoader(CinemaApplication.class.getResource("fxml/account_view/login.fxml"));
        loginView = loader.load();
        return loginView;
    }

    @SneakyThrows
    public BorderPane getSignupView() {
        FXMLLoader loader = new FXMLLoader(CinemaApplication.class.getResource("fxml/account_view/signup.fxml"));
        signupView = loader.load();
        return signupView;
    }
}
