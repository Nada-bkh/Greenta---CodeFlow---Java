
package com.example.greenta.GUI;

import com.example.greenta.Exceptions.UserNotFoundException;
import com.example.greenta.Models.Donation;
import com.example.greenta.Models.User;
import com.example.greenta.Services.DonationService;
import com.example.greenta.Services.SessionService;
import com.example.greenta.Services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class BackController {

    @FXML
    private Label charityLabel;
    @FXML
    private Button profileLabel;
    @FXML
    private ListView<Donation> topDonorsListView;
    private final UserService userService = UserService.getInstance();
    private final SessionService sessionService = SessionService.getInstance();
    private User currentUser;

//    @FXML
//    void initialize() {
//        loadShowCharityFXML();
//        displayTopDonors();
//    }
    @FXML
    void initialize(int userId) throws UserNotFoundException {
        //User user = userService.getUserbyEmail(currentUser.getEmail());
        currentUser = userService.getUserbyID(userId);
        //profileLabel.setText(currentUser.getFirstname());
        loadShowCharityFXML();
        displayTopDonors();
    }
    @FXML
    void charityButton(MouseEvent event) throws UserNotFoundException {
       // User user = userService.getUserbyEmail(currentUser.getEmail());
        loadShowCharityFXML();
        displayTopDonors();
    }

    private void loadShowCharityFXML() throws UserNotFoundException {
        User user = userService.getUserbyEmail(currentUser.getEmail());
        try {
            // Load the FXML file for showing charities
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/ShowCharity.fxml"));
            Parent root = loader.load();
            ShowCharityController showCharityController = loader.getController();
            showCharityController.initialize(user.getId());

            Stage stage = new Stage();
            stage.setTitle("Show Charity");

            // Set the scene
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayTopDonors() {
        DonationService donationService = new DonationService();
        List<Donation> topDonors = donationService.getTopDonors();

        topDonorsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Donation donation, boolean empty) {
                super.updateItem(donation, empty);
                if (empty || donation == null) {
                    setText(null);
                } else {
                    // Check if the charity associated with the donation is not null
                    if (donation.getCharity() != null) {
                        // Customize how each donation is displayed here
                        String donationInfo = String.format("Name: %s %s\nPhone: %d\nAmount: %.2f\nDate: %s\nCharity: %s",
                                donation.getFirst_name(), donation.getLast_name(), donation.getPhone_number(),
                                donation.getAmount(), donation.getDate(), donation.getCharity().getName_of_charity());
                        setText(donationInfo);
                    } else {
                        // Handle the case where the charity is null
                        setText("Name: " + donation.getFirst_name() + " " + donation.getLast_name() + "\nPhone: " + donation.getPhone_number() + "\nAmount: " + donation.getAmount() + "\nDate: " + donation.getDate() + "\nCharity: N/A");
                    }
                }
            }
        });

        ObservableList<Donation> topDonorsObservableList = FXCollections.observableArrayList(topDonors);
        topDonorsListView.setItems(topDonorsObservableList);
    }


    public void homeButton(MouseEvent mouseEvent) {
    }

    public void shopButton(MouseEvent mouseEvent) {
    }

    public void eventButton(MouseEvent mouseEvent) {
    }

    public void coursesButton(MouseEvent mouseEvent) {
    }

    public void recruitmentButton(MouseEvent mouseEvent) {
    }

    public void backOffice(MouseEvent mouseEvent) {
    }

    public void signOut(MouseEvent mouseEvent) {
    }
}
