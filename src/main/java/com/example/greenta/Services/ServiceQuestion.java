package com.example.greenta.Services;


import com.example.greenta.Interfaces.IService;
import com.example.greenta.Models.Question;
import com.example.greenta.Utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceQuestion implements IService<Question> {
    Connection connection = MyConnection.getInstance().getConnection();


    @Override
    public void ajouter(Question question) {
        try {
            String query = "INSERT INTO `question` (`quizid_id`, `question`, `reponse`, `note`,`reponseCorrecte`) VALUES (?,?,?,?,?)";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, question.getQuizid_id());
            pst.setString(2, question.getQuestion());
            pst.setString(3, question.getReponse());
            pst.setInt(4, question.getNote());
            pst.setInt(5,question.getReponseCorrecte());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    @Override
    public void modifier(Question question) {
        try {
            String query = "UPDATE `question` SET `quizid_id`=?, `question`=?, `reponse`=?, `note`=?, `reponseCorrecte`=? WHERE id=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, question.getQuizid_id());
            pst.setString(2, question.getQuestion());
            pst.setString(3, question.getReponse());
            pst.setInt(4, question.getNote());
            pst.setInt(5,question.getReponseCorrecte());
            pst.setInt(6, question.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        try {
            String query = "DELETE FROM `question` WHERE id=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    @Override
    public List<Question> afficher() {
        List<Question> lq = new ArrayList<>();
        try {
            String query = "SELECT * FROM question";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Question q = new Question();
                q.setId(rs.getInt("id"));
                q.setQuizid_id(rs.getInt("quizid_id"));
                q.setQuestion(rs.getString("question"));
                q.setReponse(rs.getString("reponse"));
                q.setNote(rs.getInt("note"));
                q.setReponseCorrecte(rs.getInt("reponseCorrecte"));
                lq.add(q);
            }
        } catch (SQLException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
        return lq;
    }
    public List<Question> afficherParQuiz(int idQuiz){
        return afficher().stream().filter(q->q.getQuizid_id()==idQuiz).collect(Collectors.toList());
    }
}
