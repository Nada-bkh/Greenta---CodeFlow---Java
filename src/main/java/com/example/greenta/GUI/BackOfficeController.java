package com.example.greenta.GUI;

import com.example.greenta.Exceptions.PermissionException;
import com.example.greenta.Exceptions.UserNotFoundException;
import com.example.greenta.Models.User;
import com.example.greenta.Services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class BackOfficeController {
    private final UserService userService = UserService.getInstance();

    @FXML
    private ListView<User> userListView;
    private User currentUser;

    @FXML
    private void initialize() {
        List<User> users = userService.getUsers();
        ObservableList<User> observableUsers = FXCollections.observableArrayList(users);
        userListView.setItems(observableUsers);
        userListView.setCellFactory(listView -> UserListCell.create());
        userListView.setFixedCellSize(50); // Set a fixed cell size for virtualization
    }

    @FXML
    private Label usernameLabel;


    @FXML
    void banAccount(MouseEvent event) throws PermissionException {
        User selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(usernameLabel.getScene().getWindow());
            alert.setTitle("No User Selected");
            alert.setHeaderText("Please select a user to ban.");
            alert.setContentText("No user was selected in the list view.");
            alert.showAndWait();
            return;
        }
        try {
            UserService.getInstance().banUser(UserService.getInstance().getUserbyID(selectedUser.getId()), selectedUser);
        } catch (UserNotFoundException e) {
            System.err.println("Error banning user: " + e.getMessage());
        }
    }

    @FXML
    void profileButton(MouseEvent event) {

    }
    @FXML
    void signOut(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/User.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
