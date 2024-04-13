package com.example.greenta.GUI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;


public class UserController extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(UserController.class.getResource("/com/example/greenta/User.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    // This method is not required if you're not launching the application from this class
    public static void main(String[] args) {
        launch();
    }

    public void onLoginClick(ActionEvent actionEvent) {

    }

    public void onForgetPasswordClick(MouseEvent mouseEvent) {
    }

    public void onSighUpClick(MouseEvent mouseEvent) {
    }
}

