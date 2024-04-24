package tn.esprit.greenta.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import tn.esprit.greenta.entity.Cour;
import tn.esprit.greenta.entity.Quiz;

public class QuizCardView {

    @FXML
    private Label ltitre;

    @FXML
    private Label lduree;

    @FXML
    private Label lnote;

    @FXML
    private Label lnbqst;

    @FXML
    private Label ldate;
    Quiz quiz;
    void remplireData(Quiz quiz){
        this.quiz=quiz;
        lduree.setText(String.valueOf(quiz.getDuree()));
        ltitre.setText(quiz.getTitre());
        ldate.setText(String.valueOf(quiz.getCreated_at()));
        lnbqst.setText(String.valueOf(quiz.getNbrq()));
        lnote.setText(String.valueOf(quiz.getNote()));
    }
    @FXML
    void voirqst(ActionEvent event) {

    }

}