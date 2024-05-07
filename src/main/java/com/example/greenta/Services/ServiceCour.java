package com.example.greenta.Services;

import com.example.greenta.Interfaces.IService;
import com.example.greenta.Models.Cour;
import com.example.greenta.Utils.MyConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceCour implements IService<Cour> {
    Connection connection = MyConnection.getInstance().getConnection();
    public ServiceCour(){
    }
    @Override
    public void ajouter(Cour cour) {
        try {

            String query = "INSERT INTO `cour`" +
                    "(`titre`, `description`," +
                    " `niveau`, `created_at`," +
                    " `categorie`, `pdfpath`) " +
                    "VALUES (?,?,?,?,?,?)";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, cour.getTitre());
            pst.setString(2, cour.getDescription());
            pst.setString(3, cour.getNiveau());
            pst.setTimestamp(4, Timestamp.valueOf(cour.getCreated_at()));
            pst.setString(5, cour.getCategorie());
            pst.setString(6, cour.getPdfpath());
            pst.executeUpdate();
        }catch (SQLException e){
            System.out.println("Erreur:"+e.getMessage());
        }

    }

    @Override
    public void modifier(Cour cour) {
        try {

            String query = "UPDATE `cour` SET " +
                    "`titre`=?," +
                    "`description`=?," +
                    "`niveau`=?," +
                    "`created_at`=?," +
                    "`categorie`=?," +
                    "`pdfpath`=? WHERE id=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, cour.getTitre());
            pst.setString(2, cour.getDescription());
            pst.setString(3, cour.getNiveau());
            pst.setTimestamp(4, Timestamp.valueOf(cour.getCreated_at()));
            pst.setString(5, cour.getCategorie());
            pst.setString(6, cour.getPdfpath());
            pst.setInt(7, cour.getId());
            pst.executeUpdate();
        }catch (SQLException e){
            System.out.println("Erreur:"+e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        try {
            String query = "DELETE FROM `cour` WHERE id=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, id);
            pst.executeUpdate();
        }catch (SQLException e){
            System.out.println("Erreur:"+e.getMessage());
        }
    }

    @Override
    public List<Cour> afficher() {
        List<Cour> lc=new ArrayList<>();
        try {

            String query = "SELECT * FROM cour";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Cour c = new Cour();
                c.setId(rs.getInt("id"));
                c.setTitre(rs.getString("titre"));
                c.setDescription(rs.getString("description"));
                c.setCategorie(rs.getString("categorie"));
                c.setNiveau(rs.getString("niveau"));
                c.setPdfpath(rs.getString("pdfpath"));
                c.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());
                lc.add(c);
            }
        }catch (SQLException e){
            System.out.println("Erreur:"+e.getMessage());
        }

        return lc;
    }
    public List<Cour> triParCritere(String critere){

        switch (critere){
            case "Titre":
                return afficher().stream().sorted(Comparator.comparing(Cour::getTitre))
                        .collect(Collectors.toList());
            case "Description":
                return afficher().stream().sorted(Comparator.comparing(Cour::getDescription))
                        .collect(Collectors.toList());
            case "Categorie":
                return afficher().stream().sorted(Comparator.comparing(Cour::getCategorie))
                        .collect(Collectors.toList());
            case "Niveau":
                return afficher().stream().sorted(Comparator.comparing(Cour::getNiveau))
                        .collect(Collectors.toList());
            case "Date":
                return afficher().stream().sorted(Comparator.comparing(Cour::getCreated_at))
                        .collect(Collectors.toList());
            default:
                return afficher();
        }
    }
}
