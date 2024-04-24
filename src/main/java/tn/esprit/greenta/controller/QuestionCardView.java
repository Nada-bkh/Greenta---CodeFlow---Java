package tn.esprit.greenta.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import tn.esprit.greenta.entity.Question;

import java.util.List;

public class QuestionCardView
{
    @FXML
    private RadioButton rep1;
    @FXML
    private RadioButton rep2;
    @FXML
    private RadioButton rep3;
    @FXML
    private RadioButton rep4;
    @FXML
    private Label lnote;
    @FXML
    private Label lqst;

    @FXML
    public void initialize() {
    }
    void remplireData(Question question){
        lnote.setText(String.valueOf(question.getNote()));
        lqst.setText(question.getQuestion());
        String[] reponse=question.getReponse().split(";");
        rep1.setText(reponse[0]);
        rep2.setText(reponse[1]);
        rep3.setText(reponse[2]);
        rep4.setText(reponse[3]);
    }
    @FXML
    void repondre(ActionEvent event) {

    }
}