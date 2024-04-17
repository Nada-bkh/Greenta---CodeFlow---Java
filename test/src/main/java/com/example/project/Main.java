package com.example.project;

import com.example.project.Connections.MyConnection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MyConnection mc=new MyConnection();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Product.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Product CRUD !!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}