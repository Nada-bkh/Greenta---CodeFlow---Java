package com.example.greenta.GUI;

import com.example.greenta.Exceptions.UserNotFoundException;
import com.example.greenta.Models.App;
import com.example.greenta.Models.User;
import com.example.greenta.Services.MailSender1;
import com.example.greenta.Services.ServiceApp;
import com.example.greenta.Services.SessionService;
import com.example.greenta.Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class AppController {

    @FXML
    private Button btnsubmit;

    @FXML
    private Button btnupload;

    @FXML
    private WebView captcha;

    @FXML
    private TextField tfemail;

    @FXML
    private TextField tffirstname;

    @FXML
    private TextField tflastname;

    private String selectedFilePath;

    private ServiceApp serviceApp;
    @FXML
    private Button profileLabel;
    private UserService userService = UserService.getInstance();
    SessionService sessionService = SessionService.getInstance();
    private User currentUser;

    public AppController() {
        this.serviceApp = new ServiceApp();
    }

    @FXML
    private void handleFileUpload() {
        if (btnupload != null && btnupload.getScene() != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Resume");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
            );
            File selectedFile = fileChooser.showOpenDialog(btnupload.getScene().getWindow()); // Change btupload to btnupload
            if (selectedFile != null) {
                selectedFilePath = selectedFile.getAbsolutePath();
                System.out.println("Selected file: " + selectedFilePath);
            }
        } else {
            System.err.println("Error: btnupload or its scene is null."); // Change btupload to btnupload
        }
    }

    @FXML
    private void handleSubmit() {

        String firstname = tffirstname.getText();
        String lastname = tflastname.getText();
        String email = tfemail.getText();
        String pdf = selectedFilePath;

        if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || pdf.isEmpty()) {
            showAlert("Please fill all fields.");
            return;
        }

        App app = new App();
        app.setFirstname(firstname);
        app.setLastname(lastname);
        app.setEmail(email);
        app.setPdf(pdf);

        serviceApp.add(app);

        new MailSender1(app.getEmail(),"Welcome to Greenta!","Dear "+app.getFirstname() + ",\n" +
                "Welcome to UBank!\n" +
                "\n" +
                "We are thrilled to have you as a new member of our banking family. As you embark on this financial journey with us, we promise to provide exceptional service, personalized solutions, and a seamless banking experience.\n" +
                "\n" +
                "At XYZ Bank, we understand that your financial needs are unique. Whether you’re saving for the future, planning investments, or managing day-to-day transactions, our dedicated team is here to assist you every step of the way.\n" +
                "\n" +
                "As a valued client, you’ll have access to a wide range of services, including online banking, mobile apps, and personalized advice from our expert advisors. We are committed to ensuring your financial well-being and helping you achieve your goals.\n" +
                "\n" +
                "Feel free to explore our website, visit our branches, or reach out to our customer support team for any assistance. We look forward to building a lasting relationship with you and making your banking experience truly exceptional.\n" +
                "\n" +
                "Once again, welcome to UBank!\n" +
                "\n" +
                "Best regards, The  UBank Team");

        showAlert("App added successfully!");
    }

    public void initialize(int userId) throws UserNotFoundException {
        currentUser = userService.getUserbyID(userId);
        profileLabel.setText(currentUser.getFirstname());
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void charityButton(MouseEvent event) throws UserNotFoundException {
        User user = userService.getUserbyEmail(currentUser.getEmail());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/ListCharity.fxml"));
            Parent root = loader.load();
            ListCharityController listCharityController = loader.getController();
            listCharityController.initialize(user.getId());
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/afficher-cours-user.fxml"));
            Parent root = loader.load();
            AfficherCoursUser afficherCoursUser = loader.getController();
            afficherCoursUser.initialize(user.getId());
            Scene scene = new Scene(root, 800, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void eventButton(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/EventFront.fxml"));
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
    void homeButton(MouseEvent event) throws UserNotFoundException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/FrontHome.fxml"));
            Parent root = loader.load();
            FrontHomeController frontHomeController = loader.getController();
            frontHomeController.initialize(currentUser.getId());
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
    void recruitmentButton(MouseEvent event) throws UserNotFoundException{
        User user = userService.getUserbyEmail(currentUser.getEmail());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/App.fxml"));
            Parent root = loader.load();
            AppController appController = loader.getController();
            appController.initialize(user.getId());
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/Shop.fxml"));
            Parent root = loader.load();
            ShopController shopController = loader.getController();
            shopController.initialize(currentUser.getId());
            Scene scene = new Scene(root, 800, 600);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/BackOffice.fxml"));
            Parent root = loader.load();
            BackOfficeController backOfficeController = loader.getController();
            backOfficeController.initialize(currentUser.getId());
            Scene scene = new Scene(root, 800, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException | UserNotFoundException e) {
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
}
