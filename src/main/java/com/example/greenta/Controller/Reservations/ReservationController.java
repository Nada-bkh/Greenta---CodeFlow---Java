package com.example.greenta.Controller.Reservations;

import com.example.greenta.Models.Event;
import com.example.greenta.Models.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.greenta.Services.EventService;
import com.example.greenta.Services.ReservationService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationController {

    @FXML
    private ChoiceBox<String> eventChoiceBox;

    @FXML
    private TableColumn<Reservation, Integer> clientID;

    @FXML
    private TableColumn<Reservation, Integer> eventID;

    @FXML
    private TableColumn<Reservation, LocalDateTime> reservationDATE;

    @FXML
    private TableView<Reservation> reservationTable;

    private ReservationService reservationService = new ReservationService();
    private EventService eventService = new EventService();

    @FXML
    void initialize() {
        try {
            // Populate the choice box with event names
            List<String> eventNames = eventService.getAllEventNames();
            eventChoiceBox.getItems().addAll(eventNames);

            // Set cell value factories for each column
            clientID.setCellValueFactory(new PropertyValueFactory<>("userId"));
            eventID.setCellValueFactory(new PropertyValueFactory<>("eventId"));
            reservationDATE.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));

            // Add event listener to the choice box
            eventChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    try {
                        // Retrieve reservations for the selected event
                        List<Reservation> reservations = reservationService.getReservationsByEventName(newValue);
                        reservationTable.getItems().setAll(reservations);
                    } catch (SQLException e) {
                        System.err.println("Error retrieving reservations: " + e.getMessage());
                    }
                }
            });

        } catch (SQLException e) {
            System.err.println("Error initializing reservations: " + e.getMessage());
        }
    }

    public void initData(Event selectedEvent, List<Reservation> reservations) {
        try {
            // Populate the TableView with reservations for the selected event
            ObservableList<Reservation> reservationList = FXCollections.observableArrayList(reservations);
            reservationTable.setItems(reservationList);
        } catch (Exception e) {
            System.err.println("Error initializing reservations data: " + e.getMessage());
        }
    }
}

