package tn.esprit.greenta.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import tn.esprit.greenta.FXMain;
import tn.esprit.greenta.entity.Question;
import tn.esprit.greenta.service.ServiceQuestion;

import java.io.IOException;

public class GestionQuestionAdmin {

    @FXML
    private TableView<Question> tvquestion;

    @FXML
    private TableColumn<Question, String> tcquestion;

    @FXML
    private TableColumn<Question, String> tcreponse;

    @FXML
    private TableColumn<Question, Integer> tccorrecte;

    @FXML
    private TableColumn<Question, Integer> tcnote;

    @FXML
    private TextField tfqst;

    @FXML
    private TextField tfrep1;

    @FXML
    private TextField tfrep2;

    @FXML
    private TextField tfrep3;

    @FXML
    private TextField tfrep4;

    @FXML
    private TextField tfrepcorr;

    @FXML
    private TextField tfnote;
    ServiceQuestion serviceQuestion=new ServiceQuestion();
    @FXML
    public void initialize() {
        refresh();
    }
    void refresh(){
        tvquestion.getItems().setAll(serviceQuestion.afficherParQuiz(GestionQuizAdmin.idQuiz));
        tcquestion.setCellValueFactory(new PropertyValueFactory<>("question"));
        tcreponse.setCellValueFactory(new PropertyValueFactory<>("reponse"));
        tcreponse.setCellFactory(new Callback<TableColumn<Question, String>, TableCell<Question, String>>() {
            @Override
            public TableCell<Question, String> call(TableColumn<Question, String> questionStringTableColumn) {
                return new TableCell<Question,String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty || item==null){
                            setText(null);
                            setGraphic(null);
                        }
                        else{
                            setText(item.replace(";","\n"));
                        }
                    }
                };
            }
        });
        tccorrecte.setCellValueFactory(new PropertyValueFactory<>("reponseCorrecte"));
        tcnote.setCellValueFactory(new PropertyValueFactory<>("note"));

    }
    @FXML
    void ajouter(ActionEvent event) {
        Question question=new Question();
        question.setQuestion(tfqst.getText());
        question.setReponseCorrecte(Integer.valueOf(tfrepcorr.getText()));
        question.setNote(Integer.valueOf(tfnote.getText()));
        String reponse=String.join(";",
                tfrep1.getText(),
                tfrep2.getText(),
                tfrep3.getText(),
                tfrep4.getText());
        question.setReponse(reponse);
        question.setQuizid_id(GestionQuizAdmin.idQuiz);
        serviceQuestion.ajouter(question);
        refresh();


    }

    @FXML
    void modifier(ActionEvent event) {
        Question question=tvquestion.getSelectionModel().getSelectedItem();
        if(question!=null){
            question.setQuestion(tfqst.getText());
            question.setReponseCorrecte(Integer.valueOf(tfrepcorr.getText()));
            question.setNote(Integer.valueOf(tfnote.getText()));
            String reponse=String.join(";",
                    tfrep1.getText(),
                    tfrep2.getText(),
                    tfrep3.getText(),
                    tfrep4.getText());
            question.setReponse(reponse);
            question.setQuizid_id(GestionQuizAdmin.idQuiz);
            serviceQuestion.modifier(question);
            refresh();
        }

    }

    @FXML
    void supprimer(ActionEvent event) {
        if(tvquestion.getSelectionModel().getSelectedItem()!=null){
            serviceQuestion.supprimer(tvquestion.getSelectionModel().getSelectedItem().getId());
            refresh();
        }
    }
    @FXML
    void retour(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(FXMain.class.getResource("gestion-quiz-admin.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ((Stage) tfnote.getScene().getWindow()).close();
        Stage stage=new Stage();
        stage.setTitle("Greenta!");
        stage.setScene(scene);
        stage.show();
    }


}