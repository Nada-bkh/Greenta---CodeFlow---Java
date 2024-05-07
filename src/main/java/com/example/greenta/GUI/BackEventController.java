package com.example.greenta.GUI;

import com.example.greenta.Models.Event;
import com.example.greenta.Services.EventService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BackEventController {

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    @FXML
    private Button btnImage;

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
            return "Veuillez entrer un titre avec au moins 5 caractères.";
        }
        if (location.isEmpty() || location.length() < 3 || !Character.isUpperCase(location.charAt(0))) {
            return "Veuillez entrer une localisation commençant par une lettre majuscule et avec au moins 3 caractères.";
        }
        if (organizer.isEmpty() || organizer.length() < 3 || !Character.isUpperCase(organizer.charAt(0))) {
            return "Veuillez entrer un organisateur commençant par une lettre majuscule et avec au moins 3 caractères.";
        }
        if (dateDebut.getValue() == null) {
            return "Veuillez sélectionner une date de début.";
        }
        if (dateFin.getValue() == null) {
            return "Veuillez sélectionner une date de fin.";
        }
        if (dateDebut.getValue().equals(dateFin.getValue())) {
            return "La date de début et la date de fin ne peuvent pas être identiques.";
        }
        if (dateDebut.getValue().isAfter(dateFin.getValue())) {
            return "La date de début doit être avant la date de fin.";
        }
        try {
            int capacity = Integer.parseInt(capacityText);
            if (capacity < 1 || capacity > 100) {
                return "Veuillez entrer une capacité valide entre 1 et 100.";
            }
        } catch (NumberFormatException e) {
            return "Veuillez entrer une capacité valide (valeur numérique).";
        }
        // Vérification de l'image
//        if (imageView.getImage() == null) {
//            return "Veuillez sélectionner une image.";
//        }

        return null; // L'entrée est valide
    }


    private Event createEvent() {
        String title = titleField.getText();
        LocalDateTime startDate = dateDebut.getValue().atStartOfDay();
        LocalDateTime endDate = dateFin.getValue().atStartOfDay();
        String location = locationField.getText();
        String organizer = organizerField.getText();
        int capacity = Integer.parseInt(capacityField.getText());
        String image = String.valueOf(imageView.getImage());
        return new Event(title, startDate, endDate, location, organizer, capacity,image);
    }

    private void updateEvent(Event event) {
        event.setTitle(titleField.getText());
        event.setStartDate(dateDebut.getValue().atStartOfDay());
        event.setEndDate(dateFin.getValue().atStartOfDay());
        event.setLocation(locationField.getText());
        event.setOrganizer(organizerField.getText());
        event.setCapacity(Integer.parseInt(capacityField.getText()));
        event.setImage(String.valueOf(imageView.getImage()));
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

                    // Gestion de l'image
                    String imagePath = newSelection.getImage(); // Obtenez le chemin de l'image depuis l'événement
                    if (imagePath != null) {
                        File file = new File(imagePath);
                        if (file.exists()) {
                            imageView.setImage(new Image("file:" + imagePath));
                        } else {
                            imageView.setImage(null); // Si le fichier n'existe pas, réinitialisez l'image
                        }
                    } else {
                        imageView.setImage(null); // Si le chemin de l'image est null, réinitialisez l'image
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

            TableColumn<Event, String> organizerColumn = new TableColumn<>("Organizateur");
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


    @FXML
    void handleUploadAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", ".png", ".jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                String targetDir = "src/main/java/com/example/greenta/img";
                Path targetPath = Files.copy(file.toPath(), new File(targetDir + file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                imageView.setImage(new Image("file:" + targetDir + file.getName()));
            } catch (IOException e) {
                showAlert("Erreur", "Erreur lors de l'upload de l'image : " + e.getMessage());
            }
        }
    }
}
