package com.example.project;
import java.awt.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import java.awt.image.BufferedImage;
import com.example.project.Connections.MyConnection;
import com.example.project.entities.ProductCategory;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.embed.swing.SwingFXUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.example.project.entities.Product;
import com.example.project.Connections.Connect;
import com.example.project.Connections.MyConnection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

import javax.imageio.ImageIO;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductCategoryController implements Initializable {


    @FXML
    private Button addbutton;
    @FXML
    private Button upload;

    @FXML
    private Button deletebutton;

    @FXML
    private TableColumn<ProductCategory,Integer> productcategory_id;

    @FXML
    private TableColumn<ProductCategory,String> productcategory_image;

    @FXML
    private TableColumn<ProductCategory,String> productcategory_name;

    @FXML
    private TextField productcategoryimage;

    @FXML
    private TextField productcategoryname;

    @FXML
    private TableView<ProductCategory> table;

    private ObservableList<ProductCategory> productcategoryList; // List to hold the products

    Connect connect = new Connect();
    private MyConnection connection; // Database connection
    private String imagePath = null; // Variable to store the image path
    @FXML
    private Button updatebutton;
    private ProductCategory productcategory;

    //Add product-----------------------------------------------------------------------------
    @FXML
    public void addbutton(ActionEvent event) {
        String productcategory_name;

        productcategory_name = productcategoryname.getText();
       // productcategory_image = productcategoryimage.getText();
        String productcategory_image = imagePath;

        // Validate input fields
        if (productcategory_name.isEmpty() ) {
            System.out.println("Name Field cannot be empty!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Name Field cannot be empty!");

            alert.setHeaderText("Name Field cannot be empty! ");
            alert.setContentText("Name Field cannot be empty!");

            alert.showAndWait();
            return;

        }
        if ( productcategory_image.isEmpty()) {
            System.out.println("Image Field cannot be empty!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Image Field cannot be empty!");

            alert.setHeaderText("Image Field cannot be empty! ");
            alert.setContentText("Image Field cannot be empty!");

            alert.showAndWait();
            return;

        }

        // Validate name (contains only letters)
        if (!productcategory_name.matches("[a-zA-Z]+")) {
            System.out.println("Product category name can only contain letters!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Product category name can only contain letters!");

            alert.setHeaderText("Product category name can only contain letters!");
            alert.setContentText("Product category name can only contain letters!");
            return;
        }

        try {
            PreparedStatement pst = connect.con.prepareStatement("INSERT INTO productcategory (productcategory_name,productcategory_image) " +
                    "VALUES (?, ?)");
            pst.setString(1, String.valueOf(productcategory_name));
            pst.setString(2, String.valueOf(productcategory_image));
            pst.executeUpdate();

            // Refresh the table
            loadProductCategories();
            table.refresh();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Product Category Add");

            alert.setHeaderText("Add a product category ! ");
            alert.setContentText("Product category added successfully !!!!");

            alert.showAndWait();
        } catch (SQLException ex) {
            Logger.getLogger(ProductCategoryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void loadProductCategories() {
        try {
            Connection conn = connection.getCnx();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM productcategory");
            ResultSet rs = stmt.executeQuery();

            productcategoryList.clear(); // Move this line here

            while (rs.next()) {
                int productcategory_id = rs.getInt("productcategory_id");
                String productcategory_name = rs.getString("productcategory_name");
                String productcategory_image = rs.getString("productcategory_image");

                ProductCategory productcategory = new ProductCategory(productcategory_id, productcategory_name, productcategory_image);
                productcategoryList.add(productcategory);
            }

            // Don't close the connection here
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //delete-------------------------------------------------------
    //Delete product-----------------------------------------------------------------------------
    @FXML
    void deletebutton(ActionEvent event) {
        ProductCategory selectedProductcategory = table.getSelectionModel().getSelectedItem();
        if (selectedProductcategory == null) {
            System.out.println("No item selected!");
            return;
        }

        // Remove the product from the list
        productcategoryList.remove(selectedProductcategory);

        // Refresh the table to reflect the updated data
        table.refresh();

        // Clear the input fields
        clearFields();

        // Create and execute the SQL delete statement
        PreparedStatement pst = null;
        try {
            pst = connect.con.prepareStatement("DELETE FROM productcategory WHERE productcategory_id=?");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            pst.setInt(1, selectedProductcategory.getProductcategory_id());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearFields() {
    }

    @FXML
    void updatebutton(ActionEvent event) throws SQLException {
        // Get the selected item
        ProductCategory selectedProductcategory = table.getSelectionModel().getSelectedItem();

        // Check if an item is selected
        if (selectedProductcategory != null) {
            // Validate the fields
            if (productcategoryname.getText().isEmpty() ||productcategoryimage.getText().isEmpty()) {
                // Display an error message
                System.out.println("All fields must be filled out");
                return;
            }

            // Get the data from the fields and update the selected item with this data
            selectedProductcategory.setProductcategory_name(productcategoryname.getText());
            selectedProductcategory.setProductcategory_image(productcategoryimage.getText());

            // Update the item in the database
            try {
                String updateQuery = "UPDATE productcategory SET productcategory_name = ?,productcategory_image = ? WHERE productcategory_id = ?";
                PreparedStatement pst = new MyConnection().getCnx().prepareStatement(updateQuery);
                pst.setString(1, selectedProductcategory.getProductcategory_name());
                pst.setString(2, selectedProductcategory.getProductcategory_image());
                pst.setInt(3, selectedProductcategory.getProductcategory_id());

                pst.executeUpdate();
                System.out.println("Product category updated successfully !! ");

            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }

            // Refresh the table
            table.refresh();

            // Reselect the item
            table.getSelectionModel().select(selectedProductcategory);

            // Clear the input fields
            clearFields();
        }
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = new MyConnection();
        productcategoryList = FXCollections.observableArrayList();

        // Set up the table columns
        productcategory_id.setCellValueFactory(new PropertyValueFactory<>("productcategory_id"));
        productcategory_name.setCellValueFactory(new PropertyValueFactory<>("productcategory_name"));
        productcategory_image.setCellValueFactory(new PropertyValueFactory<>("productcategory_image"));

        // Make the table editable
        table.setEditable(true);
        productcategory_name.setCellFactory(TextFieldTableCell.forTableColumn());
        productcategory_image.setCellFactory(TextFieldTableCell.forTableColumn());

        // Load the products from the database
        loadProductCategories();
        table.refresh();

        // Set the table data
        table.setItems(productcategoryList);

        // Add a listener to the selectedItemProperty of the TableView
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Populate the fields with the data of the new selected item
                productcategoryname.setText(newSelection.getProductcategory_name());
                // Load the image using ImageView
                try {
                    String imagePath = newSelection.getProductcategory_image();
                    File imageFile = new File(imagePath);
                    Image newImage = new Image(imageFile.toURI().toString());
                    image.setImage(newImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @FXML
    private ImageView image;

    @FXML
    void upload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(new Stage()); // Pass a stage here

        if (selectedFile != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(selectedFile);
                WritableImage fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
                image.setImage(fxImage); // Set the image to the ImageView
                imagePath = selectedFile.getAbsolutePath(); // Store the image path
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No file selected");
        }
    }


    }


