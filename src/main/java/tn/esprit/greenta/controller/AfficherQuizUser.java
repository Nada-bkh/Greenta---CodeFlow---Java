package tn.esprit.greenta.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tn.esprit.greenta.FXMain;
import tn.esprit.greenta.entity.Cour;
import tn.esprit.greenta.entity.Quiz;
import tn.esprit.greenta.service.ServiceCour;
import tn.esprit.greenta.service.ServiceQuiz;

import java.io.IOException;
import java.util.List;

public class AfficherQuizUser
{
    @FXML
    private GridPane grid;
    @FXML
    public void initialize() {
        refresh();
    }
    ServiceQuiz serviceQuiz=new ServiceQuiz();
    void refresh(){
        grid.getChildren().clear();
        List<Quiz> quizList=serviceQuiz.afficher();
        int row=1;
        int column=0;
        for(Quiz q:quizList){
            FXMLLoader fxmlLoader = new FXMLLoader(FXMain.class.getResource("quiz-card-view.fxml"));
            AnchorPane anchorPane = null;
            try {
                anchorPane=fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            QuizCardView controller=fxmlLoader.getController();
            controller.remplireData(q);
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
        FXMLLoader fxmlLoader = new FXMLLoader(FXMain.class.getResource("afficher-cours-user.fxml"));
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
}