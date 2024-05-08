package com.example.greenta.Services;

import com.example.greenta.Models.Product;
import com.example.greenta.Utils.MyConnection;

import java.sql.Connection;
import java.sql.SQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductService {


    public static ProductService instance;
    Connection connection = MyConnection.getInstance().getConnection();

    public static ProductService getInstance() throws SQLException {
        if (instance == null) {
            instance = new ProductService();
        }
        return instance;
    }

    public void addProduct(Product p) {
        try {
            String request = "INSERT INTO product (productname, productquantity, productsize, productprice, productdescription, productdisponibility, productimg) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(request);
            pst.setString(1, p.getProduct_name());
            pst.setInt(2, p.getProduct_quantity());
            pst.setString(3, p.getProduct_size());
            pst.setFloat(4, p.getProduct_price());
            pst.setString(5, p.getProduct_description());
            pst.setString(6, p.getProduct_disponibility());
            pst.setString(7, p.getProduct_image());
            //pst.setInt(8, p.getProductcategory_id());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Product> AfficherProduct() {
    List<Product> products = new ArrayList<>();
    try {
        String request = "SELECT * FROM product";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(request);
        while (rs.next()) {
            Product p = new Product();
            p.setProduct_id(rs.getInt("product_id"));
            p.setProduct_name(rs.getString("product_name"));
            p.setProduct_description(rs.getString("product_description"));
            p.setProduct_disponibility(rs.getString("product_disponibility"));
            p.setProduct_quantity(rs.getInt("product_quantity"));
            p.setProduct_price(rs.getFloat("product_price"));
            p.setProduct_size(rs.getString("product_size"));
            p.setProduct_image(rs.getString("product_image"));
            //p.setProductcategory_id(rs.getInt("productcategory_id"));

            products.add(p);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return products;
}

    public List<Product> getProductsForProductCategory(int productcategoryId) {
        List<Product> products = new ArrayList<>();
        try {
            String request = "SELECT * FROM product WHERE id = ?";
            PreparedStatement pst = connection.prepareStatement(request);
            pst.setInt(1, productcategoryId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setProduct_id(rs.getInt("product_id"));
                p.setProduct_name(rs.getString("product_name"));
                p.setProduct_description(rs.getString("product_description"));
                p.setProduct_disponibility(rs.getString("product_disponibility"));
                p.setProduct_quantity(rs.getInt("product_quantity"));
                p.setProduct_price(rs.getFloat("product_price"));
                p.setProduct_size(rs.getString("product_size"));
                p.setProduct_image(rs.getString("product_image"));
                //p.setProductcategory_id(rs.getInt("productcategory_id"));
                products.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public Product getProductById(int id) {
        Product product = null;
        try {
            String request = "SELECT * FROM product WHERE product_id = ?";
            PreparedStatement pst = connection.prepareStatement(request);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                product = new Product();
                product.setProduct_id(rs.getInt("product_id"));
                product.setProduct_name(rs.getString("product_name"));
                product.setProduct_description(rs.getString("product_description"));
                product.setProduct_disponibility(rs.getString("product_disponibility"));
                product.setProduct_quantity(rs.getInt("product_quantity"));
                product.setProduct_price(rs.getFloat("product_price"));
                product.setProduct_size(rs.getString("product_size"));
                product.setProduct_image(rs.getString("product_image"));
                //product.setProductcategory_id(rs.getInt("productcategory_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }


}