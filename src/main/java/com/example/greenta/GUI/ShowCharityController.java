 package com.example.greenta.GUI;


import com.example.greenta.Exceptions.UserNotFoundException;
import com.example.greenta.Models.Charity;
import com.example.greenta.Models.User;
import com.example.greenta.Services.CharityService;
import com.example.greenta.Services.SessionService;
import com.example.greenta.Services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;



 public class ShowCharityController {

     @FXML
     private ListView<Charity> charityListView;

     @FXML
     private Button profileLabel;

     private CharityService charityService = new CharityService();
     private final UserService userService = UserService.getInstance();
     private final SessionService sessionService = SessionService.getInstance();
     private User currentUser;
     @FXML
     public void initialize(int userId) throws UserNotFoundException {
         currentUser = userService.getUserbyID(userId);
         profileLabel.setText(currentUser.getFirstname());
         try {
             // Fetch charities from the database
             List<Charity> charities = charityService.showCharity();

             // Convert the list to an observable list
             ObservableList<Charity> observableCharities = FXCollections.observableArrayList(charities);

             // Set the observable list to the ListView
             charityListView.setItems(observableCharities);

             // Set custom cell factory to customize how each charity is displayed
             charityListView.setCellFactory(param -> new ListCell<Charity>() {
                 @Override
                 protected void updateItem(Charity charity, boolean empty) {
                     super.updateItem(charity, empty);

                     if (empty || charity == null) {
                         setText(null);
                     } else {
                         // Customize how the charity is displayed here
                         String displayText = "Name: " + charity.getName_of_charity() + "\n" +
                                 "Location: " + charity.getLocation() + "\n" +
                                 "Amount Donated: " + charity.getAmount_donated() + "\n" +
                                 "Last Date: " + charity.getLast_date();
                         setText(displayText);
                     }
                 }
             });
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

     @FXML
     void DeleteBtn(ActionEvent event) {
         // Get the selected charity from the ListView
         Charity selectedCharity = charityListView.getSelectionModel().getSelectedItem();

         if (selectedCharity != null) {
             // Delete the charity from the database
             boolean deleted = charityService.deleteCharity(selectedCharity.getId());

             if (deleted) {
                 // If deletion was successful, remove the charity from the ListView
                 charityListView.getItems().remove(selectedCharity);
             } else {
                 // Handle deletion failure
                 System.out.println("Failed to delete charity.");
             }
         } else {
             // Handle no selection error
             System.out.println("No charity selected for deletion.");
         }
     }

     private Stage primaryStage;

     public void setPrimaryStage(Stage primaryStage) {
         this.primaryStage = primaryStage;
     }
     @FXML
     void ModifyBtn(ActionEvent event) throws UserNotFoundException {
         User user = userService.getUserbyEmail(currentUser.getEmail());
         // Check if a charity is selected
         Charity selectedCharity = charityListView.getSelectionModel().getSelectedItem();
         if (selectedCharity != null) {
             try {
                 // Load the ModifyCharity.fxml file
                 FXMLLoader loader = new FXMLLoader(getClass().getResource("com/example/greenta/ModifyCharity.fxml"));
                 Parent root = loader.load();

                 // Create a new stage for the ModifyCharity popup
                 Stage modifyStage = new Stage();
                 modifyStage.setTitle("Modify Charity");
                 //modifyStage.initModality(Modality.WINDOW_MODAL);
                 modifyStage.initOwner(modifyStage);

                 // Set the controller for the ModifyCharity popup
                 ModifyCharityController controller = loader.getController();
                 controller.initialize(user.getId());
                 // Pass the reference to the ShowCharityController
                 controller.setShowCharityController(this);
                 // Pass the selected charity to the ModifyCharityController
                 controller.initData(selectedCharity);

                 // Show the ModifyCharity popup
                 Scene scene = new Scene(root);
                 modifyStage.setScene(scene);
                 modifyStage.show();

             } catch (IOException e) {
                 e.printStackTrace();
             }
         } else {
             // Display an error message if no charity is selected
             // You can customize this message according to your needs
             System.out.println("Please select a charity to modify.");
         }
     }

     public void updateCharity(Charity updatedCharity) {
         ObservableList<Charity> charities = charityListView.getItems();
         for (int i = 0; i < charities.size(); i++) {
             Charity charity = charities.get(i);
             if (charity.getId() == updatedCharity.getId()) {
                 // Update the properties of the charity
                 charity.setName_of_charity(updatedCharity.getName_of_charity());
                 charity.setAmount_donated(updatedCharity.getAmount_donated());
                 charity.setLocation(updatedCharity.getLocation());
                 charity.setLast_date(updatedCharity.getLast_date());

                 // Refresh the ListView to reflect the changes
                 charityListView.refresh();
                 break; // Exit the loop since we found the charity
             }
         }
     }
     /* @FXML
      void list(ActionEvent event) throws IOException {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListCharity.fxml"));
          Parent root = loader.load();

      }*/
     @FXML
     void list(ActionEvent event) throws IOException {
         Parent AjouterInventaireParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ListCharity.fxml")));
         Scene AjouterInventaireScene = new Scene(AjouterInventaireParent);

         // This line gets the Stage information
         Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

         window.setScene(AjouterInventaireScene);
         window.show();
     }
     @FXML
     void charityButton(MouseEvent event) throws UserNotFoundException {
         User user = userService.getUserbyEmail(currentUser.getEmail());
         try {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/Back.fxml"));
             Parent root = loader.load();
             BackController backController = loader.getController();
             backController.initialize(user.getId());
             Scene scene = new Scene(root, 800, 600);
             Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
             stage.setScene(scene);
             stage.show();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

     @FXML
     void coursesButton(MouseEvent event) throws UserNotFoundException {
         User user = userService.getUserbyEmail(currentUser.getEmail());
         try {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/gestion-cour-admin.fxml"));
             Parent root = loader.load();
             GestionCourAdmin gestionCourAdmin = loader.getController();
             gestionCourAdmin.initialize(user.getId());
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
     void homeButton(MouseEvent event) {
         try {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/FrontHome.fxml"));
             Parent root = loader.load();
             FrontHomeController frontHomeController = loader.getController();
             frontHomeController.initialize(currentUser.getId());

             Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
             Scene scene = new Scene(root, 800, 600);
             stage.setScene(scene);
             stage.show();
         } catch (IOException | UserNotFoundException e) {
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
     void recruitmentButton(MouseEvent event) throws UserNotFoundException{
         User user = userService.getUserbyEmail(currentUser.getEmail());
         try {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/FrontJob.fxml"));
             Parent root = loader.load();
             FrontJob frontJob = loader.getController();
             frontJob.initialize(user.getId());
             Scene scene = new Scene(root, 800, 600);
             Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
             stage.setScene(scene);
             stage.show();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

     @FXML
     void shopButton(MouseEvent event) throws UserNotFoundException {
         User user = userService.getUserbyEmail(currentUser.getEmail());
         try {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/Product.fxml"));
             Parent root = loader.load();
             ProductController productController = loader.getController();
             productController.initialize(currentUser.getId());
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
     void donation(MouseEvent event) throws UserNotFoundException {
         User user = userService.getUserbyEmail(currentUser.getEmail());
         try {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/AddCharity.fxml"));
             Parent root = loader.load();
             AddCharityController addCharityController = loader.getController();
             addCharityController.initialize(user.getId());
             Scene scene = new Scene(root, 800, 600);
             Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
             stage.setScene(scene);
             stage.show();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
 }