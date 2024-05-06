package com.example.greenta.GUI;

import com.example.greenta.Exceptions.*;
import com.example.greenta.Models.User;
import com.example.greenta.Services.SessionService;
import com.example.greenta.Services.UserService;
import com.example.greenta.Utils.Type;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;

public class ProfileController {
    private UserService userService = UserService.getInstance();
    private SessionService sessionService = SessionService.getInstance();

    @FXML
    private ImageView BOImage;

    @FXML
    private Label backOfficeButton;

    @FXML
    private Button editProfileBtn;

    @FXML
    private Label charityLabel;

    @FXML
    private Label coursesLabel;

    @FXML
    private Label eventLabel;

    @FXML
    private Label homeLabel;

    @FXML
    private Button profileLabel;

    @FXML
    private Label recruitmentLabel;

    @FXML
    private Label shopLabel;

    @FXML
    private Label changePassword;

    @FXML
    private Text emailText;

    @FXML
    private Text fullNameText;

    @FXML
    private Text lastNameText;

    @FXML
    private Text phoneNumberText;

    @FXML
    private Label roleLabel;

    @FXML
    private TextField firstnameTF;

    @FXML
    private TextField lastnameTF;

    @FXML
    private TextField emailTF;

    @FXML
    private TextField phoneTF;

    private User currentUser;
    private boolean isEditMode = false;

    @FXML
    void initialize() {
        // Add listener to firstname text field
        fullNameText.textProperty().addListener((observable, oldValue, newValue) -> {
            // Update name label with capitalized first name
            fullNameText.setText(capitalizeFirstLetter(newValue));
        });
        firstnameTF.setVisible(false);
        lastnameTF.setVisible(false);
        emailTF.setVisible(false);
        phoneTF.setVisible(false);
        firstnameTF.setEditable(false);
        lastnameTF.setEditable(false);
        emailTF.setEditable(false);
        phoneTF.setEditable(false);
    }

    public void initializeProfile(int userId) throws UserNotFoundException {
        try {
            currentUser = userService.getUserbyID(userId);
            profileLabel.setText(currentUser.getFirstname());
            if (currentUser != null) {
                // Populate the text fields with user data
                fullNameText.setText(capitalizeFirstLetter(currentUser.getFirstname()));
                lastNameText.setText(currentUser.getLastname());
                emailText.setText(currentUser.getEmail());
                phoneNumberText.setText(currentUser.getPhone());

                // Set the role label based on the user's role
                if (currentUser.getRoles() == Type.ROLE_ADMIN) {
                    roleLabel.setText("Admin");
                    backOfficeButton.setVisible(true);
                    BOImage.setVisible(true);
                } else {
                    roleLabel.setText("Client");
                    backOfficeButton.setVisible(false);
                    BOImage.setVisible(false);
                }
                setFieldsEditable(false);
            } else {
                System.out.println("User not found.");
            }
        } catch (UserNotFoundException e) {
            // Handle exception appropriately
            e.printStackTrace();
        }
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @FXML
    void deleteAccount(MouseEvent event) throws UserNotFoundException {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Are you sure you want to delete this account permanently?");
        confirmationAlert.setContentText("This action cannot be undone.");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    if (currentUser != null) {
                        // Delete the user account
                        userService.deleteUser(currentUser);

                        // Display a success message
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText("Account Deleted");
                        successAlert.setContentText("Your account has been deleted successfully.");

                        // Navigate to register.fxml
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                                .getResource("/com/example/greenta/Register.fxml"));
                        Parent root = fxmlLoader.load();
                        Scene scene = new Scene(root);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();

                        // Show the success message
                        successAlert.showAndWait();
                    } else {
                        System.out.println("No user account to delete.");
                    }
                } catch (IOException e) {
                    // Handle exception appropriately
                    e.printStackTrace();
                }
            } else {
                System.out.println("Deletion canceled.");
            }
        });
    }

    @FXML
    void editButton(ActionEvent event) throws UserNotFoundException, IncorrectPasswordException,
            InvalidPhoneNumberException, InvalidEmailException, EmptyFieldException {
        if (isEditMode) {
            // Save changes and switch to read-only mode
            saveChanges();
            editProfileBtn.setText("Edit");
            isEditMode = false;
        } else {
            // Enable editing
            editProfileBtn.setText("Save");
            isEditMode = true;
        }
        setFieldsEditable(isEditMode);
    }

    private void saveChanges() throws UserNotFoundException, IncorrectPasswordException,
            InvalidPhoneNumberException, InvalidEmailException, EmptyFieldException {

        currentUser.setFirstname(firstnameTF.getText());
        currentUser.setLastname(lastnameTF.getText());
        currentUser.setEmail(emailTF.getText());
        currentUser.setPhone(phoneTF.getText());

        userService.updateUser(currentUser);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informations updated successfully !");
        alert.setHeaderText(null);
        alert.setContentText("Your informations have been updated successfully !");
        alert.showAndWait();
        initializeProfile(currentUser.getId());
    }

    @FXML
    void profileButton(MouseEvent event) throws UserNotFoundException {
        // Retrieve current user's ID from session and initialize profile
        int userId = sessionService.getCurrentUser().getId();
        initializeProfile(userId);
    }

    private void setFieldsEditable(boolean editable) {
        if (editable) {
            firstnameTF.setText(fullNameText.getText());
            lastnameTF.setText(lastNameText.getText());
            emailTF.setText(emailText.getText());
            phoneTF.setText(phoneNumberText.getText());
        }
        firstnameTF.setVisible(editable);
        firstnameTF.setEditable(editable);
        lastnameTF.setVisible(editable);
        lastnameTF.setEditable(editable);
        emailTF.setVisible(editable);
        emailTF.setEditable(editable);
        phoneTF.setVisible(editable);
        phoneTF.setEditable(editable);
        fullNameText.setVisible(!editable);
        lastNameText.setVisible(!editable);
        emailText.setVisible(!editable);
        phoneNumberText.setVisible(!editable);
        phoneNumberText.setVisible(!editable);
    }

    public void changePassword(MouseEvent mouseEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/greenta/PhoneNumber.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (Exception e) {
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
    void charityButton(MouseEvent event) {

    }

    @FXML
    void coursesButton(MouseEvent event) {

    }

    @FXML
    void eventButton(MouseEvent event) {

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
        int userId = sessionService.getCurrentUser().getId();
        initializeProfile(userId);
    }

    @FXML
    void recruitmentButton(MouseEvent event) {

    }

    @FXML
    void shopButton(MouseEvent event) {

    }
}
