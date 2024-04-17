package com.example.project.Connections;

import java.sql.Connection;
import java.sql.DriverManager ;
import java.sql.SQLException;

public class MyConnection {

    public String url="jdbc:mysql://root:@127.0.0.1:3306/greenta";
    public String login="root";
    public String pwd="";
    Connection cnx ;


    public MyConnection() {
        try {
            cnx = DriverManager.getConnection(url,login,pwd);
            System.out.println("!!! CONNECTED SUCCESSFULLY BIIIAAATTCHHH !!!");
        } catch(SQLException ex) {
            System.err.println(ex.getMessage());

        }

    }
    public Connection getCnx() {
        return cnx ;

    }

}

