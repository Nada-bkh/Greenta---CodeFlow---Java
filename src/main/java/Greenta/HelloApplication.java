package greenta;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/greenta/FrontJob.fxml"));//USE
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/greenta/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1147, 819);
        stage.setTitle("job creation");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}