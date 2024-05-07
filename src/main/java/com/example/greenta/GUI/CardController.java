package com.example.greenta.GUI;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import com.example.greenta.Models.Charity;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class CardController implements Initializable {

    @FXML
    private HBox Hbox;

    @FXML
    private Label amountId;

    @FXML
    private Label dateId;

    @FXML
    private ImageView imgid;

    @FXML
    private Label locationId;

    @FXML
    private Label nameId;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setData(Charity o) {
        // Load the image from the file path
        Image image = new Image(new File(o.getPicture()).toURI().toString());
        imgid.setImage(image);

        // Set text values
        nameId.setText(o.getName_of_charity());
        locationId.setText(o.getLocation());

        // Set random green color for the card background
        double hue = Math.random() * 120; // Green hue range
        String colorStyle = String.format("-fx-background-color: hsb(%f, 60%%, 90%%); -fx-background-radius: 15;", hue);
        Hbox.setStyle(colorStyle);
    }
}

