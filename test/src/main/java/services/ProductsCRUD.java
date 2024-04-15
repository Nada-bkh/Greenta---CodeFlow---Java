package services;

import entities.Products;
import utils.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductsCRUD {



    // add product Méthode Statique **********************************
    public void addProducts(){
        try {
            String requete="INSERT INTO Product(product_name,product_quantity,product_size," +
                "product_price,product_description,product_disponibility,product_image) " +
                "VALUES('Scarf','6','Small','19.99','Best shorts','Available','img8')  ";

            Statement st = new MyConnection().getCnx().createStatement();
           //executeUpdate pour req INSERT / executeQuery pour SELECT
            st.executeUpdate(requete);
            System.out.println("Product added successfuly !! ");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

    }

    // add product Méthode Dynamique ********************************
    public void addProducts2(Products P){
        try {
            String requete2="INSERT INTO Product(product_name,product_quantity,product_size," +
                "product_price,product_description,product_disponibility,product_image) " +
                "VALUES(?,?,?,?,?,?,?)";

            PreparedStatement pst= new MyConnection().getCnx().prepareStatement(requete2);

            pst.setString(1,P.getProduct_name());
            pst.setInt(2,P.getProduct_quantity());
            pst.setString(3,P.getProduct_size());
            pst.setFloat(4,P.getProduct_price());
            pst.setString(5,P.getProduct_description());
            pst.setString(6,P.getProduct_disponibility());
            pst.setString(7,P.getProduct_image());

            pst.executeUpdate();
            System.out.println("Product added Lesggoooo !!!");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

    }

    // Show products list *********************************************
    public List<Products> showProducts(){

        List<Products> myList=new ArrayList<>();

        try {
        String requete3="SELECT * FROM product";
        Statement st = new MyConnection().getCnx().createStatement();
        ResultSet rs=st.executeQuery(requete3);

        while(rs.next()){
            Products P=new Products();
            P.setProduct_id(rs.getInt(1));
            P.setProduct_name(rs.getString("product_name"));
            P.setProduct_quantity(rs.getInt("product_quantity"));
            P.setProduct_size(rs.getString("product_size"));
            P.setProduct_price(rs.getFloat("product_price"));
            P.setProduct_description(rs.getString("product_description"));
            P.setProduct_disponibility(rs.getString("product_disponibility"));
            P.setProduct_image(rs.getString("product_image"));

            myList.add(P);

        }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return myList;
    }


    // Update product **********************************
    public void updateProducts(){
        try {
            String requete4="UPDATE product " +
                    "SET product_size = 'XL' WHERE product_name = 'Slippers'";
            Statement st = new MyConnection().getCnx().createStatement();
            //executeUpdate pour req INSERT / executeQuery pour SELECT
            st.executeUpdate(requete4);
            System.out.println("Product updated successfuly !! ");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

    }


    // Delete product **********************************
    public void deleteProducts(){
        try {
            String requete5="DELETE FROM product WHERE product_name = 'Hoodie'";

            Statement st = new MyConnection().getCnx().createStatement();
            //executeUpdate pour req INSERT / executeQuery pour SELECT
            st.executeUpdate(requete5);
            System.out.println("Product deleted successfuly !! ");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

    }

    // Search product **********************************
    public List<Products> searchByName(String product_name) {
             List<Products> products = new ArrayList<>();
             try {
                 String request = "SELECT * FROM Product WHERE product_name = ?";
                 PreparedStatement pst= new MyConnection().getCnx().prepareStatement(request);
                 pst.setString(1, product_name);
                 ResultSet rs = pst.executeQuery();
                 while (rs.next()) {
                     Products P = new Products();
                     P.setProduct_id(rs.getInt("product_id"));
                     P.setProduct_name(rs.getString("product_name"));
                     P.setProduct_quantity(rs.getInt("product_quantity"));
                     P.setProduct_size(rs.getString("product_size"));
                     P.setProduct_price(rs.getFloat("product_price"));
                     P.setProduct_description(rs.getString("product_description"));
                     P.setProduct_disponibility(rs.getString("product_disponibility"));
                     P.setProduct_image(rs.getString("product_image"));

                     products.add(P);
                 }
             } catch (SQLException e) {
                 e.printStackTrace();
             }
        return products;
         }

}
