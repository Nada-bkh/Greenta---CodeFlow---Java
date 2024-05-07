package com.example.greenta.Services;

import com.example.greenta.Interfaces.IService;
import com.example.greenta.Models.Quiz;
import com.example.greenta.Utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceQuiz implements IService<Quiz> {
    Connection connection = MyConnection.getInstance().getConnection();

    @Override
    public void ajouter(Quiz quiz) {
        try {
            String query = "INSERT INTO `quiz` (`courid_id`, `titre`, `created_at`, `duree`, `note`, `nbrq`) VALUES (?,?,?,?,?,?)";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, quiz.getCourid_id());
            pst.setString(2, quiz.getTitre());
            pst.setTimestamp(3, Timestamp.valueOf(quiz.getCreated_at()));
            pst.setInt(4, quiz.getDuree());
            pst.setInt(5, quiz.getNote());
            pst.setInt(6, quiz.getNbrq());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    @Override
    public void modifier(Quiz quiz) {
        try {
            String query = "UPDATE `quiz` SET `courid_id`=?, `titre`=?, `created_at`=?, `duree`=?, `note`=?, `nbrq`=? WHERE id=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, quiz.getCourid_id());
            pst.setString(2, quiz.getTitre());
            pst.setTimestamp(3, Timestamp.valueOf(quiz.getCreated_at()));
            pst.setInt(4, quiz.getDuree());
            pst.setInt(5, quiz.getNote());
            pst.setInt(6, quiz.getNbrq());
            pst.setInt(7, quiz.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        try {
            String query = "DELETE FROM `quiz` WHERE id=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    @Override
    public List<Quiz> afficher() {
        List<Quiz> lq = new ArrayList<>();
        try {
            String query = "SELECT * FROM quiz";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Quiz q = new Quiz();
                q.setId(rs.getInt("id"));
                q.setCourid_id(rs.getInt("courid_id"));
                q.setTitre(rs.getString("titre"));
                q.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());
                q.setDuree(rs.getInt("duree"));
                q.setNote(rs.getInt("note"));
                q.setNbrq(rs.getInt("nbrq"));
                lq.add(q);
            }
        } catch (SQLException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
        return lq;
    }
    public List<Quiz> afficherParCour(int idCour){
        return afficher().stream().filter(q->q.getCourid_id()==idCour).collect(Collectors.toList());
    }
}
