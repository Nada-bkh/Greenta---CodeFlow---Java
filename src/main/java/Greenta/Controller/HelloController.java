package greenta.Controller;


import greenta.Connectors.MyDataBase;
import greenta.models.Job;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private MenuButton btUploadimg;

    @FXML
    private TableView<?> table;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colImg;

    @FXML
    private TableColumn<?, ?> colOrg;

    @FXML
    private TableColumn<?, ?> colStartDate;

    @FXML
    private TableColumn<?, ?> colTitle;

    public void initialize(URL location, ResourceBundle resources) {
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
                job.setStartdate(rs.getDate("").toLocalDate());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return jobs;
    }

    @FXML
    private TextField tfOrg;

    @FXML
    private TextField tfStartDate;

    @FXML
    private TextField tfTitle;

    @FXML
    void DeleteJob(ActionEvent event) {

    }

    @FXML
    void UpdateJob(ActionEvent event) {

    }

    @FXML
    void clearFiel(ActionEvent event) {

    }

    @FXML
    void createJob(ActionEvent event) {

    }

    @FXML
    void uploadimg(ActionEvent event) {

    }

}
