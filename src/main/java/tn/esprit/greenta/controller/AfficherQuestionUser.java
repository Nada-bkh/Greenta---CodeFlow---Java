package tn.esprit.greenta.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import javafx.scene.Node;
import javafx.scene.layout.FlowPane; // Importation de FlowPane

import javafx.stage.Stage;
import javafx.util.Duration;
import tn.esprit.greenta.FXMain;
import tn.esprit.greenta.entity.Cour;
import tn.esprit.greenta.entity.Epreuve;
import tn.esprit.greenta.entity.Question;
import tn.esprit.greenta.service.ServiceCour;
import tn.esprit.greenta.service.ServiceEpreuve;
import tn.esprit.greenta.service.ServiceQuestion;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class AfficherQuestionUser
{
    @FXML
    private Pane pane;
    @FXML
    private Label timer;
    @FXML
    private FlowPane progressPane; // Déclaration de progressPane
    ServiceQuestion serviceQuestion=new ServiceQuestion();
    private Timeline timeline;
    private int timeSeconds;
    int i=0;
    private int idQuiz;
    public static int noteTotal=0;
    List<Question> list;
    public void setIdQuiz(int idQuiz) {
        this.idQuiz = idQuiz;
    }


    @FXML
    void precedent(ActionEvent event) {
        if(i>0){
            i--;
            afficherQuestion(list.get(i));
            updateProgressTypography(i);
        }

    }
    ServiceEpreuve serviceEpreuve=new ServiceEpreuve();


    @FXML
    void suivant(ActionEvent event) {
        if(i<list.size()-1){
            i++;
            afficherQuestion(list.get(i));
            updateProgressTypography(i);
        }
        else{
            timeline.stop();
            Epreuve epreuve=new Epreuve(idQuiz,LocalDateTime.now(),noteTotal,true);
            serviceEpreuve.ajouter(epreuve);
            FXMLLoader fxmlLoader = new FXMLLoader(FXMain.class.getResource("epreuve-reponse.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            EpreuveReponse controller=fxmlLoader.getController();
            controller.setEpreuve(epreuve);
            controller.report();
            ((Stage) pane.getScene().getWindow()).close();
            Stage stage=new Stage();
            stage.setTitle("Greenta!");
            stage.setScene(scene);
            stage.show();

        }

    }

    @FXML
    public void initialize() {
        timeSeconds=60;
        initialQuestion();
        if(timeline!=null){
            timeline.stop();
        }
        timeline=new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(
                Duration.seconds(1),
                e->{
                    timeSeconds--;
                    timer.setText(timeToString(timeSeconds));
                    if(timeSeconds<=0){
                        timeline.stop();
                        timer.setText("END");
                        Epreuve epreuve=new Epreuve(idQuiz,LocalDateTime.now(),0,true);
                        serviceEpreuve.ajouter(epreuve);
                        FXMLLoader fxmlLoader = new FXMLLoader(FXMain.class.getResource("epreuve-reponse.fxml"));
                        Scene scene = null;
                        try {
                            scene = new Scene(fxmlLoader.load());
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        EpreuveReponse controller=fxmlLoader.getController();
                        controller.setEpreuve(epreuve);
                        controller.report();
                        ((Stage) pane.getScene().getWindow()).close();
                        Stage stage=new Stage();
                        stage.setTitle("Greenta!");
                        stage.setScene(scene);
                        stage.show();
                    }

                }
        ));
        timeline.playFromStart();

        updateProgressTypography(0); // Met à jour la typographie pour la première question
    }
    String timeToString(int timeSeconds){
        int min=timeSeconds/60;
        int sec=timeSeconds%60;
        return String.format("%02d:%02d",min,sec);
    }

    void initialQuestion(){
        list=serviceQuestion.afficherParQuiz(idQuiz);
        if(list.size()!=0){

            afficherQuestion(list.get(i));



         //   ----------------------------------------------------------------
            /*// Obtention du nombre total de questions à partir de la liste de questions
            int totalQuestions = list.size();

            // Ajout des marqueurs de progression textuels dans progressPane
            for (int i = 0; i < totalQuestions; i++) {
                Label progressLabel = new Label(Integer.toString(i +1));
                progressLabel.setStyle("-fx-padding: 10px;");
                progressPane.getChildren().add(progressLabel);
            }*/
        }

    }


    private void updateProgressTypography(int currentQuestionIndex) {
        // Efface tous les marqueurs de progression précédents
        progressPane.getChildren().clear();
        // Obtention du nombre total de questions à partir de la liste de questions
        int totalQuestions = list.size();
        // Parcours de tous les marqueurs de progression
        for (int i =0 ; i < totalQuestions; i++) {
            Label progressLabel = new Label(Integer.toString(i + 1));
            progressLabel.setStyle("-fx-padding: 10px; -fx-font-size: 14px;"); // Définir la taille de la police
            // Appliquer un style différent à la question actuelle
            if (i == currentQuestionIndex) {
                progressLabel.setStyle("-fx-padding: 10px; -fx-font-size: 27px; -fx-font-weight: bold; -fx-text-fill: #ff0000;"); // Par exemple, en gras et rouge
            } else {
                progressLabel.setStyle("-fx-padding: 10px; -fx-font-size: 22px; -fx-text-fill: #000000;"); // Par exemple, en noir
            }

            progressPane.getChildren().add(progressLabel);
        }
    }


    void afficherQuestion(Question question){
        pane.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader(FXMain.class.getResource("question-card-view.fxml"));
        AnchorPane anchorPane = null;
        try {
            anchorPane=fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        QuestionCardView controller=fxmlLoader.getController();
        controller.remplireData(question);
        pane.getChildren().add(anchorPane);
    }
    /*void refresh(){
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
    }*/
}