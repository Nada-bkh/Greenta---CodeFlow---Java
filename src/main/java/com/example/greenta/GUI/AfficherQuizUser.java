package com.example.greenta.GUI;

import com.example.greenta.Interfaces.Listener;
import com.example.greenta.Models.Quiz;
import com.example.greenta.Services.ServiceQuestion;
import com.example.greenta.Services.ServiceQuiz;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AfficherQuizUser implements Listener
{
    @FXML
    private GridPane grid;
    @FXML
    public void initialize() {

    }
    private int idCour;

    public void setIdCour(int idCour) {
        this.idCour = idCour;
    }

    ServiceQuiz serviceQuiz=new ServiceQuiz();
    void refresh(){
        System.out.println(idCour);
        grid.getChildren().clear();
        List<Quiz> quizList=serviceQuiz.afficherParCour(idCour);
        int row=1;
        int column=0;
        for(Quiz q:quizList){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/quiz-card-view.fxml"));
            AnchorPane anchorPane = null;
            try {
                anchorPane=fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            QuizCardView controller=fxmlLoader.getController();
            controller.remplireData(q);
            controller.setListener(this);
            if(column==3){
                column=0;
                row++;
            }
            grid.add(anchorPane,column++,row);
            GridPane.setMargin(anchorPane,new Insets(10));

        }
    }
    @FXML
    void retour(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/afficher-cours-user.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ((Stage) grid.getScene().getWindow()).close();
        Stage stage=new Stage();
        stage.setTitle("Greenta!");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void onVoirQuizClicked(int idCour) {

    }

    @Override
    public void onVoirQuestionClicked(int idQuiz) {
        ServiceQuestion serviceQuestion=new ServiceQuestion();
        if(serviceQuestion.afficherParQuiz(idQuiz).size()==0){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No question found");
            alert.showAndWait();
        }
        else{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/afficher-question-user.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ((Stage) grid.getScene().getWindow()).close();
            AfficherQuestionUser controller=fxmlLoader.getController();
            controller.setIdQuiz(idQuiz);
            controller.initialQuestion();
            Stage stage=new Stage();
            stage.setTitle("Greenta!");
            stage.setScene(scene);
            stage.show();
        }

    }
}