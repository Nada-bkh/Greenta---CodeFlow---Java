package com.example.greenta.GUI;

import com.example.greenta.Models.Product;
import com.example.greenta.Models.Sale;
import com.example.greenta.Utils.MyConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ProductCardController {

    @FXML
    private Button BUY;
    @FXML
    private Label product_nametxt;
    @FXML
    private Label product_pricetxt;
    @FXML
    private Label product_descriptiontxt;
    @FXML
    private Label product_sizetxt;
    @FXML
    private ImageView product_imagetxt;

    private Product product; // Reference to the associated Product object

    public AnchorPane createProductCard(Product product) {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/ProductCard.fxml"));
        try {
            AnchorPane card = loader.load();
            ProductCardController cardController = loader.getController();
            cardController.setProductData(product);
            cardController.setProduct(product); // Set the associated Product object
            return card;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setProductData(Product product) {
        this.product = product; // Set the associated Product object
        product_nametxt.setText(product.getProduct_name());
        product_pricetxt.setText(String.valueOf(product.getProduct_price()));
        product_descriptiontxt.setText(product.getProduct_description());
        product_sizetxt.setText(product.getProduct_size());

        try {
            // Convert local file path to URL format
            String imagePath = "file:///" + product.getProduct_image().replace("\\", "/");
            // Load product image if the URL is valid
            Image image = new Image(imagePath);
            product_imagetxt.setImage(image);
        } catch (IllegalArgumentException e) {
            // Handle invalid URL or missing image file
            System.err.println("Invalid URL for product image: " + product.getProduct_image());
            e.printStackTrace();
        }
    }

    //--------------------------------BUY---------------------------------//
    @FXML
    void BUY(ActionEvent event) {
        System.out.println("BUY");
        // Decrement product quantity and create a new sale
        int newQuantity = product.getProduct_quantity() - 1;
        if (newQuantity >= 0) {
            updateProductAndCreateSale(product.getProduct_id(), newQuantity);
        } else {
            System.out.println("Product quantity cannot be negative");
        }
    }

    private void updateProductAndCreateSale(int productId, int newQuantity) {
        String updateQuery = "UPDATE product SET productquantity = ? WHERE id = ?";
        String checkSaleQuery = "SELECT * FROM sale WHERE product_id = ?";
        String insertQuery = "INSERT INTO sale (nbr_vente, product_id) VALUES (?, ?)";
        String updateSaleQuery = "UPDATE sale SET nbr_vente = nbr_vente + 1 WHERE product_id = ?";

        try (Connection connection = MyConnection.getInstance().getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
             PreparedStatement checkSaleStatement = connection.prepareStatement(checkSaleQuery);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
             PreparedStatement updateSaleStatement = connection.prepareStatement(updateSaleQuery)) {

            // Update product quantity
            updateStatement.setInt(1, newQuantity);
            updateStatement.setInt(2, productId);
            int updateRowsAffected = updateStatement.executeUpdate();

            // Check if sale record exists for the product
            checkSaleStatement.setInt(1, productId);
            ResultSet resultSet = checkSaleStatement.executeQuery();

            if (resultSet.next()) {
                // Sale record exists, update the nbr_vente column
                updateSaleStatement.setInt(1, productId);
                int updateSaleRowsAffected = updateSaleStatement.executeUpdate();
                if (updateSaleRowsAffected > 0) {
                    System.out.println("Product quantity updated successfully and sale incremented successfully");
                } else {
                    System.out.println("Failed to update sale");
                }
            } else {
                // Sale record does not exist, insert a new sale record
                insertStatement.setInt(1, 1); // Assuming one item is sold
                insertStatement.setInt(2, productId);
                int insertRowsAffected = insertStatement.executeUpdate();

                if (insertRowsAffected > 0) {
                    System.out.println("Product quantity updated successfully and new sale created successfully");
                } else {
                    System.out.println("Failed to insert new sale");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    // Method to set the associated Product object
    public void setProduct(Product product) {
        this.product = product;
    }
}
