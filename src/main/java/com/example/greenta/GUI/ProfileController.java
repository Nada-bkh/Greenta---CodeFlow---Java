package com.example.greenta.GUI;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController extends Application {

    @FXML
    private ListView<String> profileListView;

    @FXML
    private Label selection;
    @FXML
    private Label firstname;

    @FXML
    private Label profileSideLabel;


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(UserController.class.getResource("/com/example/greenta/Profile.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Greenta");
        stage.setScene(scene);
        stage.show();
    }
    // This method is not required if you're not launching the application from this class
    public static void main(String[] args) {
        launch();
    }


    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] items = {"Java","C#","C","C++","Python"};
        profileListView.getItems().addAll(items);
        profileListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        profileListView.getSelectionModel().selectedItemProperty().addListener(this::selectionChanged);
    }


    private void selectionChanged(ObservableValue<? extends String> Observable, String oldVal, String newVal){
        ObservableList<String> selectedItems = profileListView.getSelectionModel().getSelectedItems();
        String getSelectedItem = (selectedItems.isEmpty())?"No Selected Item":selectedItems.toString();
        selection.setText(getSelectedItem);
    }
    @FXML
    private void editProfile() {
        // Implement edit profile functionality
        System.out.println("Edit profile clicked");
    }

    @FXML
    private void deleteAccount() {
        // Implement delete account functionality
        System.out.println("Delete account clicked");
    }

}
