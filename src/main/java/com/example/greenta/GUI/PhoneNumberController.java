package com.example.greenta.GUI;

import com.example.greenta.Services.PasswordResetService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class PhoneNumberController implements Initializable {

    @FXML
    private Label EmptyFieldException;

    @FXML
    private Label InvalidPhoneNumber;

    @FXML
    private Button SendCode;

    @FXML
    private TextField phone;

    @FXML
    private ComboBox<String> country;

    private PasswordResetService passwordResetService = PasswordResetService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Hardcoded list of countries
        ArrayList<String> countriesList = new ArrayList<>();
        countriesList.add("+216");
        countriesList.add("+213");
        countriesList.add("+218");
        ObservableList<String> countries = FXCollections.observableArrayList(countriesList);
        country.setItems(countries);
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
        String selectedCountryCode = country.getValue();
        String phoneNumber = phone.getText();
        if (isValidPhoneNumber(selectedCountryCode, phoneNumber)) {
            // Concatenate the country code and the phone number
            String fullPhoneNumber = selectedCountryCode + phoneNumber;
            passwordResetService.sendVerificationCode(fullPhoneNumber);
            // Navigate to RandomCodeController
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/RandomCode.fxml"));
            Parent root = loader.load();
            RandomCodeController randomCodeController = loader.getController();
            // Pass any necessary data to RandomCodeController if required
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setUserData(fullPhoneNumber);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            InvalidPhoneNumber.setText("Invalid phone number format. Please enter a valid phone number.");
        }
    }

    // Method to validate phone number format
        private boolean isValidPhoneNumber(String selectedCountryCode, String phoneNumber) {
            // Check if the selectedCountryCode is not null or empty
            if (selectedCountryCode == null || selectedCountryCode.isEmpty()) {
                return false;
            }

            // Check if the phoneNumber is not null or empty and has the correct length
            if (phoneNumber == null || phoneNumber.isEmpty() || phoneNumber.length() != 8) {
                return false;
            }

            // Check if the phoneNumber contains only digits
            try {
                int phoneNumberAsInt = Integer.parseInt(phoneNumber);
            } catch (NumberFormatException e) {
                return false;
            }

            // If all checks pass, the phone number is valid
            return true;
        }
}
