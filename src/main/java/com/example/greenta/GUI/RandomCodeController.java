package com.example.greenta.GUI;

import com.example.greenta.Services.PasswordResetService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class RandomCodeController {

    @FXML
    private Label InvalidCode;

    @FXML
    private Label ResendCode;

    @FXML
    private Button Submit;

    @FXML
    private Label forgetPassword;

    @FXML
    private TextField code;

    private PasswordResetService passwordResetService; // Initialize this in the constructor or with dependency injection

    private String phoneNumber; // Store the phone number received from the PhoneNumberController

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @FXML
    void ResendCode(MouseEvent event) {
        String phoneNumber = ""; // Get the phone number from previous steps

        // Call sendVerificationCode method from PasswordResetService
        PasswordResetService resetService = new PasswordResetService();
        resetService.sendVerificationCode(phoneNumber);
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

    @FXML
    void submitBtn(ActionEvent event) throws IOException {
        String verificationCode = code.getText();

        // Verify the verification code
        if (passwordResetService.verifySMSCode(phoneNumber, verificationCode)) {
            // Navigate to ResetPasswordController
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/GUI/ResetPassword.fxml"));
            Parent root = loader.load();
            ResetPasswordController resetPasswordController = loader.getController();

            // Pass any necessary data to ResetPasswordController if required

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            InvalidCode.setText("Invalid verification code. Please enter the correct code.");
        }
    }
}
