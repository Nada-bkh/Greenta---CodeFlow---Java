package Services;

import Models.Charity;
import Models.Donation;
import Models.User;
import Utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.time.Instant;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

public class DonationService {
    MyConnection Mycnx = MyConnection.getInstance();
    Connection connection1 = Mycnx.getConnection();

    /*====================Add===============================*/
    public void addDonation(Donation d, Charity charity) {
        String req = "INSERT INTO Donation (first_name, last_name, phone_number, amount, address, date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection1 = MyConnection.getInstance().getConnection();
             PreparedStatement R = connection1.prepareStatement(req)) {


            R.setString(1, d.getFirst_name());
            R.setString(2, d.getLast_name());
            R.setInt(3, d.getPhone_number());
            R.setDouble(4, d.getAmount());
            R.setString(5, d.getAddress());


            LocalDateTime date = d.getDate() != null ? d.getDate() : LocalDateTime.now();
            R.setTimestamp(6, Timestamp.valueOf(date));

            int rowsAffected = R.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Donation added successfully!");
            } else {
                System.out.println("Failed to add donation.");
            }

        } catch (SQLException ex) {
            System.err.println("Error while adding donation : " + ex.getMessage());
        }
    }


    /*====================fetch===============================*/
    public List<Donation> showDonation() {
        List<Donation> donations = new ArrayList<>();
        String req = "SELECT * FROM Donation";
        try {Connection connection1 = MyConnection.getInstance().getConnection();
            Statement st = connection1.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Donation D = new Donation();
                D.setId(rs.getInt(1));
                java.sql.Date  date = rs.getDate("date");
                java.util.Date utilDate = new java.util.Date(date.getTime());
                LocalDateTime localDateTime = utilDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                D.setDate(localDateTime);
                CharityService c = new CharityService();
                Charity charity = c.showCharityById(rs.getInt(4));
                D.setCharity(charity);

                String firstNameString = rs.getString("first_name");
                D.setFirst_name(firstNameString);

                // Set last name
                String lastNameString = rs.getString("last_name");
                D.setLast_name(lastNameString);

                // Set address
                String addressString = rs.getString("address");
                D.setAddress(addressString);

                // Set phone number
                int phoneNumber = rs.getInt("phone_number");
                D.setPhone_number(phoneNumber);

                // Set amount
                double amount = rs.getDouble("amount");
                D.setAmount(amount);


                donations.add(D);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DonationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return donations;




    }
    /*==================delete=================================*/
    public Boolean deleteDonation(int id){
        try
        {
        String req="DELETE FROM Donation WHERE id=" +id;
        Statement stm=connection1.createStatement();
        stm=connection1.createStatement();
        int rowsDeleted= stm.executeUpdate(req);
        if(rowsDeleted>0){
            System.out.println("donation with id= " +id+ "id deleted");
            return true;
        }
        else{
            System.out.println("Failed to delete donation with ID " + id + ".");
            return false;
        }

    }
        catch (SQLException ex){
            System.err.println(ex.getMessage());
        }
        return false;
    }
    /*==================Donation all count=================================*/
    public Integer donationAllCount(){
        int totalDonationCount=0;
        String sql="SELECT COUNT(id) AS totalDonationCount FROM Donation";
        try (PreparedStatement statement=connection1.prepareStatement(sql)){
            try (ResultSet rs=statement.executeQuery()){
                if(rs.next()){
                    totalDonationCount=rs.getInt("totalDonationCount");
                }

            }


        }
        catch (SQLException ex){
            Logger.getLogger(DonationService.class.getName()).log(Level.SEVERE,null,ex);
        }
        return totalDonationCount;
    }



}






