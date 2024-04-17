package com.example.greenta.Greenta;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFXML extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFXML.class.getResource("/com/example/greenta/User.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Greenta");
        stage.setScene(scene);
        stage.show();
    }

    // This method is not required if you're not launching the application from this class
    public static void main(String[] args) {
        launch();
    }
}
