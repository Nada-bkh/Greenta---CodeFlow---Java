package com.example.greenta.GUI;

import com.example.greenta.Models.Quiz;
import com.example.greenta.Services.ServiceQuiz;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class GestionQuizAdmin {

    @FXML
    private TextField tftitre;

    @FXML
    private TextField tfduree;

    @FXML
    private TextField tfnote;

    @FXML
    private TableColumn<Quiz, Integer> tcnbqst;

    @FXML
    private TableView<Quiz> tvquiz;

    @FXML
    private TableColumn<Quiz, String> tctitre;

    @FXML
    private TableColumn<Quiz, Integer> tccour;

    @FXML
    private TableColumn<Quiz, Integer> tcduree;

    @FXML
    private TableColumn<Quiz, Integer> tcnote;

    @FXML
    private TableColumn<Quiz, LocalDateTime> tcdate;

    @FXML
    private TableColumn<Quiz, Void> tcquestion;
    @FXML
    private TextField tfnbqst;
    ServiceQuiz serviceQuiz=new ServiceQuiz();
    public static int idQuiz;
    @FXML
    void initialize(){
        refresh();
    }
    @FXML
    void ajouter(ActionEvent event) {
        Quiz quiz=new Quiz();
        quiz.setDuree(Integer.valueOf(tfduree.getText()));
        quiz.setNbrq(Integer.valueOf(tfnbqst.getText()));
        quiz.setNote(Integer.valueOf(tfnote.getText()));
        quiz.setTitre(tftitre.getText());
        quiz.setCreated_at(LocalDateTime.now());
        quiz.setCourid_id(GestionCourAdmin.idCour);
        serviceQuiz.ajouter(quiz);
        refresh();

    }

    @FXML
    void modifier(ActionEvent event) {
        Quiz quiz=tvquiz.getSelectionModel().getSelectedItem();
        if(quiz!=null){
            quiz.setDuree(Integer.valueOf(tfduree.getText()));
            quiz.setNbrq(Integer.valueOf(tfnbqst.getText()));
            quiz.setNote(Integer.valueOf(tfnote.getText()));
            quiz.setTitre(tftitre.getText());
            quiz.setCreated_at(LocalDateTime.now());
            serviceQuiz.modifier(quiz);
            refresh();
        }
    }

    @FXML
    void supprimer(ActionEvent event) {
        Quiz quiz=tvquiz.getSelectionModel().getSelectedItem();
        if(quiz!=null){
            serviceQuiz.supprimer(quiz.getId());
            refresh();
        }
    }
    void refresh(){
        List<Quiz> list=serviceQuiz.afficherParCour(GestionCourAdmin.idCour);
        tvquiz.getItems().setAll(list);
        tctitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        tcduree.setCellValueFactory(new PropertyValueFactory<>("duree"));
        tcnbqst.setCellValueFactory(new PropertyValueFactory<>("nbrq"));
        tccour.setCellValueFactory(new PropertyValueFactory<>("courid_id"));
        tcdate.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        tcnote.setCellValueFactory(new PropertyValueFactory<>("note"));
        tcquestion.setCellFactory(c->{
            return new TableCell<Quiz,Void>() {
                private final Button addQuiz = new Button("Add question");

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty){
                        setGraphic(null);
                    }
                    else{
                        setGraphic(addQuiz);
                        addQuiz.setOnAction(event->{
                            Quiz quiz=getTableView().getItems().get(getIndex());
                            idQuiz=quiz.getId();
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/gestion-question-admin.fxml"));
                            Scene scene = null;
                            try {
                                scene = new Scene(fxmlLoader.load());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            ((Stage) tfduree.getScene().getWindow()).close();
                            Stage stage=new Stage();
                            stage.setTitle("Greenta!");
                            stage.setScene(scene);
                            stage.show();
                        });
                    }
                }
            };

        });
    }
    @FXML
    void retour(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gestion-cour-admin.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ((Stage) tfduree.getScene().getWindow()).close();
        Stage stage=new Stage();
        stage.setTitle("Greenta!");
        stage.setScene(scene);
        stage.show();
    }


}
