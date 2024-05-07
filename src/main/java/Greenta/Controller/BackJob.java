package greenta.Controller;

import greenta.Connectors.MyDataBase;
import greenta.models.Job;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class BackJob implements Initializable {

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

    @FXML
    private Button view;


    private FrontJob frontJobController;

    // Add a method to set the FrontJob controller
    public void setFrontJobController(FrontJob frontJobController) {
        this.frontJobController = frontJobController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Initialize database connection
            cnx = MyDataBase.getInstance().getCnx();

            // Call showJobs to populate the table
            showJobs();


        } catch (SQLException e) {
            showAlert("Error", "Failed to initialize: " + e.getMessage());
        }


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
                    Image image = new Image(imagePath);
                    colImg.toString();
                } else {
                    colImg.toString();
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
        colImg.setCellFactory(param -> new TableCell<Job, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);

                if (empty || imagePath == null) {
                    setGraphic(null);
                } else {
                    Image image = new Image(new File(imagePath).toURI().toString());
                    imageView.setImage(image);
                    imageView.setFitWidth(100); // Adjust width as needed
                    imageView.setFitHeight(100); // Adjust height as needed
                    setGraphic(imageView);
                }
            }
        });
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
        Job selectedJob = table.getSelectionModel().getSelectedItem();
        if (selectedJob != null) {
            String organisation = tfOrg.getText();
            String title = tfTitle.getText();
            String description = tfDescription.getText();
            LocalDate startDate = tfStartDate.getValue();

            if (organisation.isEmpty() || title.isEmpty() || description.isEmpty() || startDate == null) {
                showAlert("Error", "All fields are required.");
                return;
            }

            String qry = "UPDATE job SET organisation=?, title=?, description=?, startdate=?, picture=? WHERE id=?";
            try {
                PreparedStatement pstm = cnx.prepareStatement(qry);
                pstm.setString(1, organisation);
                pstm.setString(2, title);
                pstm.setString(3, description);
                Date sqlStartDate = Date.valueOf(startDate);
                pstm.setDate(4, sqlStartDate);
                pstm.setString(5, selectedJob.getPicture());
                pstm.setInt(6, selectedJob.getId());
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
        String organisation = tfOrg.getText();
        String title = tfTitle.getText();
        String description = tfDescription.getText();
        LocalDate startDate = tfStartDate.getValue();
        String picture = null;

        if (organisation.isEmpty() || title.isEmpty() || description.isEmpty() || startDate == null) {
            showAlert("Error", "All fields are required.");
            return;
        }
        if (startDate.isBefore(LocalDate.now())) {
            showAlert("Error", "Start date cannot be in the past.");
            return;
        }
        // Get the selected Job object if any
        Job selectedJob = table.getSelectionModel().getSelectedItem();
        if (selectedJob != null) {
            picture = selectedJob.getPicture(); // Get the image path from the selected Job
        }

        String qry = "INSERT INTO job (organisation, title, description, startdate, picture) VALUES (?, ?, ?, ?, ?)";
        cnx = MyDataBase.getInstance().getCnx();
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, organisation);
            pstm.setString(2, title);
            pstm.setString(3, description);
            Date sqlStartDate = Date.valueOf(startDate);
            pstm.setDate(4, sqlStartDate);
            pstm.setString(5, picture); // Insert the image path
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
                if (image.isError()) {
                    showAlert("Error", "Failed to load the image.");
                } else {
                    Job selectedJob = table.getSelectionModel().getSelectedItem();
                    if (selectedJob != null) {
                        // Set the image path to the selected Job object
                        selectedJob.setPicture(file.getAbsolutePath());
                        // Refresh the table to reflect the changes
                        showJobs();
                    }
                }
            } catch (FileNotFoundException | SQLException e) {
                showAlert("Error", "File not found: " + file.getAbsolutePath());
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    void go(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/greenta/FrontJob.fxml"));
            Parent newRoot = fxmlLoader.load();

            // Set the FrontJob controller
            FrontJob frontJobController = fxmlLoader.getController();
            setFrontJobController(frontJobController);

            // Get the current stage from the event source
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene of the current stage with the new scene
            currentStage.setScene(new Scene(newRoot));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
