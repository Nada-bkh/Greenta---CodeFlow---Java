package com.example.greenta.Services;

import com.example.greenta.Interfaces.IService;
import com.example.greenta.Models.Epreuve;
import com.example.greenta.Utils.MyConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceEpreuve implements IService<Epreuve> {
    Connection connection = MyConnection.getInstance().getConnection();

    @Override
    public void ajouter(Epreuve epreuve) {
        try {
            String query = "INSERT INTO `epreuve` (`quizid_id`, `date_p`, `note`, `etat`) VALUES (?,?,?,?)";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, epreuve.getQuizid_id());
            pst.setTimestamp(2, Timestamp.valueOf(epreuve.getDate_p()));
            pst.setInt(3, epreuve.getNote());
            pst.setBoolean(4, epreuve.isEtat());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    @Override
    public void modifier(Epreuve epreuve) {
        try {
            String query = "UPDATE `epreuve` SET `quizid_id`=?, `date_p`=?, `note`=?, `etat`=? WHERE id=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, epreuve.getQuizid_id());
            pst.setTimestamp(2, Timestamp.valueOf(epreuve.getDate_p()));
            pst.setInt(3, epreuve.getNote());
            pst.setBoolean(4, epreuve.isEtat());
            pst.setInt(5, epreuve.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        try {
            String query = "DELETE FROM `epreuve` WHERE id=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    @Override
    public List<Epreuve> afficher() {
        List<Epreuve> le = new ArrayList<>();
        try {
            String query = "SELECT * FROM epreuve";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Epreuve e = new Epreuve();
                e.setId(rs.getInt("id"));
                e.setQuizid_id(rs.getInt("quizid_id"));
                e.setDate_p(rs.getTimestamp("date_p").toLocalDateTime());
                e.setNote(rs.getInt("note"));
                e.setEtat(rs.getBoolean("etat"));
                le.add(e);
            }
        } catch (SQLException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
        return le;
    }
}
