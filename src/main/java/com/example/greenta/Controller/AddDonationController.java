package com.example.greenta.Controller;

import com.example.greenta.Models.Charity;
import com.example.greenta.Models.Donation;
import com.example.greenta.Services.CharityService;
import com.example.greenta.Services.DonationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AddDonationController {

    @FXML
    private TextField AddressId;
    @FXML
    private TextField amountId;


    @FXML
    private TextField firstNameId;

    @FXML
    private TextField lastNameId;

    @FXML
    private TextField phoneId;

    private DonationService donationService; // Declare donationService

    private CharityService charityService;
    private Charity selectedCharity;

    public void setDonationService(DonationService donationService) {
        this.donationService = donationService;
    }

    public void setCharityService(CharityService charityService) {
        this.charityService = charityService;
    }

    // This method initializes the donationService
    public void initializeDonationService(DonationService donationService) {
        this.donationService = donationService;
    }

    // Method to handle donation action
    @FXML
    void donateId(ActionEvent event) {
        // Check if donationService is null
        if (donationService == null) {
            // Show an error alert indicating that donationService is not initialized
            showAlert("Error", "Donation service is not initialized.");
            return;
        }

        // Get the input values from the text fields
        String firstName = firstNameId.getText();
        String lastName = lastNameId.getText();
        String address = AddressId.getText();
        String phoneNumberText = phoneId.getText();
        String amountText = amountId.getText();

        // Check if any of the fields are empty
        if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || phoneNumberText.isEmpty() || amountText.isEmpty()) {
            // Show an alert for empty fields
            showAlert("Missing Information", "Please fill in all fields.");
            return; // Exit the method early
        }

        // Perform error handling for parsing phone number and amount
        int phoneNumber;
        double amount;
        try {
            phoneNumber = Integer.parseInt(phoneNumberText);
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            // Show an alert for invalid input
            showAlert("Invalid Input", "Phone number and amount must be valid numbers.");
            return; // Exit the method early
        }

        // Create a new Donation object
        Donation donation = new Donation();
        donation.setFirst_name(firstName);
        donation.setLast_name(lastName);
        donation.setAddress(address);
        donation.setPhone_number(phoneNumber);
        donation.setAmount(amount);

        // Add the donation to the selected charity
        donationService.addDonation(donation, selectedCharity);

        // Show a success alert
        showAlert("Donation Successful", "Thank you for your donation!");

        // Clear input fields after successful donation
        clearFields();
    }

    // Method to show an alert
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Method to clear input fields
    private void clearFields() {
        firstNameId.clear();
        lastNameId.clear();
        AddressId.clear();
        phoneId.clear();
    }

    // Setter for selected charity
    public void setSelectedCharity(Charity selectedCharity) {
        this.selectedCharity = selectedCharity;
    }

    // Getter for selected charity
    public Charity getSelectedCharity() {
        return selectedCharity;
    }
}

