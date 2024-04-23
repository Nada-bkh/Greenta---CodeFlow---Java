package Greenta.Services;

import Greenta.Interfaces.IJob;
import Greenta.Utils.MyConnection;
import Greenta.Models.Job;

import java.sql.*;
import java.util.ArrayList;

public class ServiceJob implements IJob<Job> {

    private Connection cnx ;

    public ServiceJob(){
        cnx = MyConnection.getInstance().getMyConnection();
    }
    @Override
    public void add(Job job) {
        //1-req sql INSERT
        //2-executer req
        String qry ="INSERT INTO `job`(`organisation`, `title`, `description`, `startdate`, `picture`) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement pstm =cnx.prepareStatement(qry);


            pstm.setInt(1,job.getId());
            pstm.setString(2,job.getOrganisation());
            pstm.setString(3,job.getTitle());
            pstm.setString(4,job.getDescription());
            pstm.setDate(5,job.getStartdate());
            pstm.setString(6,job.getPicture());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }





    @Override
    public ArrayList<Job> getAll() {
        //1-req SELECT
        //2-recuperation de la base de donné remplissage dans Array
        //3-retour du tableau done
        ArrayList<Job> jobs = new ArrayList<>();
        String qry ="SELECT * FROM `job`";
        try {
            Statement stm = cnx.createStatement();

            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()){
                Job p = new Job();
                p.setId(rs.getInt(1));
                p.setOrganisation(rs.getString(""));
                p.setTitle(rs.getString(""));
                p.setDescription(rs.getString(""));
                p.setPicture(rs.getString(""));
                p.setStartdate(Date.valueOf(rs.getDate("").toLocalDate()));

                jobs.add(p);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return jobs;
    }

    @Override
    public void update(Job job) {

    }

    @Override
    public boolean delete(Job job) {
        return false;
    }


}

