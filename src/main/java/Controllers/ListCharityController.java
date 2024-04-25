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


        /**
         * Initializes the controller class.
         */
        /*@Override
        public void initialize(URL url, ResourceBundle rb) {
            // TODO


            Recently_Added = new CharityService().orderCharitiesByDonationCount();
            try{
                for(int i=0;i<Recently_Added.size();i++)
                {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/Card.fxml"));
                    HBox cardBox = fxmlLoader.load();
                    CardController cardController = fxmlLoader.getController();
                    cardController.setData(Recently_Added.get(i));
                    carLayout.getChildren().add(cardBox);

                }
            }catch (IOException e){
                e.printStackTrace();
            }


        }
*/
        @Override
        public void initialize(URL url, ResourceBundle rb) {
            Recently_Added = new CharityService().orderCharitiesByDonationCount();
            try {
                for (int i = 0; i < Recently_Added.size(); i++) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Card.fxml"));
                    HBox cardBox = fxmlLoader.load();
                    CardController cardController = fxmlLoader.getController();
                    cardController.setData(Recently_Added.get(i));
                    carLayout.getChildren().add(cardBox);
                }
            } catch (IOException e) {
                e.printStackTrace();
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

