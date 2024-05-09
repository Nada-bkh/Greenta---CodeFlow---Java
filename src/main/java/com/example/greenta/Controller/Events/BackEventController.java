package com.example.greenta.Controller.Events;

import com.example.greenta.Controller.Reservations.ReservationController;
import com.example.greenta.Models.Event;
import com.example.greenta.Models.Reservation;
import com.example.greenta.Services.EventService;
import com.example.greenta.Services.ReservationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BackEventController {

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    @FXML
    private Button btnReservations;

    @FXML
    private Button btnImage;

    @FXML
    private Button btnRservation;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField capacityField;

    @FXML
    private DatePicker dateDebut;

    @FXML
    private DatePicker dateFin;

    @FXML
    private TextField locationField;

    @FXML
    private TextField organizerField;

    @FXML
    private TableView<Event> table;

    @FXML
    private TextField titleField;

    @FXML
    private TextField searchField;

    private EventService eventService;

    public BackEventController() {
        eventService = new EventService();
    }

    @FXML
    void DeleteEvent(ActionEvent event) {
        Event selectedEvent = table.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Delete Event");
            alert.setContentText("Are you sure you want to delete this event?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    eventService.delete(selectedEvent.getId());
                    table.getItems().remove(selectedEvent);
                    initialize(); // Refresh the page
                } catch (SQLException e) {
                    showErrorDialog("Error deleting event", e.getMessage());
                }
            }
        } else {
            showInformationDialog("No event selected", "Please select an event to delete.");
        }
    }

    @FXML
    void UpdateEvent(ActionEvent event) {
        Event selectedEvent = table.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            // Validate input
            String errorMessage = validateInput();
            if (errorMessage != null) {
                showErrorDialog("Input Error", errorMessage);
                return;
            }

            // Update event object
            updateEvent(selectedEvent);

            try {
                eventService.update(selectedEvent);
                showAlert("Success", "Event updated successfully.");
                initialize(); // Refresh the page
            } catch (SQLException e) {
                showErrorDialog("Error updating event", e.getMessage());
            }
        } else {
            showInformationDialog("No event selected", "Please select an event to update.");
        }
    }

    @FXML
    void addEvent(ActionEvent event) {
        // Validate input
        String errorMessage = validateInput();
        if (errorMessage != null) {
            showErrorDialog("Input Error", errorMessage);
            return;
        }
        // Create and add event
        try {
            Event newEvent = createEvent();
            eventService.add(newEvent);
            table.getItems().add(newEvent);
            showAlert("Success", "Event added successfully.");
            clearFields();
            initialize(); // Refresh the page
        } catch (SQLException e) {
            showErrorDialog("Error adding event", e.getMessage());
        }
    }

    private String validateInput() {
        String title = titleField.getText();
        String location = locationField.getText();
        String organizer = organizerField.getText();
        String capacityText = capacityField.getText();

        if (title.isEmpty() || title.length() < 5) {
            return "Please enter a title with at least 5 characters.";
        }
        if (location.isEmpty() || location.length() < 3 || !Character.isUpperCase(location.charAt(0))) {
            return "Please enter a location starting with an uppercase letter and with at least 3 characters.";
        }
        if (organizer.isEmpty() || organizer.length() < 3 || !Character.isUpperCase(organizer.charAt(0))) {
            return "Please enter an organizer starting with an uppercase letter and with at least 3 characters.";
        }
        if (dateDebut.getValue() == null) {
            return "Please select a start date.";
        }
        if (dateFin.getValue() == null) {
            return "Please select an end date.";
        }
        if (dateDebut.getValue().equals(dateFin.getValue())) {
            return "Start date and end date cannot be identical.";
        }
        if (dateDebut.getValue().isAfter(dateFin.getValue())) {
            return "Start date must be before end date.";
        }
        try {
            int capacity = Integer.parseInt(capacityText);
            if (capacity < 1 || capacity > 100) {
                return "Please enter a valid capacity between 1 and 100.";
            }
        } catch (NumberFormatException e) {
            return "Please enter a valid capacity (numeric value).";
        }
        // Image validation
        if (imageView.getImage() == null) {
            return "Please select an image.";
        }

        return null; // Input is valid
    }

    private Event createEvent() {
        String title = titleField.getText();
        LocalDateTime startDate = dateDebut.getValue().atStartOfDay();
        LocalDateTime endDate = dateFin.getValue().atStartOfDay();
        String location = locationField.getText();
        String organizer = organizerField.getText();
        int capacity = Integer.parseInt(capacityField.getText());


        return new Event(title, startDate, endDate, location, organizer, capacity, imagePath);
    }

    private void updateEvent(Event event) {
        event.setTitle(titleField.getText());
        event.setStartDate(dateDebut.getValue().atStartOfDay());
        event.setEndDate(dateFin.getValue().atStartOfDay());
        event.setLocation(locationField.getText());
        event.setOrganizer(organizerField.getText());
        event.setCapacity(Integer.parseInt(capacityField.getText()));

        // Update image only if a new image is selected
        if (imagePath != null) {
            event.setImage(imagePath);
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        titleField.clear();
        locationField.clear();
        organizerField.clear();
        capacityField.clear();
        dateDebut.setValue(null);
        dateFin.setValue(null);
        imageView.setImage(null);
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInformationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        try {
            List<Event> events = eventService.select();
            ObservableList<Event> eventList = FXCollections.observableArrayList(events);
            table.setItems(eventList);

            table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    titleField.setText(newSelection.getTitle());
                    locationField.setText(newSelection.getLocation());
                    organizerField.setText(newSelection.getOrganizer());
                    capacityField.setText(String.valueOf(newSelection.getCapacity()));
                    dateDebut.setValue(newSelection.getStartDate().toLocalDate());
                    dateFin.setValue(newSelection.getEndDate().toLocalDate());

                    // Image handling
                    String imagePath = newSelection.getImage(); // Get the image path from the event
                    if (imagePath != null) {
                        File file = new File(imagePath);
                        if (file.exists()) {
                            String imageURI = file.toURI().toString();
                            imageView.setImage(new Image(imageURI));
                        } else {
                            imageView.setImage(null); // If file does not exist, reset the image
                        }
                    } else {
                        imageView.setImage(null); // If image path is null, reset the image
                    }
                }
            });

            // Set cell value factories for table columns
            TableColumn<Event, String> titleColumn = new TableColumn<>("Titre");
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

            TableColumn<Event, LocalDateTime> dateDebutColumn = new TableColumn<>("Date De Debut");
            dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

            TableColumn<Event, LocalDateTime> dateFinColumn = new TableColumn<>("Date De Fin");
            dateFinColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

            TableColumn<Event, String> organizerColumn = new TableColumn<>("Organisateur");
            organizerColumn.setCellValueFactory(new PropertyValueFactory<>("organizer"));

            TableColumn<Event, String> locationColumn = new TableColumn<>("Localisation");
            locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

            TableColumn<Event, Integer> capacityColumn = new TableColumn<>("Capacité");
            capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));

            TableColumn<Event, String> imageColumn = new TableColumn<>("Image");
            imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));

            table.getColumns().setAll(titleColumn, dateDebutColumn, dateFinColumn, organizerColumn, locationColumn, capacityColumn,imageColumn);
        } catch (SQLException e) {
            showErrorDialog("Error", e.getMessage());
        }
    }

    private String imagePath; // Define a class-level variable to store the image path
    @FXML
    void handleUploadAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                String targetDir = "src/main/resources/img/";
                Path targetPath = Files.copy(file.toPath(), new File(targetDir + file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                imageView.setImage(new Image("file:" + targetDir + file.getName()));

                // Set the image path in the class-level variable
                imagePath = targetDir + file.getName();
            } catch (IOException e) {
                showAlert("Erreur", "Erreur lors de l'upload de l'image : " + e.getMessage());
            }
        }
    }



    private void openReservationScene(Event selectedEvent, List<Reservation> reservations) {
        try {
            // Load the reservation.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/Reservation.fxml"));
            Parent root = loader.load();

            // Get the controller associated with the reservation scene
            ReservationController reservationController = loader.getController();

            // Pass the selected event and its reservations to the reservation controller
            reservationController.initData(selectedEvent, reservations);

            // Create a new stage for the reservation scene
            Stage stage = new Stage();
            stage.setTitle("Reservations"); // Set the title of the stage
            stage.setScene(new Scene(root)); // Set the scene in the stage

            // Show the new stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the case where the reservation.fxml file cannot be loaded
            showAlert("Error", "Could not load Reservation scene: " + e.getMessage());
        }
    }


    @FXML
    void search() {
        String searchText = searchField.getText().toLowerCase();

        // If the search text is empty, reset the table to show all events
        if (searchText.isEmpty()) {
            initialize();
            return;
        }

        List<Event> filteredList = table.getItems().stream()
                .filter(event -> event.getTitle().toLowerCase().contains(searchText)
                        || event.getOrganizer().toLowerCase().contains(searchText)
                        || String.valueOf(event.getCapacity()).contains(searchText))
                .collect(Collectors.toList());
        table.setItems(FXCollections.observableArrayList(filteredList));
    }


    @FXML
    void clearSearch(ActionEvent event) {
        searchField.clear();
        initialize(); // Reset the table to show all events
    }
    @FXML
    void generatePDF(ActionEvent event) {
        Event selectedEvent = table.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            try {
                PDDocument document = new PDDocument();
                PDPage page = new PDPage();
                document.addPage(page);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                // Draw title "Event Details" at the top center
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
                contentStream.newLineAtOffset((page.getMediaBox().getWidth() - 120) / 2, page.getMediaBox().getHeight() - 50);
                contentStream.showText("Event Details");
                contentStream.endText();

                float startX = 50; // Starting X position for text
                float startY = page.getMediaBox().getHeight() - 150; // Starting Y position for text
                float imageX = page.getMediaBox().getWidth() - 250; // Starting X position for image
                float imageY = page.getMediaBox().getHeight() - 250; // Starting Y position for image
                float lineHeight = 20; // Height of each line

                // Draw other event details
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(startX, startY);

                contentStream.showText("Title: " + selectedEvent.getTitle());
                contentStream.newLineAtOffset(0, -lineHeight);

                contentStream.showText("Start Date: " + selectedEvent.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                contentStream.newLineAtOffset(0, -lineHeight);

                contentStream.showText("End Date: " + selectedEvent.getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                contentStream.newLineAtOffset(0, -lineHeight);

                contentStream.showText("Location: " + selectedEvent.getLocation());
                contentStream.newLineAtOffset(0, -lineHeight);

                contentStream.showText("Organizer: " + selectedEvent.getOrganizer());
                contentStream.newLineAtOffset(0, -lineHeight);

                contentStream.showText("Capacity: " + selectedEvent.getCapacity());
                contentStream.endText();

                // Add image on the right
                if (selectedEvent.getImage() != null && !selectedEvent.getImage().isEmpty()) {
                    PDImageXObject image = PDImageXObject.createFromFile(selectedEvent.getImage(), document);
                    float width = 200; // Adjust image width as needed
                    float height = 100; // Adjust image height as needed
                    contentStream.drawImage(image, imageX, imageY, width, height);
                }

                // Add logo image to the bottom right
                PDImageXObject logoImage = PDImageXObject.createFromFile("src/main/java/com/example/greenta/Controller/Events/Logo_ESPRIT_Ariana.jpg", document);
                float logoWidth = (float) logoImage.getWidth() / 6;
                float logoHeight = (float) logoImage.getHeight() / 6;
                float logoX = page.getMediaBox().getWidth() - logoWidth - 50; // Adjust horizontal position
                float logoY = 50; // Adjust vertical position
                contentStream.drawImage(logoImage, logoX, logoY, logoWidth, logoHeight);

                contentStream.close();
                // Saving the document
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
                File file = fileChooser.showSaveDialog(null);

                if (file != null) {
                    document.save(file);
                    document.close();
                    showAlert("PDF Generated", "PDF document generated successfully.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                showErrorDialog("Error", "An error occurred while generating the PDF document: " + e.getMessage());
            }
        } else {
            showInformationDialog("No Event Selected", "Please select an event to generate a PDF document.");
        }
    }
    @FXML
    void handleViewStatistics(ActionEvent event) {
        try {
            // Load the Statistics.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("//Statistics.fxml"));
            Parent root = loader.load();

            // Create a new stage for the statistics view
            Stage stage = new Stage();
            stage.setTitle("Event Statistics");
            stage.setScene(new Scene(root));

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any errors while loading the statistics view
        }
    }


}


