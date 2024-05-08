package com.example.greenta.Greenta;

import com.example.greenta.Utils.MyConnection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        MyConnection mc = MyConnection.getInstance();
  FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/greenta/Product.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/greenta/ProductCategory.fxml"));

//   FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/greenta/Shop.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/greenta/ProductCard.fxml"));



        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("GREENTA ");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}