package com.example.greenta.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class FrontHomeController {

    @FXML
    private ImageView BOImage;

    @FXML
    private Label charityLabel;

    @FXML
    private Button learnMoreButton;

    @FXML
    void backOffice(MouseEvent event) {

    }

    @FXML
    void charityButton(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListCharity.fxml"));
            if (root == null) {
                System.err.println("FXML file not found.");
                return;
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML file: " + e.getMessage());
            e.printStackTrace();
        }
    }



    @FXML
    void coursesButton(MouseEvent event) {

    }

    @FXML
    void eventButton(MouseEvent event) {

    }

    @FXML
    void homeButton(MouseEvent event) {

    }

    @FXML
    void learnMore(ActionEvent event) {

    }

    @FXML
    void profileClicked(ActionEvent event) {

    }

    @FXML
    void recruitmentButton(MouseEvent event) {

    }

    @FXML
    void shopButton(MouseEvent event) {

    }

    @FXML
    void signOut(MouseEvent event) {

    }

}
