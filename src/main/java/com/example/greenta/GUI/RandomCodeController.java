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
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @FXML
    private TextField digit1;

    @FXML
    private TextField digit2;

    @FXML
    private TextField digit3;

    @FXML
    private TextField digit4;
    @FXML
    private Label phoneNumberLabel;

    private PasswordResetService passwordResetService = PasswordResetService.getInstance(); // Initialize this in the constructor or with dependency injection

    private List<TextField> textFields;
    // Current index of focused text field
    private int currentIndex;

    public void initialize() {
        // ... other initialization code ...

        // Add text fields to list
        textFields = new ArrayList<>();
        textFields.add(digit1);
        textFields.add(digit2);
        textFields.add(digit3);
        textFields.add(digit4);

        // Initialize index
        currentIndex = 0;

        // Add change listeners to text fields
        for (TextField textField : textFields) {
            textField.textProperty().addListener((obs, oldText, newText) -> {
                // Increment index and wrap around
                currentIndex = (currentIndex + 1) % textFields.size();
                // Request focus on next text field
                textFields.get(currentIndex).requestFocus();
            });
        }
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
        String calculatedCode = String.valueOf(Integer.parseInt(digit4.getText()) + 10 * Integer.parseInt(digit3.getText()) + 100 * Integer.parseInt(digit2.getText()) + 1000 * Integer.parseInt(digit1.getText()));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        String phoneNumber = (String) stage.getUserData();
        // Verify the verification code
      if (passwordResetService.verifySMSCode(phoneNumber, calculatedCode)) {
            // Navigate to ResetPasswordController
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/reset-password.fxml"));
            Parent root = loader.load();
            ResetPasswordController resetPasswordController = loader.getController();

            // Pass any necessary data to ResetPasswordController if required
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            InvalidCode.setText("Invalid verification code. Please enter the correct code.");
        }
    }
}
