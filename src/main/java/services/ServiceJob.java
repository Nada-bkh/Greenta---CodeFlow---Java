package services;

import greenta.Connectors.MyDataBase;
import greenta.models.Job;
import interfaces.IJob;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ServiceJob implements IJob<Job> {

    private Connection cnx;

    public ServiceJob() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(Job job) {
        String qry = "INSERT INTO job (organisation, title, description, startdate, picture) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, job.getOrganisation());
            pstm.setString(2, job.getTitle());
            pstm.setString(3, job.getDescription());
            LocalDate startDate = job.getStartdate();
            pstm.setDate(4, startDate != null ? Date.valueOf(startDate) : null);
            pstm.setString(5, job.getPicture());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Job> getAll() {
        ArrayList<Job> jobs = new ArrayList<>();
        String qry = "SELECT * FROM `job`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                Job p = new Job();
                p.setId(rs.getInt("id"));
                p.setOrganisation(rs.getString("organisation"));
                p.setTitle(rs.getString("title"));
                p.setDescription(rs.getString("description"));
                p.setPicture(rs.getString("picture"));
                p.setStartdate(rs.getDate("startdate").toLocalDate());
                jobs.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return jobs;
    }

    @Override
    public void update(Job job) {
        // Implement update logic here
    }

    @Override
    public boolean delete(Job job) {
        // Implement delete logic here
        return false;
    }
}
