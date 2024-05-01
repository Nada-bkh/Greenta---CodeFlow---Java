package com.example.greenta.Controller.Events;

import com.example.greenta.Models.Event;
import com.example.greenta.Services.EventService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FrontEventController implements Initializable {


    @FXML
    private HBox cardLayout;

    //@FXML
    //private TableColumn<Event, String> titleField;

    private final EventService eventService;

    public FrontEventController() {
        eventService = new EventService();
    }

    private List<Event> recentlyAdded;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)  {
        recentlyAdded = new ArrayList<>(recentlAdded());
        try {
            for (int i = 0; i<recentlyAdded.size(); i++) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/Front/eventCard.fxml"));
                HBox eventCard = loader.load();
                eventCardController controller = loader.getController();
                controller.setData(recentlyAdded.get(i));
                cardLayout.getChildren().add(eventCard);
                System.out.println("Event added");
                //events are being added but the scene is not loaded , I can't see anything
            }
        } catch (IOException e) {
            showErrorDialog("Error", e.getMessage());
        }
    }
    //q:
    private void showErrorDialog(String error, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(error);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public List<Event> recentlAdded(){
        List<Event> events = eventService.getEvents();
        //only keep event that are 7 days away or less
        events.removeIf(event -> event.getStartDate().isAfter(LocalDateTime.now().plusDays(7)));
        events.removeIf(event -> event.getStartDate().isBefore(LocalDateTime.now()));
        System.out.println(LocalDateTime.now().plusDays(7));

        return events;

   }


}
