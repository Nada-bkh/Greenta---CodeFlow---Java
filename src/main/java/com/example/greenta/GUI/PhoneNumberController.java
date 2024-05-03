package com.example.greenta.GUI;

import com.example.greenta.Services.PasswordResetService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class PhoneNumberController {

    @FXML
    private Label EmptyFieldException;

    @FXML
    private Label InvalidPhoneNumber;

    @FXML
    private Label ResendCode;

    @FXML
    private Button SendCode;

    @FXML
    private Label forgetPassword;

    @FXML
    private TextField phone;

    @FXML
    private ComboBox<?> country;

    private PasswordResetService passwordResetService = PasswordResetService.getInstance();

    public void Initialize() {

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
    void sendCodeBtn(ActionEvent event) throws IOException {
        String phoneNumber = phone.getText();
        if (isValidPhoneNumber(phoneNumber)) {
            // Call sendVerificationCode method from PasswordResetService
            passwordResetService.sendVerificationCode(phoneNumber);

            // Navigate to RandomCodeController
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/RandomCode.fxml"));
            Parent root = loader.load();
            RandomCodeController randomCodeController = loader.getController();

            // Pass any necessary data to RandomCodeController if required
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setUserData(phoneNumber);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            InvalidPhoneNumber.setText("Invalid phone number format. Please enter a valid phone number.");
        }
    }

    // Method to validate phone number format
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Implement your phone number validation logic here
        // For example, you might use regular expressions to check if the phone number has the correct format
        return true; // Placeholder return value, replace with actual validation logic
    }
}
