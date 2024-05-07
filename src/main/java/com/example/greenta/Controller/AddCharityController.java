package com.example.greenta.Controller;
import com.example.greenta.Models.Charity;
import com.example.greenta.Services.CharityService;
import com.example.greenta.Services.ExcelService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class AddCharityController {

    @FXML
    private DatePicker DatePick;

    @FXML
    private Button excel;

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

    // Inject ExcelService
    private ExcelService excelService;



    // Setter methods for CharityService and ExcelService injection
    public void setCharityService(CharityService charityService) {
        this.charityService = charityService;
    }

    public void setExcelService(ExcelService excelService) {
        this.excelService = excelService;
    }

    @FXML
    public void initialize() {
        // Initialize the charityService field here
        charityService = new CharityService();

        // Initialize the excelService field here
        excelService = new ExcelService();
    }

    @FXML
    void chooseImage(ActionEvent event) {
        System.out.println("chooseImage");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            ImgPath.setText(selectedFile.getAbsolutePath());

            try {
                BufferedImage image = ImageIO.read(selectedFile);
                File output=new File("src/main/resources"+selectedFile.getAbsolutePath());
                ImageIO.write(image,"png",output);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
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

    public void handleSelectImage(ActionEvent event) {
        // Create a file chooser dialog
        FileChooser fileChooser = new FileChooser();

        // Set the title of the dialog
        fileChooser.setTitle("Select Image");

        // Set the initial directory for the dialog (optional)
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // Add filters to the dialog to only allow certain file types (optional)
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        // Show the dialog and wait for the user's selection
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        // Check if a file was selected
        if (selectedFile != null) {
            ImgPath.setText(selectedFile.getAbsolutePath());

            try {
                BufferedImage image = ImageIO.read(selectedFile);
                File output = new File("src/main/resources/images"); // Specify a valid file path here
                ImageIO.write(image, "png", output);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @FXML
    void excel(ActionEvent event) {
        // Prompt the user to select a file location and name for the Excel file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            String filePath = file.getAbsolutePath();

            // Call the ExcelCreator method with the charities list and file path
            excelService.ExcelCreator(charityService.showCharity(), filePath);
        } else {
            // User canceled the file selection
            System.out.println("File selection canceled.");
        }
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

