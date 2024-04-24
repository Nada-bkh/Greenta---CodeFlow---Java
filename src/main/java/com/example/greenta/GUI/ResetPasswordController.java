package com.example.greenta.GUI;

import com.example.greenta.Exceptions.IncorrectPasswordException;
import com.example.greenta.Exceptions.SamePasswordException;
import com.example.greenta.Services.PasswordResetService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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
    private String email; // Store the phone number received from the PhoneNumberController

    public void setEmail(String email) {
        this.email = email;
    }

    @FXML
    void resetPasswordBtn(ActionEvent event) throws SamePasswordException, IncorrectPasswordException{
        String newPassword = newPass.getText();
        String email = ""; // Get email from previous steps

        // Reset password and send email notification
        PasswordResetService resetService = new PasswordResetService();
        try {
            resetService.resetPassword(email, newPassword);
            resetService.sendEmailNotification(email);
            // Navigate to login page or show success message
        } catch (SamePasswordException | IncorrectPasswordException e) {
            // Handle exceptions
        }
    }

    @FXML
    void returnBtn(MouseEvent event) {

    }

}
