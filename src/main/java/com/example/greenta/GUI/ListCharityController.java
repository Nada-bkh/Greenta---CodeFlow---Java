package com.example.greenta.GUI;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.example.greenta.Exceptions.UserNotFoundException;
import com.example.greenta.Models.Charity;
import com.example.greenta.Models.Donation;
import com.example.greenta.Models.User;
import com.example.greenta.Services.CharityService;
import com.example.greenta.Services.DonationService;
import com.example.greenta.Services.SessionService;
import com.example.greenta.Services.UserService;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class ListCharityController {


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
    private Button profileLabel;
    @FXML
    private Label backOfficeButton;

    @FXML
    private Label charityLabel;

    @FXML
    private Label donate;

    @FXML
    private Label coursesLabel;

    @FXML
    private Label eventLabel;

    @FXML
    private Label homeLabel;

    @FXML
    private Label recruitmentLabel;

    @FXML
    private Label shopLabel;

    @FXML
    private HBox carLayout;


    private List<Charity> Recently_Added;
    @FXML
    private TextField tx_Recherche;

    private final UserService userService = UserService.getInstance();
    private SessionService sessionService = SessionService.getInstance();
    private User currentUser;

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
    @FXML
    public void initialize(int userId) throws UserNotFoundException {
        currentUser = userService.getUserbyID(userId);
        profileLabel.setText(currentUser.getFirstname());
        // Initialize the ListView with custom cell factory
        list_view_affiche.setCellFactory(param -> new ListCell<Charity>() {
            @Override
            protected void updateItem(Charity charity, boolean empty) {
                super.updateItem(charity, empty);
                if (empty || charity == null) {
                    setText(null);
                } else {
                    // Customize how each charity is displayed here
                    String charityInfo = String.format("Name: %s\nLocation: %s\nAmount Donated: %.2f",
                            charity.getName_of_charity(), charity.getLocation(), charity.getAmount_donated());
                    setText(charityInfo);
                }
            }
        });

        // Load the cards dynamically
        loadCards();
    }

    private void loadCards() {
        Recently_Added = new CharityService().orderCharitiesByDonationCount();
        try {
            for (int i = 0; i < Recently_Added.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/Card.fxml"));
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
    private void GoToAddDonation(ActionEvent event) throws UserNotFoundException {
        User user = userService.getUserbyEmail(currentUser.getEmail());
        Charity selectedCharity = list_view_affiche.getSelectionModel().getSelectedItem();

        if (selectedCharity == null) {
            showAlert("No Charity Selected", "Please select a charity to make a donation.");
        } else {
            try {
                // Load the AddDonation.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/AddDonation.fxml"));
                Parent root = loader.load();

                // Pass the selected charity to the controller of the AddDonation.fxml form
                AddDonationController donationController = loader.getController();
                donationController.setSelectedCharity(selectedCharity);
                donationController.initialize(user.getId());

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
        String location = tx_Recherche.getText();
        if (!name.isEmpty() ) {
            try {
                Charity charity = cs.showCharityByName(name);


                if (charity != null) {
                    ObservableList<Charity> charityList = FXCollections.observableArrayList();
                    charityList.add(charity);

                    list_view_affiche.setItems(charityList);
                    list_view_affiche.getSelectionModel().select(charity);
                }


                else {

                    Charity charity1 = cs.showCharityByLocation(location);

                    if (charity1 != null) {
                        ObservableList<Charity> charityList = FXCollections.observableArrayList();
                        charityList.add(charity1);

                        list_view_affiche.setItems(charityList);
                        list_view_affiche.getSelectionModel().select(charity1);
                    }

                    else {
                        showAlert("Charity Not Found", "Charity not found");
                    }
                } }
            catch (NumberFormatException e) {
                showAlert("Invalid location", "Please enter a valid  location.");
            }
        }
        else {
            showAlert("Empty Name", "Please enter a name or location to search for a charity.");
        }

    }

    @FXML
    void charityButton(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/ShowCharity.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 800, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void coursesButton(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/gestion-quiz-admin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void eventButton(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/AjouterEvent.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 800, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void homeButton(MouseEvent event) throws  UserNotFoundException{
        User user = userService.getUserbyEmail(currentUser.getEmail());
        try {
            sessionService.setCurrentUser(user);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/FrontHome.fxml"));
            Parent root = loader.load();
            FrontHomeController frontHomeController = loader.getController();
            frontHomeController.initialize(user.getId());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void profileClicked(ActionEvent event) throws UserNotFoundException {
        User user = userService.getUserbyEmail(currentUser.getEmail());
        try {
            sessionService.setCurrentUser(user);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/Profile.fxml"));
            Parent root = loader.load();
            ProfileController profileController = loader.getController();
            profileController.initializeProfile(user.getId());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void recruitmentButton(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/App.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 800, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void shopButton(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/ProductCategory.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 800, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void signOut(MouseEvent event) {
        sessionService.logout();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/User.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void backOffice(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/BackOffice.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void donation(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/AddDonation.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 800, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

