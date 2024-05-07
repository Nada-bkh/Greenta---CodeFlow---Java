package com.example.greenta.Services;

import com.example.greenta.Interfaces.IApp;
import com.example.greenta.Models.App;
import com.example.greenta.Utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;

public class ServiceApp implements IApp<App> {

    Connection connection = MyConnection.getInstance().getConnection();

    @Override
    public void add(App app) {
        String qry = "INSERT INTO Application (firstname, lastname, email, pdf) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstm = connection.prepareStatement(qry);
            pstm.setString(1, app.getFirstname());
            pstm.setString(2, app.getLastname());
            pstm.setString(3, app.getEmail());
            pstm.setString(4, app.getPdf());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<App> getAll() {
        ArrayList<App> apps = new ArrayList<>();
        String qry = "SELECT * FROM `Application`";
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                App p = new App();
                p.setId(rs.getInt("id"));
                p.setFirstname(rs.getString("firstname"));
                p.setLastname(rs.getString("lastname"));
                p.setEmail(rs.getString("Email"));
                p.setPdf(rs.getString("Pdf"));

                apps.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return apps ;
    }

    @Override
    public void update(App app) {

    }

    @Override
    public boolean delete(App app) {
        return false;
    }
}