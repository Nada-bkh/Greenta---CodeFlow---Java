package com.example.greenta.GUI;



import com.example.greenta.Exceptions.UserNotFoundException;
import com.example.greenta.Models.Job;
import com.example.greenta.Models.User;
import com.example.greenta.Services.SessionService;
import com.example.greenta.Services.UserService;
import com.example.greenta.Utils.MyConnection;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.ResourceBundle;

public class FrontJob  {

    ResultSet rs = null;
    @FXML
    private MenuButton btnfilter;

    @FXML
    private MenuItem filterRecent;

    @FXML
    private MenuItem filterOldest;

    @FXML
    private TableView<Job> listjob;

    @FXML
    private TableColumn<Job, String> clTitle;

    @FXML
    private TableColumn<Job, String> colDescription;

    @FXML
    private TableColumn<Job, String> colImg;

    @FXML
    private TableColumn<Job, String> colOrg;

    @FXML
    private TableColumn<Job, LocalDate> colStartDate;

    @FXML
    private TextField searchfield;

    @FXML
    private Button profileLabel;
    private UserService userService = UserService.getInstance();
    SessionService sessionService = SessionService.getInstance();
    private User currentUser;

    @FXML
    private FilteredList<Job> filteredData;

    Connection connection = MyConnection.getInstance().getConnection();

@FXML
    public void initialize(int userId) throws UserNotFoundException {
        currentUser = userService.getUserbyID(userId);
        profileLabel.setText(currentUser.getFirstname());
        try {
            showJobs();
            listjob.setRowFactory(tv -> {
                TableRow<Job> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && !row.isEmpty()) {
                        Job selectedJob = row.getItem();
                        try {
                            showPopup(selectedJob);
                        } catch (UserNotFoundException e) {
                            System.out.println("disconnected");
                        }
                    }
                });
                return row;
            });

            filteredData = new FilteredList<>(getJob(), p -> true);

            searchfield.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(job -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    return job.getTitle().toLowerCase().contains(lowerCaseFilter) ||
                            job.getDescription().toLowerCase().contains(lowerCaseFilter) ||
                            job.getOrganisation().toLowerCase().contains(lowerCaseFilter);
                });
            });

            SortedList<Job> sortedData = new SortedList<>(filteredData);

            filterRecent.setOnAction(event -> {
                sortedData.setComparator(Comparator.comparing(Job::getStartdate).reversed());
            });

            filterOldest.setOnAction(event -> {
                sortedData.setComparator(Comparator.comparing(Job::getStartdate));
            });

            listjob.setItems(sortedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Job> getJob() throws SQLException {
        ObservableList<Job> jobs = FXCollections.observableArrayList();
        String qry = "select * from Job";
        try {
            PreparedStatement pstm = connection.prepareStatement(qry);
            rs = pstm.executeQuery();
            while (rs.next()) {
                Job job = new Job();
                job.setDescription(rs.getString("description"));
                job.setId(rs.getInt("id"));
                job.setTitle(rs.getString("title"));
                job.setOrganisation(rs.getString("organisation"));
                job.setStartdate(rs.getDate("startdate").toLocalDate());
                job.setPicture(rs.getString("picture"));
                jobs.add(job); // Add the job to the list
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return jobs;
    }

    public void showJobs() throws SQLException {
        ObservableList<Job> list = getJob();
        listjob.setItems(list);
        colDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        colOrg.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrganisation()));
        clTitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        colImg.setCellValueFactory(new PropertyValueFactory<>("picture"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startdate"));
    }
    @FXML
    private void handleFilterRecent() {

    }

    @FXML
    private void handleFilterOldest() {

    }
    private void showPopup(Job job) throws UserNotFoundException {
        User user = userService.getUserbyEmail(currentUser.getEmail());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("com/example/greenta/App.fxml"));
            Parent root = loader.load();
            AppController appController = loader.getController();
            // Pass necessary data to AppController if needed
            // For example: appController.setJob(job);
            appController.initialize(user.getId());
            Stage stage = new Stage();
            stage.setTitle("Job Details"); // Set the title of the new stage
            stage.setScene(new Scene(root)); // Set the scene with the content loaded from FXML
            stage.show(); // Show the stage
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error loading popup.");
        }


}
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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


