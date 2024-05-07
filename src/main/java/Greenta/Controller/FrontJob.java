package greenta.Controller;


import greenta.Connectors.MyDataBase;
import greenta.models.Job;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class FrontJob implements Initializable {

    Connection cnx = null;
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
    private FilteredList<Job> filteredData;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            cnx = MyDataBase.getInstance().getCnx();
            showJobs();
            listjob.setRowFactory(tv -> {
                TableRow<Job> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && !row.isEmpty()) {
                        Job selectedJob = row.getItem();
                        showPopup(selectedJob);
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
        cnx = MyDataBase.getInstance().getCnx();
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
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
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colOrg.setCellValueFactory(new PropertyValueFactory<>("organisation"));
        clTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colImg.setCellValueFactory(new PropertyValueFactory<>("picture"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startdate"));
    }
    @FXML
    private void handleFilterRecent() {

    }

    @FXML
    private void handleFilterOldest() {

    }
    private void showPopup(Job job) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/greenta/App.fxml"));
            Parent root = loader.load();
            AppController appController = loader.getController();
            // Pass necessary data to AppController if needed
            // For example: appController.setJob(job);

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

}


