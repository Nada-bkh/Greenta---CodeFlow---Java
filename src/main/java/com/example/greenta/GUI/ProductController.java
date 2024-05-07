package com.example.greenta.GUI;


import com.example.greenta.Models.Product;
import com.example.greenta.Utils.MyConnection;
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

import java.net.URL;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductController implements  Initializable {
    private Product product;

    @FXML
    private Button addbutton;

    @FXML
    private Button deletebutton;

    @FXML
    private Button updatebutton;

    @FXML
    private TableColumn<Product, String> product_description;

    @FXML
    private TableColumn<Product, String> product_disponibility;

    @FXML
    private TableColumn<Product, Integer> product_id;

    @FXML
    private TableColumn<Product, String> product_image;

    @FXML
    private TableColumn<Product, String> product_name;

    @FXML
    private TableColumn<Product, Float> product_price;

    @FXML
    private TableColumn<Product, Integer> product_quantity;

    @FXML
    private TableColumn<Product, String> product_size;

    @FXML
    private TextField productdescription;

    @FXML
    private TextField productdisponibility;

    @FXML
    private TextField productimage;

    @FXML
    private TextField productname;

    @FXML
    private TextField productprice;

    @FXML
    private TextField productquantity;

    @FXML
    private TextField productsize;

    @FXML
    private TableView<Product> table;

    private ObservableList<Product> productList; // List to hold the products
    Connection connection = MyConnection.getInstance().getConnection();


    @FXML
    private Label welcomeText;

    //Add product-----------------------------------------------------------------------------
    @FXML
    public void addbutton(ActionEvent event) {
        String product_name, product_size, product_description, product_disponibility, product_image;
        Integer product_quantity = null;
        Float product_price = null;

        product_name = productname.getText();
        product_size = productsize.getText();
        product_description = productdescription.getText();
        product_disponibility = productdisponibility.getText();
        product_image = productimage.getText();

        // Validate input fields
        if (product_name.isEmpty() || product_size.isEmpty() || product_description.isEmpty() || product_disponibility.isEmpty() || product_image.isEmpty()) {
            System.out.println("Fields cannot be empty!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validation");
            alert.setHeaderText("Fields cannot be empty ! ");
            alert.setContentText("All fields must be filled ! ");
            alert.showAndWait();
            return;
        }
        // Validate name (contains only letters)
        if (!product_name.matches("[a-zA-Z]+")) {
            System.out.println("Product name can only contain letters!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validation");
            alert.setHeaderText("Product name can only contain letters! ");
            alert.showAndWait();
            return;
        }

        // Validation product quantity
        try {
            product_quantity = Integer.valueOf(productquantity.getText());
            if (product_quantity <= 0) {
                System.out.println("quantity wrongg !!!!");
                return;
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validation");
            alert.setHeaderText("Quantity cant be null ! ");
            alert.setContentText("Quantity field must be a number !!");
            alert.showAndWait();
            return;
        }

        // Validate size
        List<String> validSizes = Arrays.asList("XS", "S", "M", "L", "XL", "XXL", "XXXL");
        if (!validSizes.contains(product_size.toUpperCase())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validation");
            alert.setHeaderText("Size can only be [XS , S , M , L , XL , XXL, XXXL ] ");
            alert.showAndWait();
            return;
        }

// Parse product price
        try {
            product_price = Float.valueOf(productprice.getText());
            if (product_price <= 0) {
                System.out.println("Price wrongg !!");
                return;
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validation");
            alert.setHeaderText("Price field must be a float ");
            alert.showAndWait();
            return;
        }


        // Validate disponibility
        if (!product_disponibility.equalsIgnoreCase("available") && !product_disponibility.equalsIgnoreCase("unavailable" )&& !product_disponibility.equalsIgnoreCase("Available" )&& !product_disponibility.equalsIgnoreCase("Unavailable" )) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validation");
            alert.setHeaderText("The product is either [Available] or [Unavailable] ! ");
            alert.showAndWait();
            return;
        }


        try {
            PreparedStatement pst = connection.prepareStatement("INSERT INTO product (product_name, product_quantity, product_size, product_price, product_description, product_disponibility, product_image) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)");
            pst.setString(1, product_name);
            if (product_quantity != null) {
                pst.setInt(2, product_quantity);
            } else {
                pst.setNull(2, Types.INTEGER);
            }
            pst.setString(3, product_size);
            if (product_price != null) {
                pst.setFloat(4, product_price);
            } else {
                pst.setNull(4, Types.FLOAT);
            }
            pst.setString(5, product_description);
            pst.setString(6, product_disponibility);
            pst.setString(7, product_image);
            pst.executeUpdate();

            // Refresh the table
            loadProducts();
            table.refresh();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Product Add");
            alert.setHeaderText("Add a product !");
            alert.setContentText("Product added successfully !!!!");
            alert.showAndWait();
        } catch (SQLException ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @FXML
    // Method to load the products from the database
    private void loadProducts() {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM product");
            ResultSet rs = stmt.executeQuery();

            productList.clear(); // Move this line here

            while (rs.next()) {
                int product_id = rs.getInt("product_id");
                String product_name = rs.getString("product_name");
                int product_quantity = rs.getInt("product_quantity");
                String product_size = rs.getString("product_size");
                float product_price = rs.getFloat("product_price");
                String product_description = rs.getString("product_description");
                String product_disponibility = rs.getString("product_disponibility");
                String product_image = rs.getString("product_image");

                Product product = new Product(product_id, product_name, product_quantity, product_size, product_price, product_description, product_disponibility, product_image);
                productList.add(product);
            }

            // Don't close the connection here
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //Update--------------------------------------------------
    @FXML
    void updatebutton(ActionEvent event) throws SQLException {
        // Get the selected item
        Product selectedProduct = table.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("CRUD");
            alert.setHeaderText("Select an item to update ! ");
            alert.showAndWait();
            return;
        }
        // Check if an item is selected
        if (selectedProduct != null) {
            // Validate the fields
            if (productname.getText().isEmpty() ||productquantity.getText().isEmpty()||productsize.getText().isEmpty()||productprice.getText().isEmpty() ||productdescription.getText().isEmpty()||productdisponibility.getText().isEmpty()||productimage.getText().isEmpty()) {
                // Display an error message
                System.out.println("All fields must be filled out");
                return;
            }

            // Get the data from the fields and update the selected item with this data
            selectedProduct.setProduct_name(productname.getText());
            selectedProduct.setProduct_quantity(Integer.parseInt(productquantity.getText()));
            selectedProduct.setProduct_size(productsize.getText());
            selectedProduct.setProduct_price(Float.parseFloat(productprice.getText()));
            selectedProduct.setProduct_description(productdescription.getText());
            selectedProduct.setProduct_disponibility(productdisponibility.getText());
            selectedProduct.setProduct_image(productimage.getText());

            // Update the item in the database
            try {
                String updateQuery = "UPDATE product SET product_name = ?, product_quantity = ?, product_size = ?, product_price = ?, product_description = ?, product_disponibility = ?, product_image = ? WHERE product_id = ?";
                PreparedStatement pst = connection.prepareStatement(updateQuery);
                pst.setString(1, selectedProduct.getProduct_name());
                pst.setInt(2, selectedProduct.getProduct_quantity());
                pst.setString(3, selectedProduct.getProduct_size());
                pst.setFloat(4, selectedProduct.getProduct_price());
                pst.setString(5, selectedProduct.getProduct_description());
                pst.setString(6, selectedProduct.getProduct_disponibility());
                pst.setString(7, selectedProduct.getProduct_image());
                pst.setInt(8, selectedProduct.getProduct_id());

                pst.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("CRUD");
                alert.setHeaderText("Product updated successfully ! ");
                alert.showAndWait();

            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }

            // Refresh the table
            table.refresh();

            // Reselect the item
            table.getSelectionModel().select(selectedProduct);

            // Clear the input fields
            clearFields();
        }
    }


    //Delete product-----------------------------------------------------------------------------
    @FXML
    void deletebutton(ActionEvent event) {
        Product selectedProduct = table.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("CRUD");
            alert.setHeaderText("Select an item to delete ! ");
            alert.showAndWait();
            return;
        }

        // Remove the product from the list
        productList.remove(selectedProduct);

        // Refresh the table to reflect the updated data
        table.refresh();

        // Clear the input fields
        clearFields();

        // Create and execute the SQL delete statement
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement("DELETE FROM product WHERE product_id=?");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            pst.setInt(1, selectedProduct.getProduct_id());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            pst.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("CRUD");
            alert.setHeaderText("Product deleted successfully ! ");
            alert.showAndWait();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productList = FXCollections.observableArrayList();

        // Set up the table columns
        product_id.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        product_name.setCellValueFactory(new PropertyValueFactory<>("product_name"));
        product_quantity.setCellValueFactory(new PropertyValueFactory<>("product_quantity"));
        product_size.setCellValueFactory(new PropertyValueFactory<>("product_size"));
        product_price.setCellValueFactory(new PropertyValueFactory<>("product_price"));
        product_description.setCellValueFactory(new PropertyValueFactory<>("product_description"));
        product_disponibility.setCellValueFactory(new PropertyValueFactory<>("product_disponibility"));
        product_image.setCellValueFactory(new PropertyValueFactory<>("product_image"));

        // Make the table editable
        table.setEditable(true);
        product_name.setCellFactory(TextFieldTableCell.forTableColumn());
        product_quantity.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        product_size.setCellFactory(TextFieldTableCell.forTableColumn());
        product_price.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        product_description.setCellFactory(TextFieldTableCell.forTableColumn());
        product_disponibility.setCellFactory(TextFieldTableCell.forTableColumn());
        product_image.setCellFactory(TextFieldTableCell.forTableColumn());

        // Load the products from the database
        loadProducts();

        // Set the table data
        table.setItems(productList);

        // Add a listener to the selectedItemProperty of the TableView
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Populate the fields with the data of the new selected item
                productname.setText(newSelection.getProduct_name());
                productquantity.setText(String.valueOf(newSelection.getProduct_quantity()));
                productsize.setText(newSelection.getProduct_size());
                productprice.setText(String.valueOf(newSelection.getProduct_price()));
                productdescription.setText(newSelection.getProduct_description());
                productdisponibility.setText(newSelection.getProduct_disponibility());
                productimage.setText(newSelection.getProduct_image());
            }
        });
    }


    // Method to clear the input fields
    private void clearFields() {
        productname.clear();
        productquantity.clear();
        productsize.clear();
        productprice.clear();
        productdescription.clear();
        productdisponibility.clear();
        productimage.clear();
    }


}