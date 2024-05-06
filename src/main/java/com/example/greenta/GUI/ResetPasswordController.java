package com.example.greenta.GUI;

import com.example.greenta.Exceptions.IncorrectPasswordException;
import com.example.greenta.Exceptions.SamePasswordException;
import com.example.greenta.Exceptions.UserNotFoundException;
import com.example.greenta.Services.PasswordResetService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ResetPasswordController {

    @FXML
    private Label InvalidPassword;

    @FXML
    private Label PasswordDontMatch;

    @FXML
    private PasswordField confirmNewPass;

    @FXML
    private TextField newPass;

    @FXML
    private Button resetPassword;

    @FXML
    private Label returnBtn;

    private PasswordResetService passwordResetService = new PasswordResetService();
    private String email; // Store the email received from the previous step
    private String enteredCode; // Store the verification code entered by the user

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEnteredCode(String enteredCode) {
        this.enteredCode = enteredCode;
    }

    @FXML
    void resetPasswordBtn(ActionEvent event) {
        String newPassword = newPass.getText();
        String confirmPassword = confirmNewPass.getText();

        try {
            if (!newPassword.equals(confirmPassword)) {
                PasswordDontMatch.setText("Passwords don't match. Please enter the same password in both fields.");
                return;
            }
            // Reset password and send email notification
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            String phoneNumber = (String) stage.getUserData();
            passwordResetService.resetPasswordProcess(phoneNumber.replace("+216", ""), newPassword);
            String email = this.email;

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Password changed successfully !");
            alert.setHeaderText(null);
            alert.setContentText("You have been notified by this change, please check your email.");
            alert.showAndWait();

            // Navigate to login page or show success message
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                    .getResource("/com/example/greenta/PasswordResetSuccessful.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (SamePasswordException e) {
            InvalidPassword.setText("New password must be different from the old password.");
            InvalidPassword.setVisible(true);
        } catch (IncorrectPasswordException e) {
            InvalidPassword.setText("Password must contain at least one uppercase letter," +
                    " one lowercase letter, one digit, and be at least 6 characters long.");
            InvalidPassword.setVisible(true);
        } catch (UserNotFoundException e) {
            InvalidPassword.setText("This user doesn't exist.");
            InvalidPassword.setVisible(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void returnClicked(MouseEvent event) {
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
