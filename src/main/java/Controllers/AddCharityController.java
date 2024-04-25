package Controllers;
import Entities.Charity;
import Services.CharityService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.Date;

public class AddCharityController {

    @FXML
    private DatePicker DatePick;

    @FXML
    private TextField ImgPath;

    @FXML
    private TextField LcField;

    @FXML
    private TextField NameField;

    @FXML
    private TextField amField;

    // Inject CharityService
    private CharityService charityService;

    // Setter method for CharityService injection
    public void setCharityService(CharityService charityService) {
        this.charityService = charityService;
    }

    @FXML
    public void initialize() {
        // Initialize the charityService field here
        charityService = new CharityService(); // Replace CharityServiceImpl with the actual implementation class
    }

    @FXML
    void SaveBtn(ActionEvent event) {
        // Retrieve data from input fields
        String name = NameField.getText();
        String amountText = amField.getText();
        String location = LcField.getText();
        String picture = ImgPath.getText();

        // Check for empty fields
        if (name.isEmpty() || amountText.isEmpty() || location.isEmpty() || picture.isEmpty() || DatePick.getValue() == null) {
            showAlert("Missing Information", "Please fill in all fields.");
            return; // Exit the method early
        }

        // Validate the date
        LocalDate currentDate = LocalDate.now();
        LocalDate selectedDate = DatePick.getValue();
        if (selectedDate.isBefore(currentDate)) {
            showAlert("Invalid Date", "Please select a future date.");
            return; // Exit the method early
        }

        // Parse amount to double
        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            // Handle invalid input for amount
            showAlert("Invalid Amount", "Please enter a valid amount.");
            return; // Exit the method early
        }

        // Create Charity object
        Charity charity = new Charity();
        charity.setName_of_charity(name);
        charity.setAmount_donated(amount);
        charity.setLocation(location);
        charity.setPicture(picture);
        charity.setLast_date(java.sql.Date.valueOf(selectedDate));

        // Add Charity to the database using CharityService
        charityService.addCharity(charity);

        // Show success alert
        showAlert("Charity Added", "The charity has been successfully added.");
    }

    // Method to show an alert
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
