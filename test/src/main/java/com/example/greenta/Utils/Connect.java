/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.greenta.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class provides a database connection to the application
 */
public class Connect {
    public Connection con;
    public PreparedStatement pst;

    
    /**
     * Constructor for the Connect class
     */
    public Connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/greenta","root","");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Returns a PreparedStatement object for the given SQL query
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return con.prepareStatement(sql);
    }
}