package com.example.greenta.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    public String url="jdbc:mysql://localhost:3306/greenta?useSSL=false&serverTimezone=UTC";
    public String login="root";
    public String pwd="";
    public Connection connection;

    public static MyConnection instance;

    public  MyConnection(){
        try {
            connection = DriverManager.getConnection(url ,login ,pwd);
            System.out.println("You have been successfully connected to the database !");

        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static MyConnection getInstance(){
        if(instance == null){
            instance = new MyConnection();
        }
        return instance;
    }

}
