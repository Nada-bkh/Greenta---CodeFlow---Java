package Controllers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Entities.Charity;
import Entities.Donation;
import Services.CharityService;
import Services.DonationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import Entities.Charity;
import Entities.Donation;
import Services.DonationService;
import Services.CharityService;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;
public class ListCharityController  implements Initializable {



        CharityService cs = new CharityService();
        @FXML
        private Button Donate;
        @FXML
        private Button AfficheCharity;
        @FXML
        private ListView<Charity> list_view_affiche;
        @FXML
        private Button retour;


        @FXML
        private HBox carLayout;

        private List<Charity> Recently_Added;
        @FXML
        private TextField tx_Recherche;





    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // Order the charities by donation count
            List<Charity> orderedCharities = new CharityService().orderCharitiesByDonationCount();

            // Display charities as cards in ListView
            displayCharitiesAsCardsInListView(orderedCharities);

            // Display charities as cards in HBox
            displayCharitiesAsCardsInHBox(orderedCharities);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayCharitiesAsCardsInListView(List<Charity> charities) {
        ObservableList<Charity> observableCharities = FXCollections.observableArrayList(charities);
        list_view_affiche.setItems(observableCharities);
        list_view_affiche.setCellFactory(param -> new ListCell<Charity>() {
            @Override
            protected void updateItem(Charity charity, boolean empty) {
                super.updateItem(charity, empty);
                if (empty || charity == null) {
                    setGraphic(null);
                } else {
                    setGraphic(createCard(charity));
                }
            }
        });
    }

    private void displayCharitiesAsCardsInHBox(List<Charity> charities) {
        carLayout.getChildren().clear();
        for (Charity charity : charities) {
            carLayout.getChildren().add(createCard(charity));
        }
    }

    private Node createCard(Charity charity) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Card.fxml"));
            HBox cardBox = fxmlLoader.load();
            CardController cardController = fxmlLoader.getController();
            cardController.setData(charity);
            return cardBox;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }





    @FXML
    private void GoToAddDonation(ActionEvent event) {
        Charity selectedCharity = list_view_affiche.getSelectionModel().getSelectedItem();

        if (selectedCharity == null) {
            showAlert("No Charity Selected", "Please select a charity to make a donation.");
        } else {
            try {
                // Load the AddDonation.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddDonation.fxml"));
                Parent root = loader.load();

                // Pass the selected charity to the controller of the AddDonation.fxml form
                AddDonationController donationController = loader.getController();
                donationController.setSelectedCharity(selectedCharity);

                // Create a new stage for the AddDonation.fxml form
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.setTitle("Add Donation");
                stage.showAndWait(); // Wait for the AddDonation.fxml form to close before continuing
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }





    @FXML
        private void GoToHome(ActionEvent event) {
        }

    @FXML
    private void ActualiseCharity(ActionEvent event) {
        List<Charity> charities = cs.showCharity();

        // Create an ObservableList to store the charities
        ObservableList<Charity> charityList = FXCollections.observableArrayList(charities);

        // Set the items of the ListView
        list_view_affiche.setItems(charityList);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



    @FXML
    private void Recherche(ActionEvent event) {
        String name = tx_Recherche.getText();
        if (!name.isEmpty()) {
            try {
                Charity charity = cs.showCharityByName(name);

                if (charity != null) {
                    ObservableList<Charity> charityList = FXCollections.observableArrayList();
                    charityList.add(charity);

                    list_view_affiche.setItems(charityList);
                    list_view_affiche.getSelectionModel().select(charity);
                } else {
                    showAlert("Charity Not Found", "Charity with name " + name + " does not exist.");
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid Name", "Please enter a valid name.");
            }
        } else {
            showAlert("Empty Name", "Please enter a name to search for a charity.");
        }
    }







}

