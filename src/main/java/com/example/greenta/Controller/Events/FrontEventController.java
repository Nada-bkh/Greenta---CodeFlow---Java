package com.example.greenta.Controller.Events;

import com.example.greenta.Models.Event;
import com.example.greenta.Services.EventService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class FrontEventController {

    @FXML
    private TableView<Event> table;

    @FXML
    private TextField capacityField = new TextField();





    @FXML
    private DatePicker dateDebut = new DatePicker();

    @FXML
    private DatePicker dateFin = new DatePicker();

    @FXML
    private TextField locationField = new TextField();

    @FXML
    private TextField organizerField = new TextField();

    @FXML
    private TextField titleField = new TextField();

    //@FXML
    //private TableColumn<Event, String> titleField;

    private final EventService eventService;

    public FrontEventController() {
        eventService = new EventService();
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

            table.getColumns().setAll(titleColumn, dateDebutColumn, dateFinColumn, organizerColumn, locationColumn, capacityColumn, imageColumn);
        } catch (SQLException e) {
            showErrorDialog("Error", e.getMessage());
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
