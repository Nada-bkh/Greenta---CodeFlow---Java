package Services;

import Models.Charity;
import Models.Donation;
import Utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;



public class CharityService {
    MyConnection Mycnx=MyConnection.getInstance();
    Connection connection=Mycnx.getConnection();

    /*===================================================*/
    public boolean charityExists(String  name_of_charity){
        boolean charityExists=false;
        String query ="SELECT COUNT(*) FROM Charity WHERE  name_of_charity=?";
        try
        {
            PreparedStatement R=connection.prepareStatement(query);
            R.setString(1,name_of_charity);
            ResultSet resultSet=R.executeQuery();
            if(resultSet.next())
            {int count=resultSet.getInt(1);
                charityExists=(count>0);}


        } catch (SQLException ex){
            Logger.getLogger((CharityService.class.getName())).log(Level.SEVERE,null,ex);
        }
        return charityExists;
    }
    /*=====================ADD============================*/
    public void addCharity(Charity c) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        String dateDebFormatted = dateFormat.format(c.getLast_date());
        String req = "INSERT INTO Charity (name_of_charity, amount_donated, location, picture, last_date) VALUES ('" + c.getName_of_charity() + "','" + c.getAmount_donated() + "','" + c.getLocation() + "','" + c.getPicture() + "','" + dateDebFormatted + "')";
        try (PreparedStatement st =connection.prepareStatement(req)){
            Statement st1 = connection.createStatement();
            if (!charityExists(c.getName_of_charity())) {
                st.executeUpdate(req);
                System.out.println("Charity added successfully!");
            } else {
                System.out.println("Charity already exists");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CharityService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public List<Charity> showCharity(){
        List<Charity> charities=new ArrayList<>();
        String req = "SELECT id, amount_donated, name_of_charity, picture, last_date, location FROM Charity";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                int id = rs.getInt("id"); // Retrieve the id value from the ResultSet
                System.out.println("Retrieved id from database: " + id); // Print the retrieved id value
                Charity c = new Charity();
                c.setId(rs.getInt("id"));
                c.setName_of_charity(rs.getString("name_of_charity"));
                c.setAmount_donated(rs.getDouble("amount_donated"));
                c.setLocation(rs.getString("location"));
                c.setPicture(rs.getString("picture"));
                c.setLast_date(rs.getDate("last_date"));
                charities.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CharityService.class.getName()).log(Level.SEVERE, "Error retrieving charities", ex);
        }
        return charities;
    }
    /*====================delete===============================*/
    public boolean deleteCharity(int id)
    {
        try {
           /* String deleteDonationsSQL = "DELET FROM Donation WHERE id=?";
            PreparedStatement deleteDonationsStmt = connection.prepareStatement(deleteDonationsSQL);
            deleteDonationsStmt.setInt(1, id);
            int rowDeleteDonations = deleteDonationsStmt.executeUpdate();*/
            String deleteCharitySQL = "DELETE FROM Charity WHERE id=?";
            PreparedStatement deleteCharityStmt = connection.prepareStatement(deleteCharitySQL);
            deleteCharityStmt.setInt(1, id);
            int rowDeletCharity = deleteCharityStmt.executeUpdate();
            if (/*rowDeleteDonations > 0 ||*/ rowDeletCharity > 0) {
                System.out.println("deleted successfully");
                return true;
            }
        }
        catch (SQLException ex){
            System.err.println(ex.getMessage());}
        return false;
        }


/*======================update=============================*/

    public void update(Charity c, int idCharity) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateDebFormatted = dateFormat.format(c.getLast_date());
        String req = "UPDATE Charity SET name_of_charity='" + c.getName_of_charity() + "', "+
                "amount_donated= " + c.getAmount_donated() + ", "+
                "location= '" + c.getLocation() + "', "+
                "picture='" + c.getPicture() + "', "+
                "last_date='" + dateDebFormatted + "' " +
                "WHERE id=" + idCharity;
        try {
            Statement stm = connection.createStatement();
            int rowsUpdated = stm.executeUpdate(req);
            if (rowsUpdated > 0) {
                System.out.println("Updated successfully.");
            } else {
                System.out.println("No updates performed. There's no charity with this id: " + idCharity);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CharityService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

        public Charity showCharityById(int id){
            String req="SELECT * FROM Charity WHERE id="+id;
            try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(req);
                if (rs.next()) {
                    Charity c = new Charity();
                    c.setId(rs.getInt("id"));
                    c.setName_of_charity(rs.getString("name_of_charity"));
                    c.setAmount_donated(rs.getDouble("amount_donated"));
                    c.setLocation(rs.getString("location"));
                    c.setPicture(rs.getString("picture"));
                    c.setLast_date(rs.getDate("last_date"));
                    return c;
                } else {
                    System.out.println("Charity with id =" + id + "doesn't exist");
                    return null;
                }
            } catch (SQLException ex){
                Logger.getLogger((Charity.class.getName())).log(Level.SEVERE,null,ex);
            }
            return null;
            }

            public Integer donationCount(int id){
        int totalDonationCount=0;
        String sql ="SELECT COUNT(d.id) AS totalDonationCount FROM Donation d WHERE d.id=?";
        try (PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setInt(1,id);
            try (ResultSet rs=statement.executeQuery()){
                if(rs.next()){
                    totalDonationCount=rs.getInt("totalDonationCount");
                }

            }

        }
        catch (SQLException ex){
            Logger.getLogger(CharityService.class.getName()).log(Level.SEVERE,null,ex);
        }
        return totalDonationCount;
            }
    /*====================Charity with most donation===============================*/
        public void charityWithMostDonation(List<Charity> charities){
            int maxDonationCount=0;
            int lastDonationCount=0;
            Charity charityWithLastDonations=null;
            boolean test=false;
            for(Charity charity:charities){
                int NBdonation=donationCount(charity.getId());
                if(NBdonation>maxDonationCount){
                    maxDonationCount=NBdonation;
                }
                if (NBdonation>=lastDonationCount){
                    lastDonationCount=NBdonation;
                    charityWithLastDonations= charity;

                }
            }
            System.out.println("charity with the most donations: "+ charityWithLastDonations.getName_of_charity());
            for (Charity charity:charities){
                if(charity !=charityWithLastDonations){
                    if(!test){
                        System.out.println("Charity with the least donations: "+charity.getName_of_charity());
                        test=true;
                    }
                }
            }

        }
        public List<Charity> orderCharitiesByDonationCount(){
            List<Charity>charities=showCharity();
            charities.sort(Comparator.comparingInt(o->donationCount((o.getId()))));
            Collections.reverse(charities);
            return charities;
        }
}






