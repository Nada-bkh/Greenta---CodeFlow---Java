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

    private PasswordResetService passwordResetService = new PasswordResetService();
    public void Initialize(){

    }

    @FXML
    void ResendCode(MouseEvent event) {

    }

    @FXML
    void returnClicked(MouseEvent event) {

    }

    @FXML
    void sendCodeBtn(ActionEvent event) throws IOException {
        String phoneNumber = phone.getText();
        // Call sendVerificationCode method from PasswordResetService
        PasswordResetService resetService = new PasswordResetService();
        resetService.sendVerificationCode(phoneNumber);

        // Navigate to RandomCodeController
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/GUI/RandomCode.fxml"));
        Parent root = loader.load();
        RandomCodeController randomCodeController = loader.getController();

        // Pass any necessary data to RandomCodeController if required

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
