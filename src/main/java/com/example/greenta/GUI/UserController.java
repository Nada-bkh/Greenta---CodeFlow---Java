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

import java.io.*;
import java.sql.*;
import java.util.Base64;


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
    private static final String CREDENTIALS_FILE = "credentials.txt";
    private User currentUser;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(UserController.class.getResource("/com/example/greenta/User.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Greenta");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void initialize() {
        loadCredentials();
    }

    // Save the email and password to a file
    private void saveCredentials(String email, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE))) {
            writer.write(email + "," + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load the email and password from a file
    private void loadCredentials() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(",");
                emailField.setText(parts[0]);
                passwordField.setText(parts[1]);
                rememberMeCheckBox.setSelected(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Clear the saved credentials
    private void clearCredentials() {
        File file = new File(CREDENTIALS_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @FXML
    public void rememberMe(ActionEvent actionEvent) {
        if (rememberMeCheckBox.isSelected()) {
            String email = emailField.getText();
            String password = passwordField.getText();
            saveCredentials(email, password);
        } else {
            clearCredentials();
        }
    }

    public void onLoginClick(ActionEvent actionEvent) throws SQLException, UserNotFoundException {
        String email = emailField.getText();
        String password = passwordField.getText();
      /*  if (!sessionService.attempts(email, password)) {
            // Display an error message to the user
            UserNotFoundExceptionLabel.setText("Too many incorrect attempts. Account locked. Please contact the admin to unlock your account.");
            return;
        }*/

        // Retrieve user from the database
        User user = userService.getUserbyEmail(email);

        // Verify password
        if (!userService.verifyPassword(password, user.getPassword())) {
            return;
        }
        // Retrieve user from the database
        user = userService.getUserbyEmail(email);
        // Check if user exists
        System.out.println(user);
        System.out.println(user.getPassword());
        System.out.println(password);
        // Verify password
        if (!userService.verifyPassword(password, user.getPassword())) {
            System.out.println("no");
            return;
        }
        try {
            sessionService.setCurrentUser(user);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/FrontHome.fxml"));
            Parent root = loader.load();
            FrontHomeController frontHomeController = loader.getController();
            frontHomeController.initialize(user.getId());

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (rememberMeCheckBox.isSelected()) {
            saveCredentials(email, password);
        } else {
            // Clear saved credentials if "Remember Me" checkbox is not checked
            clearCredentials();
        }
    }

    public void onForgetPasswordClick(MouseEvent mouseEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/greenta/PhoneNumber.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

