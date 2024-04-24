package tn.esprit.greenta;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FXMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(FXMain.class.getResource("gestion-cour-admin.fxml"));//ADMIN
         //FXMLLoader fxmlLoader = new FXMLLoader(FXMain.class.getResource("afficher-question-user.fxml"));//USER
        FXMLLoader fxmlLoader = new FXMLLoader(FXMain.class.getResource("afficher-cours-user.fxml"));//USER
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Greenta!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }
}