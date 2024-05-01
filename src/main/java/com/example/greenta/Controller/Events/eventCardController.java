package com.example.greenta.Controller.Events;
import com.example.greenta.Models.Event;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;


import javafx.scene.input.MouseEvent;
import java.util.Objects;

public class eventCardController {
    @FXML
    private HBox box;

    @FXML
    private Label eventCapacity;

    @FXML
    private ImageView eventImage;

    @FXML
    private Label eventLocation;

    @FXML
    private Label eventTitle;
    private String[] colors = {"#f8d7da", "#d4edda", "#cce5ff", "#fff3cd", "#d1ecf1", "#f8d7da", "#f5c6cb", "#d6d8d9"};
    @FXML
    public void handleMouseEntered(MouseEvent event) {
        box.setCursor(Cursor.HAND); // Change cursor to hand
    }

    @FXML
    public void handleMouseExited(MouseEvent event) {
        box.setCursor(Cursor.DEFAULT); // Change cursor to default
    }
    public void setData(Event event) {
        System.out.println("Jawna behy");
        eventTitle.setText(event.getTitle());
        eventLocation.setText(event.getLocation());
        eventCapacity.setText(String.valueOf(event.getCapacity()));
        //this line kasarly zeby
        Image image = new Image(event.getImage());
        eventImage.setImage(image);
        box.setStyle("-fx-background-color: " + colors[(int) (Math.random() * colors.length)]);

    }

}
