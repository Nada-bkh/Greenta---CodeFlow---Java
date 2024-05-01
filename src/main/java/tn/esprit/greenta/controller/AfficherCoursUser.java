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
import tn.esprit.greenta.service.Listener;
import tn.esprit.greenta.service.ServiceCour;

import java.io.IOException;
import java.util.List;

public class AfficherCoursUser implements Listener
{
    @FXML
    private GridPane grid;

    @FXML
    public void initialize() {
        refresh();
    }
    ServiceCour serviceCour=new ServiceCour();
    void refresh(){
        grid.getChildren().clear();
        List<Cour> courList=serviceCour.afficher();
        int row=1;
        int column=0;
        for(Cour c:courList){
            FXMLLoader fxmlLoader = new FXMLLoader(FXMain.class.getResource("cour-card-view.fxml"));
            AnchorPane anchorPane = null;
            try {
                anchorPane=fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            CourCardView controller=fxmlLoader.getController();
            controller.remplireData(c);
            controller.setListener(this);
            if(column==3){
                column=0;
                row++;
            }
            grid.add(anchorPane,column++,row);
            GridPane.setMargin(anchorPane,new Insets(10));

        }
    }

    @Override
    public void onVoirQuizClicked(int idCour) {

        FXMLLoader fxmlLoader = new FXMLLoader(FXMain.class.getResource("afficher-quiz-user.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AfficherQuizUser controller=fxmlLoader.getController();
        controller.setIdCour(idCour);
        controller.refresh();
        ((Stage) grid.getScene().getWindow()).close();
        Stage stage=new Stage();
        stage.setTitle("Greenta!");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void onVoirQuestionClicked(int idQuiz) {

    }


}