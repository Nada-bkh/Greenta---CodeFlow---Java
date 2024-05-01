package com.example.greenta.Controller.Reservations;

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
import javafx.scene.control.TableColumn;

public class ReservationController {

    @FXML
    private TableColumn<Integer, Integer> clientID;

    @FXML
    private TableColumn<Integer, Integer> eventID;

    @FXML
    private TableColumn<LocalDateTime, LocalDateTime> reservationDATE;

    @FXML
    public void initialize() {
        // Set cell value factories for each column
        clientID.setCellValueFactory(new PropertyValueFactory<>("clientID"));
        eventID.setCellValueFactory(new PropertyValueFactory<>("eventID"));
        reservationDATE.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
    }
}


