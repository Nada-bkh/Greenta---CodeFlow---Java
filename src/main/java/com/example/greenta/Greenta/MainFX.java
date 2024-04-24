package com.example.greenta.Greenta;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/AjouterEvent.fxml"));
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/EventFront.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Gérer les Events");
            primaryStage.show();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }}
