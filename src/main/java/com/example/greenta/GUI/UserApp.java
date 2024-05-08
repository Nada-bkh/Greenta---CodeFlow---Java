package com.example.greenta.GUI;

import com.example.greenta.Exceptions.*;
import com.example.greenta.Models.User;
import com.example.greenta.Services.UserService;
import com.example.greenta.Services.ValidationService;
import com.example.greenta.Utils.MyConnection;
import com.example.greenta.Utils.Type;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class UserApp {

    @FXML
    private TextField confirmPasswordField;

    @FXML
    private Label confirmPasswordLabel;

    @FXML
    private TextField emailField;

    @FXML
    private Label emailLabel;

    @FXML
    private TextField firstnameField;

    @FXML
    private Label firstnameLabel;

    @FXML
    private TextField lastnameField;

    @FXML
    private Label lastnameLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label passwordLabel;

    @FXML
    private TextField phoneField;

    @FXML
    private Label phoneNumberLabel;

    @FXML
    private Button registerButton;

    @FXML
    void registerOnAction(ActionEvent event) {
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        try {
            Connection connection = MyConnection.getInstance().getConnection();
            UserService userService = UserService.getInstance();
            ValidationService validationService = new ValidationService();
            User user = new User();
            if (!firstnameField.getText().isEmpty() || !lastnameField.getText().isEmpty() ||
                    !emailField.getText().isEmpty() || !passwordField.getText().isEmpty()) {
                user.setFirstname(firstnameField.getText());
                user.setLastname(lastnameField.getText());
                user.setEmail(emailField.getText());
                user.setPassword(passwordField.getText());
                user.setPhone(phoneField.getText());
                user.setRoles(Type.ROLE_CLIENT);
                userService.addUser(user);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Account Created");
                alert.setHeaderText(null);
                alert.setContentText("Your account has been created successfully.");
                alert.showAndWait();
                registerButton.getScene().getWindow().hide();
            } else {
                firstnameLabel.setText("Please enter your firstname.");
                lastnameLabel.setText("Please enter your lastname.");
                passwordLabel.setText("Please enter your password.");
                confirmPasswordLabel.setText("This field must match password field.");
                phoneNumberLabel.setText("Please enter your phone number.");
            }
            if (!confirmPassword.equals(password)) {
                throw new SamePasswordException("password dont match.");
            }
            if (!validationService.isValidEmail(emailField.getText())) {
                throw new InvalidEmailException("invalid email.");
            }
            if (!phoneNumberLabel.getText().isEmpty() && !validationService.isValidPhoneNumber(phoneField.getText())) {
                throw new InvalidPhoneNumberException("phone number invalid.");
            }
            if (!validationService.isValidPassword(passwordField.getText())) {
               throw new IncorrectPasswordException("Password invalid.");
            }
        } catch (EmptyFieldException e) {
            System.err.println(e);
        } catch (IncorrectPasswordException e){
            passwordLabel.setText("Password must contain at least one uppercase letter," +
                    " one lowercase letter, one digit, and be at least 6 characters long.");
            passwordLabel.setVisible(true);
        } catch (InvalidPhoneNumberException e){
            phoneNumberLabel.setText("Invalid phone number format.");
            phoneNumberLabel.setVisible(true);
        } catch (InvalidEmailException e){
            emailLabel.setText("Invalid email, please check your email address.");
            emailLabel.setVisible(true);
        }
        catch (SamePasswordException e){
            emailLabel.setText("Password don't match, please confirm your password.");
            emailLabel.setVisible(true);
        }
    }
    @FXML
    public void returnBTN(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/User.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
