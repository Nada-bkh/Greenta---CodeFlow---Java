package tn.esprit.greenta.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import tn.esprit.greenta.FXMain;
import tn.esprit.greenta.entity.Cour;
import tn.esprit.greenta.entity.Question;
import tn.esprit.greenta.service.ServiceCour;
import tn.esprit.greenta.service.ServiceQuestion;

import java.io.IOException;
import java.util.List;

public class AfficherQuestionUser
{
    @FXML
    private GridPane grid;

    @FXML
    public void initialize() {
        refresh();
    }
    ServiceQuestion serviceQuestion=new ServiceQuestion();
    void refresh(){
        grid.getChildren().clear();
        List<Question> questions=serviceQuestion.afficher();
        int row=1;
        int column=0;
        for(Question q:questions){
            FXMLLoader fxmlLoader = new FXMLLoader(FXMain.class.getResource("question-card-view.fxml"));
            AnchorPane anchorPane = null;
            try {
                anchorPane=fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            QuestionCardView controller=fxmlLoader.getController();
            controller.remplireData(q);
            if(column==1){
                column=0;
                row++;
            }
            grid.add(anchorPane,column++,row);
            GridPane.setMargin(anchorPane,new Insets(10));

        }
    }
}