package tn.esprit.greenta.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    private static final String URL="jdbc:mysql://localhost:3306/greenta";
    private static final String USER="root";
    private static final String PWD="";
    private Connection cnx;
    private static MyDataBase instance;
    private MyDataBase(){
        try{
            cnx=DriverManager.getConnection(URL,USER,PWD);
            System.out.println("connecter");
        }catch (SQLException e){
            System.out.println("Erreur:"+e.getMessage());
        }

    }
    public static MyDataBase getInstance(){
        if(instance==null){
            instance=new MyDataBase();
        }
        else{
            System.out.println("Deja connecter!");
        }
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }
}
