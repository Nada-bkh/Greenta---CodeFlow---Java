package com.example.greenta.Interfaces;

import com.example.greenta.Exceptions.UserNotFoundException;

public interface Listener {
    void onVoirQuizClicked(int idCour) throws UserNotFoundException;
    void onVoirQuestionClicked(int idQuiz) throws UserNotFoundException;
}
