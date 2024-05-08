package com.example.greenta.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {

    private String url = "jdbc:mysql://root:@127.0.0.1:3306/greenta";
    private String login = "root";
    private String pwd = "";
    private Connection cnx;
    private static MyConnection instance;

    private MyConnection() {
        // Private constructor to prevent instantiation
    }

    public static MyConnection getInstance() {
        if (instance == null) {
            instance = new MyConnection();
        }
        return instance;
    }

    public Connection getCnx() {
        if (cnx == null) {
            try {
                cnx = DriverManager.getConnection(url, login, pwd);
                System.out.println("!!! CONNECTED SUCCESSFULLY BIIIAAATTCHHH !!!");
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return cnx;
    }
}
