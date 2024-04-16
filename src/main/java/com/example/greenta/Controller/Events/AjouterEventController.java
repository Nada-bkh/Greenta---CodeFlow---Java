package com.example.greenta.Controller.Events;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.example.greenta.Models.Events.Event;
import com.example.greenta.Services.Events.EventService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AjouterEventController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField startDateField;

    @FXML
    private TextField endDateField;

    @FXML
    private TextField locationField;

    @FXML
    private TextField organizerIdField;

    @FXML
    private TextField capacityField;

    @FXML
    private Text errorTitle;

    @FXML
    private Text errorDescription;

    @FXML
    private Text errorStartDate;

    @FXML
    private Text errorEndDate;

    @FXML
    private Text errorLocation;

    @FXML
    private Text errorOrganizerId;

    @FXML
    private Text errorCapacity;

    @FXML
    void ajouterEvent(ActionEvent event) {
        String title = titleField.getText();
        String description = descriptionField.getText();
        String startDateStr = startDateField.getText();
        String endDateStr = endDateField.getText();
        String location = locationField.getText();
        String organizerIdStr = organizerIdField.getText();
        String capacityStr = capacityField.getText();

        // Validate input
        String errorTitleText = validateField(title, "Title");
        String errorDescriptionText = validateField(description, "Description");
        String errorStartDateText = validateField(startDateStr, "Start Date");
        String errorEndDateText = validateField(endDateStr, "End Date");
        String errorLocationText = validateField(location, "Location");
        String errorOrganizerIdText = validateField(organizerIdStr, "Organizer ID");
        String errorCapacityText = validateField(capacityStr, "Capacity");

        // Display or hide error messages
        errorTitle.setText(errorTitleText);
        errorTitle.setVisible(errorTitleText != null);

        errorDescription.setText(errorDescriptionText);
        errorDescription.setVisible(errorDescriptionText != null);

        errorStartDate.setText(errorStartDateText);
        errorStartDate.setVisible(errorStartDateText != null);

        errorEndDate.setText(errorEndDateText);
        errorEndDate.setVisible(errorEndDateText != null);

        errorLocation.setText(errorLocationText);
        errorLocation.setVisible(errorLocationText != null);

        errorOrganizerId.setText(errorOrganizerIdText);
        errorOrganizerId.setVisible(errorOrganizerIdText != null);

        errorCapacity.setText(errorCapacityText);
        errorCapacity.setVisible(errorCapacityText != null);

        if (errorTitleText != null || errorDescriptionText != null || errorStartDateText != null ||
                errorEndDateText != null || errorLocationText != null || errorOrganizerIdText != null ||
                errorCapacityText != null) {
            return; // There are errors, do not proceed
        }

        // If input is valid, create the event and continue
        LocalDateTime startDate = LocalDateTime.parse(startDateStr);
        LocalDateTime endDate = LocalDateTime.parse(endDateStr);
        int organizerId = Integer.parseInt(organizerIdStr);
        int capacity = Integer.parseInt(capacityStr);

        Event eventObj = new Event(title, description, startDate, endDate, location, organizerId, capacity);
        EventService eventService = new EventService();
        try {
            eventService.add(eventObj);

            // Load and show the AfficherEvent page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/Sponsoring/AfficherEvent.fxml"));
            Parent root = loader.load();
            AfficherEventController controller = loader.getController();
            controller.setData(title);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Afficher les événements");
            stage.show();

            // Close the current stage (AjouterEvent)
            Stage currentStage = (Stage) titleField.getScene().getWindow();
            currentStage.close();
        } catch (SQLException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    private String validateField(String value, String fieldName) {
        if (value.isEmpty()) {
            return "Please fill in the " + fieldName + " field.";
        }

        return null; // No error
    }
}

