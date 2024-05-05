package com.example.greenta.GUI;

import com.example.greenta.Exceptions.PermissionException;
import com.example.greenta.Exceptions.UserNotFoundException;
import com.example.greenta.Models.User;
import com.example.greenta.Services.SessionService;
import com.example.greenta.Services.UserService;
import com.example.greenta.Utils.Type;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BackOfficeController {
    private final UserService userService = UserService.getInstance();
    private final SessionService sessionService = SessionService.getInstance();

    @FXML
    private ImageView BOImage;

    @FXML
    private Label backOfficeButton;

    @FXML
    private Label charityLabel;

    @FXML
    private Label coursesLabel;

    @FXML
    private Label eventLabel;

    @FXML
    private Label homeLabel;

    @FXML
    private Button profileLabel;

    @FXML
    private Label recruitmentLabel;

    @FXML
    private Label shopLabel;

    @FXML
    private Button unlockButton;

    @FXML
    private ListView<User> userListView;
    @FXML
    private TableView<User> bannedUsers;

    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableView<User> lockedUsers;

    @FXML
    private TableColumn<User, String> firstNameColumn1;

    @FXML
    private TableColumn<User, String> emailColumn1;
    private User currentUser;

    @FXML
    public void initialize(int userId) throws UserNotFoundException {
        currentUser = userService.getUserbyID(userId);
        profileLabel.setText(currentUser.getFirstname());
        getUsers();
    }

    private void getUsers() {
        List<User> users = userService.getUsers();
        ObservableList<User> observableUsers = FXCollections.observableArrayList(users);
        userListView.setItems(observableUsers);
        userListView.setCellFactory(listView -> UserListCell.create());
        userListView.setFixedCellSize(50); // Set a fixed cell size for virtualization
        populateBannedUsersTable();
        populateLockedUsersTable();
    }
    private void populateBannedUsersTable() {
        List<User> bannedUsersList = userService.getUsers().stream()
                .filter(User::getIsBanned)
                .collect(Collectors.toList());

        ObservableList<User> bannedUsersData = FXCollections.observableArrayList(bannedUsersList);
        bannedUsers.setItems(bannedUsersData);

        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstname()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        bannedUsers.refresh();
    }

    private void populateLockedUsersTable() {
        List<User> lockedUsersList = userService.getUsers().stream()
                .filter(user -> !user.getIsBanned())
                .collect(Collectors.toList());

        ObservableList<User> lockedUsersData = FXCollections.observableArrayList(lockedUsersList);
        lockedUsers.setItems(lockedUsersData);

        firstNameColumn1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstname()));
        emailColumn1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        lockedUsers.refresh();
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
        if (selectedUser.getRoles().equals(Type.ROLE_ADMIN)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Permission Denied");
            alert.setHeaderText("You can't ban an administrator.");
            alert.setContentText("Only clients can be banned.");
            alert.showAndWait();
            return;
        }
        try {
            UserService.getInstance().banUser(selectedUser);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ban Successful");
            alert.setHeaderText(null);
            alert.setContentText("Account banned successfully!");
            alert.showAndWait();
            getUsers();
        } catch (UserNotFoundException e) {
            System.err.println("Error banning user: " + e.getMessage());
        }
    }

    @FXML
    void unbanAccount(MouseEvent event) {
        User selectedUser = bannedUsers.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(usernameLabel.getScene().getWindow());
            alert.setTitle("No User Selected");
            alert.setHeaderText("Please select a user to unban.");
            alert.setContentText("No user was selected in the list view.");
            alert.showAndWait();
            return;
        }
        try {
            UserService.getInstance().unbanUser(currentUser, selectedUser);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Unban Successful");
            alert.setHeaderText(null);
            alert.setContentText("Account unbanned successfully!");
            alert.showAndWait();
            getUsers();
        } catch (UserNotFoundException e) {
            System.err.println("Error unbanning user: " + e.getMessage());
        } catch (PermissionException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    public void unlockAccount(ActionEvent actionEvent) {
        User selectedUser = lockedUsers.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            String userEmail = selectedUser.getEmail();
            sessionService.unlockAccount(userEmail);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Unlock Successful");
            alert.setHeaderText(null);
            alert.setContentText("Account unlocked successfully!");
            alert.showAndWait();
            getUsers();
            userListView.refresh();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No User Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a user to unlock.");
            alert.showAndWait();
        }
    }

    @FXML
    void charityButton(MouseEvent event) {

    }

    @FXML
    void coursesButton(MouseEvent event) {

    }

    @FXML
    void eventButton(MouseEvent event) {

    }

    @FXML
    void homeButton(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/FrontHome.fxml"));
            Parent root = loader.load();
            FrontHomeController frontHomeController = loader.getController();
            frontHomeController.initialize(currentUser.getId());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException | UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void profileClicked(ActionEvent event) throws UserNotFoundException {
        User user = userService.getUserbyEmail(currentUser.getEmail());
        try {
            sessionService.setCurrentUser(user);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/Profile.fxml"));
            Parent root = loader.load();
            ProfileController profileController = loader.getController();
            profileController.initializeProfile(user.getId());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void recruitmentButton(MouseEvent event) {

    }

    @FXML
    void shopButton(MouseEvent event) {

    }

    @FXML
    void signOut(MouseEvent event) {
        sessionService.logout();
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

    @FXML
    void backOffice(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/BackOffice.fxml"));
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
