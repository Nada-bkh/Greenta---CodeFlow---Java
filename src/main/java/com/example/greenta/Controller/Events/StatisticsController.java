package com.example.greenta.Controller;

import com.example.greenta.Models.Event;
import com.example.greenta.Models.Reservation;
import com.example.greenta.Services.EventService;
import com.example.greenta.Services.ReservationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

import java.sql.SQLException;
import java.util.List;

public class StatisticsController {

    @FXML
    private PieChart pieChart;

    private EventService eventService;
    private ReservationService reservationService;

    public StatisticsController() {
        eventService = new EventService();
        reservationService = new ReservationService();
    }

    @FXML
    public void initialize() {
        // Fetch events and their reservations
        try {
            List<Event> events = eventService.select();
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            for (Event event : events) {
                int reservationsCount = reservationService.getReservationsCountByEventId(event.getId());
                pieChartData.add(new PieChart.Data(event.getTitle(), reservationsCount));
            }

            pieChart.setData(pieChartData);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle error
        }
    }
}
