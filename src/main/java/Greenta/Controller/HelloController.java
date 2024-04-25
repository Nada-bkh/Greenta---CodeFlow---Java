package greenta.Controller;

import greenta.Connectors.MyDataBase;
import greenta.models.Job;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class HelloController {

    Connection cnx = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    @FXML
    private Button btClear;

    @FXML
    private Button btDelete;

    @FXML
    private Button btSave;

    @FXML
    private Button btUpdate;

    @FXML
    private Button btUploadimg;
    @FXML
    private TableColumn<?, ?> clTitle;

    @FXML
    private TableView<Job> table;

    @FXML
    private TableColumn<Job, String> colDescription;

    @FXML
    private TableColumn<Job, String> colImg;

    @FXML
    private TableColumn<Job, String> colOrg;

    @FXML
    private TableColumn<Job, LocalDate> colStartDate;

    @FXML
    private ImageView imgview;

    @FXML
    private TextArea tfDescription;

    @FXML
    private TextField tfOrg;

    @FXML
    private DatePicker tfStartDate;

    @FXML
    private TextField tfTitle;

    public void initialize(URL location, ResourceBundle resources) throws SQLException {
        showJobs();
        // Set up listener for table selection change
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tfTitle.setText(newSelection.getTitle());
                tfOrg.setText(newSelection.getOrganisation());
                tfDescription.setText(newSelection.getDescription());
                tfStartDate.setValue(newSelection.getStartdate());

                // Load and display image
                String imagePath = newSelection.getPicture();
                if (imagePath != null) {
                    File file = new File(imagePath);
                    if (file.exists()) {
                        try {
                            FileInputStream input = new FileInputStream(file);
                            Image image = new Image(input);
                            imgview.setImage(image);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        imgview.setImage(null); // If file doesn't exist, clear the ImageView
                    }
                } else {
                    imgview.setImage(null); // If image path is null, clear the ImageView
                }
            }
        });
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
        table.setItems(list);
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colOrg.setCellValueFactory(new PropertyValueFactory<>("organisation"));
        clTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colImg.setCellValueFactory(new PropertyValueFactory<>("picture"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startdate"));
    }

    @FXML
    void DeleteJob(ActionEvent event) {
        Job selectedJob = table.getSelectionModel().getSelectedItem(); // Get the selected job
        if (selectedJob != null) {
            String qry = "DELETE FROM job WHERE id=?";
            try {
                PreparedStatement pstm = cnx.prepareStatement(qry);
                pstm.setInt(1, selectedJob.getId()); // Set the ID of the selected job for the WHERE clause
                pstm.executeUpdate();
                showJobs();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @FXML
    void UpdateJob(ActionEvent event) {
        Job selectedJob = table.getSelectionModel().getSelectedItem(); // Get the selected job
        if (selectedJob != null) {
            String qry = "UPDATE job SET organisation=?, title=?, description=?, startdate=? WHERE id=?";
            try {
                PreparedStatement pstm = cnx.prepareStatement(qry);
                pstm.setString(1, tfOrg.getText());
                pstm.setString(2, tfTitle.getText());
                pstm.setString(3, tfDescription.getText());
                LocalDate startDate = tfStartDate.getValue();
                java.sql.Date sqlStartDate = startDate != null ? java.sql.Date.valueOf(startDate) : null;
                pstm.setDate(4, sqlStartDate);
                pstm.setInt(5, selectedJob.getId()); // Set the ID of the selected job for the WHERE clause
                pstm.executeUpdate();
                showJobs();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }

    @FXML
    void clearFiel(ActionEvent event) {

    }

    @FXML
    void createJob(ActionEvent event) {
        String qry = "INSERT INTO job (organisation, title, description, startdate) VALUES (?, ?, ?, ?)";
        cnx = MyDataBase.getInstance().getCnx();
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, tfOrg.getText());
            pstm.setString(2, tfTitle.getText());
            pstm.setString(3, tfDescription.getText());
            LocalDate startDate = tfStartDate.getValue();
            java.sql.Date sqlStartDate = startDate != null ? java.sql.Date.valueOf(startDate) : null;
            pstm.setDate(4, sqlStartDate);
            pstm.executeUpdate();
            showJobs();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void uploadimg(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                FileInputStream input = new FileInputStream(file);
                Image image = new Image(input);
                imgview.setImage(image); // Set the image to the ImageView
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
}
