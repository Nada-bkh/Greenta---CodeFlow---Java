package com.example.greenta.GUI;


import com.example.greenta.Exceptions.*;
import com.example.greenta.Models.User;
import com.example.greenta.Services.SessionService;
import com.example.greenta.Services.UserService;
import com.example.greenta.Services.ValidationService;
import com.example.greenta.Utils.MyConnection;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;


public class UserController extends Application {

    @FXML
    private Label UserNotFoundExceptionLabel;
    @FXML
    private Label EmptyFieldExceptionLabel;

    @FXML
    private Label InvalidEmailExceptionLabel;

    @FXML
    private Label InvalidPasswordExceptionLabel;

    @FXML
    private TextField emailField;

    @FXML
    private Label forgetPassword;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;
    @FXML
    private CheckBox rememberMeCheckBox;

    @FXML
    private Label signUp;

    UserService userService = UserService.getInstance();
    SessionService sessionService = SessionService.getInstance();
    ValidationService validationService = new ValidationService();
    Connection connection = MyConnection.getInstance().getConnection();
    private User currentUser;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(UserController.class.getResource("/com/example/greenta/User.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Greenta");
        stage.setScene(scene);
        stage.show();
    }
    // This method is not required if you're not launching the application from this class
    public static void main(String[] args) {
        launch();
    }

    public void onLoginClick(ActionEvent actionEvent) throws SQLException, UserNotFoundException {
        String email = emailField.getText();
        String password = passwordField.getText();
        // Retrieve user from the database
        User user = userService.getUserbyEmail(email);
        // Check if user exists
        System.out.println(user);
        System.out.println(user.getPassword());
        System.out.println(password);
        // Verify password
        if (!userService.verifyPassword(password, user.getPassword())) {
            System.out.println("no");
            return;
        }
        // If login is successful, navigate to hello-view.fxml
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/greenta/Profile.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onForgetPasswordClick(MouseEvent mouseEvent) {
    }

    public void onSighUpClick(MouseEvent mouseEvent) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/greenta/Register.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

