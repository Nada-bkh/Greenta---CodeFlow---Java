package com.example.greenta.GUI;

import com.example.greenta.Models.Product;
import com.example.greenta.Utils.MyConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ShopController implements Initializable {


    @FXML
    private ImageView BOImage;

    @FXML
    private Label backOfficeButton;

    @FXML
    private Label charityLabel;

    @FXML
    private Label coursesLabel;

    @FXML
    private Label eventLabel;

    @FXML
    private Label homeLabel;


    @FXML
    private Label recruitmentLabel;

    @FXML
    private Label shopLabel;


    @FXML
    private FlowPane productsFlowPane; // This is the container for product cards

    @FXML
    private AnchorPane productsAnchorPane; // This is the container for product cards

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Adjust layout properties of productsPane
        productsFlowPane.setHgap(10); // Horizontal gap between children
        productsFlowPane.setVgap(10); // Vertical gap between children
        productsFlowPane.setPrefWrapLength(600); // Preferred width of the pane
        productsFlowPane.setPrefHeight(400); // Preferred height of the pane
        loadProductsFromDatabase();
    }

    private void loadProductsFromDatabase() {
        try {
            Connection connection = MyConnection.getInstance().getCnx();
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM product";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Product product = new Product();
                product.setProduct_id(resultSet.getInt("product_id"));
                product.setProduct_name(resultSet.getString("product_name"));
                product.setProduct_quantity(resultSet.getInt("product_quantity"));
                product.setProduct_size(resultSet.getString("product_size"));
                product.setProduct_price(resultSet.getFloat("product_price"));
                product.setProduct_description(resultSet.getString("product_description"));
                product.setProduct_disponibility(resultSet.getString("product_disponibility"));
                product.setProduct_image(resultSet.getString("product_image"));

                // Create and add product card to the productsPane
                ProductCardController cardController = new ProductCardController();
                productsFlowPane.getChildren().add(cardController.createProductCard(product));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void backOffice(MouseEvent event) {

    }

    @FXML
    void charityButton(MouseEvent event) {

    }

    @FXML
    void coursesButton(MouseEvent event) {

    }

    @FXML
    void eventButton(MouseEvent event) {

    }

    @FXML
    void homeButton(MouseEvent event) {

    }

    @FXML
    void recruitmentButton(MouseEvent event) {

    }

    @FXML
    void shopButton(MouseEvent event) {

    }

    @FXML
    void signOut(MouseEvent event) {

    }




}
