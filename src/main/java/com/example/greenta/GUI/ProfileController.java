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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ProfileController {
    private UserService userService = UserService.getInstance();
    private SessionService sessionService = SessionService.getInstance();

    @FXML
    private Button editProfileBtn;

    @FXML
    private TextField email;

    @FXML
    private TextField firstname;

    @FXML
    private TextField lastname;

    @FXML
    private Label name;

    @FXML
    private TextField phone;

    @FXML
    private Label role;

    @FXML
    private Label usernameLabel;
    @FXML
    private Label usersBtn;

    private User currentUser;
    private boolean isEditMode = false;
    @FXML
    void initialize() {
        // Add listener to firstname text field
        firstname.textProperty().addListener((observable, oldValue, newValue) -> {
            // Update name label with capitalized first name
            name.setText(capitalizeFirstLetter(newValue));
        });
       /* setUsersButtonVisibility();
    }
    private void setUsersButtonVisibility() {
        if (currentUser != null && role.getText().equals("Admin")) {
            usersBtn.setVisible(true);
        } else {
            usersBtn.setVisible(false);
        }*/
    }

    public void initializeProfile(int userId) throws UserNotFoundException {
        try {
            currentUser = userService.getUserbyID(userId);
            if (currentUser != null) {
                // Populate the text fields with user data
                firstname.setText(currentUser.getFirstname());
                lastname.setText(currentUser.getLastname());
                email.setText(currentUser.getEmail());
                phone.setText(currentUser.getPhone());

                // Set the role label based on the user's role
                if (currentUser.getRoles() == Type.ROLE_ADMIN) {
                    role.setText("Admin");
                    usersBtn.setVisible(true);
                } else {
                    role.setText("Client");
                    usersBtn.setVisible(false);
                }

                // Set the username label (optional)
                name.setText(capitalizeFirstLetter(currentUser.getFirstname())); // or any other field you want to display as username

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
    void deleteAccount(MouseEvent event) throws UserNotFoundException{
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
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/Register.fxml"));
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
                } catch ( IOException e) {
                    // Handle exception appropriately
                    e.printStackTrace();
                }
            } else {
                System.out.println("Deletion canceled.");
            }
        });
    }

    @FXML
    void editProfile(ActionEvent event) throws UserNotFoundException, IncorrectPasswordException, InvalidPhoneNumberException, InvalidEmailException, EmptyFieldException {
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

    private void saveChanges() throws UserNotFoundException, IncorrectPasswordException, InvalidPhoneNumberException, InvalidEmailException, EmptyFieldException {
        // Update user object with new data
        currentUser.setFirstname(firstname.getText());
        currentUser.setLastname(lastname.getText());
        currentUser.setEmail(email.getText());
        currentUser.setPhone(phone.getText());

        // Save changes to the database
        userService.updateUser(currentUser);
    }

    @FXML
    void profileButton(MouseEvent event) throws UserNotFoundException {
        // Retrieve current user's ID from session and initialize profile
        int userId = sessionService.getCurrentUser().getId();
        initializeProfile(userId);
    }

    private void setFieldsEditable(boolean editable) {
        firstname.setEditable(editable);
        lastname.setEditable(editable);
        email.setEditable(editable);
        phone.setEditable(editable);
    }

    public void ForgotPassword(MouseEvent mouseEvent) {
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
    void usersButton(MouseEvent event) {
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
    void signOut(MouseEvent event) {
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
