package com.example.greenta.GUI;

import com.example.greenta.Exceptions.EmptyFieldException;
import com.example.greenta.Exceptions.IncorrectPasswordException;
import com.example.greenta.Exceptions.InvalidEmailException;
import com.example.greenta.Exceptions.InvalidPhoneNumberException;
import com.example.greenta.Models.User;
import com.example.greenta.Services.UserService;
import com.example.greenta.Services.ValidationService;
import com.example.greenta.Utils.MyConnection;
import com.example.greenta.Utils.Type;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
                confirmPasswordField.getText();
                user.setPhone(phoneField.getText());
                user.setRoles(Type.ROLE_CLIENT);
                userService.addUser(user);
            } else {
                firstnameLabel.setText("Please enter your firstname.");
                lastnameLabel.setText("Please enter your lastname.");
                passwordLabel.setText("Please enter your password.");
                confirmPasswordLabel.setText("This field if necessary.");
                phoneNumberLabel.setText("Please enter your phone number.");
            }
            if (!validationService.isValidEmail(emailField.getText())) {
                emailLabel.setText("Invalid email, please check your email address.");
            }
            // Valider le format du numéro de téléphone (s'il est fourni)
            if (!phoneNumberLabel.getText().isEmpty() && !validationService.isValidPhoneNumber(phoneField.getText())) {
                phoneNumberLabel.setText("Invalid phone number format.");
            }
            // Valider le format du mot de passe
            if (!validationService.isValidPassword(passwordField.getText())) {
                passwordLabel.setText("Password must contain at least one uppercase letter, one lowercase letter, one digit, and be at least 6 characters long.");
            }
        } catch (InvalidEmailException | IncorrectPasswordException | InvalidPhoneNumberException |
                 EmptyFieldException e) {
            System.err.println(e);
        }
    }

}
