package com.example.greenta.Services;


import com.example.greenta.Models.ProductCategories;
import com.example.greenta.Utils.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryCRUD {
    // add product category  ***********************************************
    public void addProductCategory(ProductCategories productCategory) {
        try {
            String requete = "INSERT INTO productcategory(productcategory_name,productcategory_image) VALUES(?,?)  ";
            PreparedStatement pst = new MyConnection().getConnection().prepareStatement(requete);

            pst.setString(1, productCategory.getProductcategory_name());
            pst.setString(2, productCategory.getProductcategory_image());
            pst.executeUpdate();

            System.out.println("Product Category added successfully !! ");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }


        // Show product category list *********************************************
            public List<ProductCategories> showProductCategory() {

                List<ProductCategories> myList = new ArrayList<>();

                try {
                    String requete1 = "SELECT * FROM productcategory";
                    Statement st = new MyConnection().getConnection().createStatement();
                    ResultSet rs = st.executeQuery(requete1);

                    while (rs.next()) {
                        ProductCategories P = new ProductCategories();
                        P.setProductcategory_id(rs.getInt(1));
                        P.setProductcategory_name(rs.getString("productcategory_name"));
                        P.setProductcategory_image(rs.getString("productcategory_image"));

                        myList.add(P);

                    }

                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }
                return myList;
            }
    // update product category **********************************
    public void updateProductCategory() {
        try {
            String requete2= "UPDATE productcategory " +
                    "SET productcategory_image = 'Test' WHERE productcategory_name = 'Animals'";

            Statement st = new MyConnection().getConnection().createStatement();
            //executeUpdate pour req INSERT / executeQuery pour SELECT
            st.executeUpdate(requete2);
            System.out.println("Product Category updated successfuly !! ");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    // Delete product category **********************************
    public void deleteProductCategory() {
        try {
            String requete3= "DELETE FROM productcategory WHERE productcategory_name = 'Animals'";

            Statement st = new MyConnection().getConnection().createStatement();
            //executeUpdate pour req INSERT / executeQuery pour SELECT
            st.executeUpdate(requete3);
            System.out.println("Product Category deleted successfuly !! ");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    // Search product category **********************************
   public List<ProductCategories> searchByName(String productcategory_name) {
    List<ProductCategories> productCategories = new ArrayList<>();
    try {
        String request = "SELECT * FROM productcategory WHERE productcategory_name = ?";
        PreparedStatement pst= new MyConnection().getConnection().prepareStatement(request);
        pst.setString(1, productcategory_name);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            ProductCategories PC = new ProductCategories();
            PC.setProductcategory_id(rs.getInt("productcategory_id"));
            PC.setProductcategory_name(rs.getString("productcategory_name"));
            PC.setProductcategory_image(rs.getString("productcategory_image"));

            productCategories.add(PC);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return productCategories;
}

}



