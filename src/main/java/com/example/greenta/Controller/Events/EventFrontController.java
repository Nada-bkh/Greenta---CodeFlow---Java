package com.example.greenta.Controller.Events;

import com.example.greenta.Models.Event;
import com.example.greenta.Models.Reservation;
import com.example.greenta.Models.User;
import com.example.greenta.Services.EventService;
import com.example.greenta.Services.ReservationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventFrontController {

    @FXML
    private TilePane eventTilePane;

    private EventService eventService = new EventService();
    @FXML
    private TextField searchField;

    @FXML
    private HBox searchBox;

    @FXML
    public void initialize() {
        try {
            List<Event> eventList = eventService.select();

            for (Event event : eventList) {
                AnchorPane card = createEventCard(event);
                eventTilePane.getChildren().add(card);
            }
        } catch (Exception e) {
            System.err.println("Error loading events: " + e.getMessage());
        }
    }

    private AnchorPane createEventCard(Event event) {
        AnchorPane card = new AnchorPane();
        card.setPrefSize(250, 350); // Increase the size of the card
        // Set event data as user data of the card
        card.setUserData(event);
        // Adding border and background color to the card
        card.setStyle("-fx-background-color: #f0f0f0; " +
                "-fx-border-color: #CCCCCC; " +
                "-fx-border-width: 1px; " +
                "-fx-border-radius: 5px; " +
                "-fx-padding: 10px;");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(160);
        imageView.setFitHeight(120); // Adjusted image size
        imageView.setLayoutX(10);
        imageView.setLayoutY(10);

        try {
            File file = new File(event.getImage());
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
            } else {
                System.err.println("Image file not found: " + event.getImage());
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
            e.printStackTrace();
        }

        Label titleLabel = new Label("Title: " + event.getTitle());
        titleLabel.setLayoutX(10);
        titleLabel.setLayoutY(140);
        titleLabel.setStyle("-fx-font-weight: bold;"); // Making title bold

        Label locationLabel = new Label("Location: " + event.getLocation());
        locationLabel.setLayoutX(10);
        locationLabel.setLayoutY(160);

        Label organizerLabel = new Label("Organizer: " + event.getOrganizer());
        organizerLabel.setLayoutX(10);
        organizerLabel.setLayoutY(180);

        Label capacityLabel = new Label("Capacity: " + event.getCapacity());
        capacityLabel.setLayoutX(10);
        capacityLabel.setLayoutY(200);

        Label startDateLabel = new Label("Start Date: " + event.getStartDate());
        startDateLabel.setLayoutX(10);
        startDateLabel.setLayoutY(220);

        Label endDateLabel = new Label("End Date: " + event.getEndDate());
        endDateLabel.setLayoutX(10);
        endDateLabel.setLayoutY(240);

        // Reservation Button
        Button reserveButton = new Button("Réserver");
        reserveButton.setLayoutX(10);
        reserveButton.setLayoutY(260);
        reserveButton.setOnAction(this::handleReservation); // Add action handler

        // Applying some padding to the labels
        titleLabel.setPadding(new Insets(0, 0, 5, 0));
        locationLabel.setPadding(new Insets(0, 0, 2, 0));
        organizerLabel.setPadding(new Insets(0, 0, 2, 0));
        capacityLabel.setPadding(new Insets(0, 0, 2, 0));
        startDateLabel.setPadding(new Insets(0, 0, 2, 0));
        endDateLabel.setPadding(new Insets(0, 0, 2, 0));

        card.getChildren().addAll(imageView, titleLabel, locationLabel, organizerLabel, capacityLabel, startDateLabel, endDateLabel, reserveButton);
        return card;
    }

    @FXML
    private void handleReservation(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (source instanceof Button) {
            Button reserveButton = (Button) source;
            AnchorPane eventCard = (AnchorPane) reserveButton.getParent();
            Event event = (Event) eventCard.getUserData(); // Get the event associated with the card

            // Assuming a default user for now
            User user = new User(1, "Default", "User", "default@example.com", "", "", "");

            // Create a reservation
            Reservation reservation = new Reservation();
            reservation.setEvent(event);
            reservation.setUser(user);
            reservation.setReservationDate(LocalDateTime.now());

            try {
                // Create an instance of ReservationService
                ReservationService reservationService = new ReservationService();
                // Add reservation to the database using the instance
                reservationService.add(reservation);
                System.out.println("Reservation added successfully!");
            } catch (SQLException e) {
                System.err.println("Error adding reservation: " + e.getMessage());
            }
        }
    }

}

