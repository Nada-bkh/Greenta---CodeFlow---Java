package com.example.greenta.GUI;

import com.example.greenta.Models.Charity;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.example.greenta.Services.CharityService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.event.ActionEvent;



import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModifyCharityController {

    private Charity charity; // Declare charity as a member variable
    private ShowCharityController showCharityController; // Reference to the ShowCharityController

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField amountTextField;

    @FXML
    private TextField locationTextField;

    @FXML
    private TextField lastDateTextField;

    public void setShowCharityController(ShowCharityController showCharityController) {
        this.showCharityController = showCharityController;
    }

    @FXML
    void saveChanges(ActionEvent event) {
        // Implement the logic to save the changes to the charity
        // You can retrieve the values from the text fields like this:
        String name = nameTextField.getText();
        String amountText = amountTextField.getText();
        String location = locationTextField.getText();
        String lastDateStr = lastDateTextField.getText();

        // Check for empty fields
        if (name.isEmpty() || amountText.isEmpty() || location.isEmpty() || lastDateStr.isEmpty()) {
            showAlert("Missing Information", "Please fill in all fields.");
            return; // Exit the method early
        }

        double amountDonated = 0;
        try {
            amountDonated = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            // Handle invalid input for amount
            showAlert("Invalid Amount", "Please enter a valid amount.");
            return; // Exit the method early
        }

        Date lastDate = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            lastDate = dateFormat.parse(lastDateStr);
        } catch (ParseException e) {
            // Handle parse exception
            showAlert("Invalid Date", "Please enter a valid date in the format yyyy-MM-dd.");
            return; // Exit the method early
        }

        // Update the charity object
        charity.setName_of_charity(name);
        charity.setAmount_donated(amountDonated);
        charity.setLocation(location);
        charity.setLast_date(lastDate);

        // Update the charity in the database
        boolean updated = CharityService.updateCharity(charity);
        if (updated) {
            // Pass the updated charity to the ShowCharityController
            showCharityController.updateCharity(charity);
            // Close the modify window automatically
            ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
        } else {
            showAlert("Update Failed", "Failed to update charity in the database.");
        }
    }

    @FXML
    void cancelChanges(ActionEvent event) {
        // Close the modify window without saving changes
        ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
    }

    public void initData(Charity charity) {
        this.charity = charity;
        // Populate the text fields with the selected charity's information
        nameTextField.setText(charity.getName_of_charity());
        amountTextField.setText(String.valueOf(charity.getAmount_donated()));
        locationTextField.setText(charity.getLocation());
        lastDateTextField.setText(charity.getLast_date().toString());
    }

    // Method to show an alert
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
