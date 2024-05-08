package com.example.greenta.GUI;

import com.example.greenta.Exceptions.UserNotFoundException;
import com.example.greenta.Interfaces.Listener;
import com.example.greenta.Models.Quiz;
import com.example.greenta.Models.User;
import com.example.greenta.Services.SessionService;
import com.example.greenta.Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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
    private Listener listener;
    private final UserService userService = UserService.getInstance();
    private SessionService sessionService = SessionService.getInstance();
    private User currentUser;

    @FXML
    public void initialize(int userId) throws UserNotFoundException {
        currentUser = userService.getUserbyID(userId);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

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
    void voirqst(ActionEvent event) throws UserNotFoundException {
        if(listener!=null){
            listener.onVoirQuestionClicked(quiz.getId());
        }
    }

}