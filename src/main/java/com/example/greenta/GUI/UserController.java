package com.example.greenta.GUI;


import at.favre.lib.crypto.bcrypt.BCrypt;
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
import java.util.HashMap;
import java.util.Map;


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
    private Map<String, Integer> loginAttemptsMap = new HashMap<>();

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

    private void saveCredentials(String email, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE))) {
            String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
            writer.write(email + "," + hashedPassword);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void loadCredentials() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line = reader.readLine();
            if (line!= null) {
                String[] parts = line.split(",");
                emailField.setText(parts[0]);
                String hashedPassword = parts[1];
                if (BCrypt.verifyer().verify(passwordField.getText().toCharArray(), hashedPassword).verified) {
                    passwordField.setText(passwordField.getText());
                }
                rememberMeCheckBox.setSelected(true);
            }
        } catch (IOException e) {
            System.err.println( e);
        }
    }

    private void clearCredentials() {
        emailField.setText("");
        passwordField.setText("");
        rememberMeCheckBox.setSelected(false);
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
        int attempts = 0;
        try {
            User user = userService.getUserbyEmail(email);
            attempts = loginAttemptsMap.getOrDefault(email, 0);
            if (attempts >= sessionService.MAX_LOGIN_ATTEMPTS) {
                throw new TooManyLoginAttemptsException("Too many login attempts.");
            }
            if (!sessionService.attempts(email, password)) {
                throw new IncorrectPasswordException("Incorrect password."); // Throw IncorrectPasswordException when password is invalid
            } if (email.isEmpty() || password.isEmpty()) {
                InvalidEmailExceptionLabel.setText("Please enter your email and password.");
                InvalidEmailExceptionLabel.setVisible(true);
                return;
            }
            if (!validationService.isValidEmail(email)) {
                throw new UserNotFoundException("Invalid email.");
            }
            if (!user.getIsActive()) {
                throw new AccountLockedException("Your account is locked.");
            }
            if (user.getIsBanned()) {
                throw new AccountBannedException("Your account is banned.");
            }
            if (!user.getIsActive()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Account locked");
                alert.setHeaderText(null);
                alert.setContentText("Too many attempts! Your account has been locked please contact us or change password.");
                alert.showAndWait();
            } else if (user.getIsBanned()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Account banned");
                alert.setHeaderText(null);
                alert.setContentText("Your account is banned please contact bannedAccounts@greenta.com.");
                alert.showAndWait();
            } else {
                try {
                    if (sessionService.attempts(email, password)) {
                        sessionService.setCurrentUser(user);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/FrontHome.fxml"));
                        Parent root = loader.load();
                        FrontHomeController frontHomeController = loader.getController();
                        frontHomeController.initialize(user.getId());
                        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root, 800, 600);
                        stage.setScene(scene);
                        stage.show();
                    } else {
                        attempts++;
                        loginAttemptsMap.put(email, attempts);
                        if (attempts >= sessionService.MAX_LOGIN_ATTEMPTS) {
                            // Account locked
                            displayAccountLockedMessage();
                            sessionService.lockAccount(email);
                        } else {
                            // Incorrect password or other error
                            displayIncorrectPasswordMessage(attempts);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (rememberMeCheckBox.isSelected()) {
                    saveCredentials(email, password);
                } else {
                    clearCredentials();
                }
            }
        } catch (IncorrectPasswordException e) {
            attempts++;
            loginAttemptsMap.put(email, attempts);
            InvalidPasswordExceptionLabel.setText("Incorrect password. Attempts left: " + (sessionService.MAX_LOGIN_ATTEMPTS - attempts));
            InvalidPasswordExceptionLabel.setVisible(true);
        } catch (TooManyLoginAttemptsException e) {
            displayAccountLockedMessage();
        } catch (AccountLockedException e) {
            UserNotFoundExceptionLabel.setText("Your account has been locked please contact us.");
            UserNotFoundExceptionLabel.setVisible(true);
        } catch (AccountBannedException e) {
            UserNotFoundExceptionLabel.setText("Your account is banned please contact bannedAccounts@greenta.com");
            UserNotFoundExceptionLabel.setVisible(true);
        } catch (UserNotFoundException e) {
            InvalidEmailExceptionLabel.setText("Invalid email.");
            InvalidEmailExceptionLabel.setVisible(true);
        }
    }
    private void displayAccountLockedMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Account locked");
        alert.setHeaderText(null);
        alert.setContentText("Too many attempts! Your account has been locked. Please contact us or change password.");
        alert.showAndWait();
    }
    private void displayIncorrectPasswordMessage(int attempts) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Incorrect password");
        alert.setHeaderText(null);
        alert.setContentText("Incorrect password. Attempts left: " + (sessionService.MAX_LOGIN_ATTEMPTS - attempts));
        alert.showAndWait();
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

