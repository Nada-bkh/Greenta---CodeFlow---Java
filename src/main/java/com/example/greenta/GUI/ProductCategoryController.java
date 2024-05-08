package com.example.greenta.GUI;
import java.awt.*;

import com.example.greenta.Exceptions.UserNotFoundException;
import com.example.greenta.Models.ProductCategory;
import com.example.greenta.Models.User;
import com.example.greenta.Services.SessionService;
import com.example.greenta.Services.UserService;
import com.example.greenta.Utils.MyConnection;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import java.awt.image.BufferedImage;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
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
    private Button profileLabel;
    @FXML
    private Button deletebutton;

    @FXML
    private TableColumn<ProductCategory,Integer> productcategory_id;

    @FXML
    private TableColumn<ProductCategory,String> productcategory_image;

    @FXML
    private TableColumn<ProductCategory,String> productcategory_name;

    @FXML
    private ImageView image;

    @FXML
    private TextField productcategoryimage;

    @FXML
    private TextField productcategoryname;

    @FXML
    private TableView<ProductCategory> table;

    private ObservableList<ProductCategory> productcategoryList; // List to hold the products

    Connection connection = MyConnection.getInstance().getConnection();
    private String imagePath = null; // Variable to store the image path
    @FXML
    private Button updatebutton;
    private ProductCategory productcategory;
    private final UserService userService = UserService.getInstance();
    private SessionService sessionService = SessionService.getInstance();
    private User currentUser;
    @FXML
    public void initialize(int userId) throws UserNotFoundException {
        currentUser = userService.getUserbyID(userId);
        profileLabel.setText(currentUser.getFirstname());
    }

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
            alert.setTitle("Validation");
            alert.setHeaderText("Name Field cannot be empty! ! ");
            alert.showAndWait();
            return;

        }
        if ( productcategory_image.isEmpty()) {
            System.out.println("Image Field cannot be empty!");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validation");
            alert.setHeaderText("Image Field cannot be empty! ! ");
            alert.showAndWait();
            return;

        }

        // Validate name (contains only letters)
        if (!productcategory_name.matches("[a-zA-Z]+")) {
            System.out.println("Product category name can only contain letters!");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Product category name can only contain letters!");
            alert.setHeaderText("Product category name can only contain letters!");
            alert.showAndWait();
            return;
        }

        try {
            PreparedStatement pst = connection.prepareStatement("INSERT INTO product_category (categoryname,categoryimage) " +
                    "VALUES (?, ?)");
            pst.setString(1, String.valueOf(productcategoryname));
            pst.setString(2, String.valueOf(productcategoryimage));
            pst.executeUpdate();

            // Refresh the table
            loadProductCategories();
            table.refresh();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            System.out.println("Product category added successfully ! ");
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
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM product_category");
            ResultSet rs = stmt.executeQuery();

            productcategoryList.clear(); // Move this line here

            while (rs.next()) {
                //int productcategory_id = rs.getInt("id");
                String productcategory_name = rs.getString("categoryname");
                String productcategory_image = rs.getString("categoryimage");

                ProductCategory productcategory = new ProductCategory(productcategory_name, productcategory_image);
                productcategoryList.add(productcategory);
            }

            // Don't close the connection here
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Delete product-----------------------------------------------------------------------------
    @FXML
    void deletebutton(ActionEvent event) {
        ProductCategory selectedProductcategory = table.getSelectionModel().getSelectedItem();
        if (selectedProductcategory == null) {
            System.out.println("No item selected!");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("CRUD");
            alert.setHeaderText("Select a product category to delete ! ");
            alert.showAndWait();

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
            pst = connection.prepareStatement("DELETE FROM product_category WHERE id=?");
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
            System.out.println("Product category deleted successfully ! ");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("CRUD");
            alert.setHeaderText("Product category deleted successfully ! ");
            alert.showAndWait();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearFields() {
    }


//Update----------------------------------------------------------------------
    @FXML
    void updatebutton(ActionEvent event) throws SQLException {

        // Get the selected item
        ProductCategory selectedProductcategory = table.getSelectionModel().getSelectedItem();

        // Check if an item is selected

//        if (selectedProductcategory != null) {
            // Validate the fields
            if (productcategoryname.getText().isEmpty() ||productcategoryimage.getText().isEmpty()) {
                // Display an error message
                System.out.println("All fields must be filled out");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("CRUD");
                alert.setHeaderText("Select a product category to update ! ");
                alert.showAndWait();

                return;
            }

            // Get the data from the fields and update the selected item with this data
            selectedProductcategory.setProductcategory_name(productcategoryname.getText());
            selectedProductcategory.setProductcategory_image(productcategoryimage.getText());

            // Update the item in the database
            try {
                String updateQuery = "UPDATE product_category SET categoryname = ?,categoryimage = ? WHERE id = ?";
                PreparedStatement pst = connection.prepareStatement(updateQuery);
                pst.setString(1, selectedProductcategory.getProductcategory_name());
                pst.setString(2, selectedProductcategory.getProductcategory_image());
                pst.setInt(3, selectedProductcategory.getProductcategory_id());

                pst.executeUpdate();
                System.out.println("Product category updated successfully !! ");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("CRUD");
                alert.setHeaderText("Product category updated successfully !! ");
                alert.showAndWait();

            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }

            // Refresh the table
            table.refresh();

            // Reselect the item
            table.getSelectionModel().select(selectedProductcategory);

            // Clear the input fields
            clearFields();
//        }
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

    @FXML
    void charityButton(MouseEvent event) throws UserNotFoundException {
        User user = userService.getUserbyEmail(currentUser.getEmail());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/Back.fxml"));
            Parent root = loader.load();
            BackController backController = loader.getController();
            backController.initialize(user.getId());
            Scene scene = new Scene(root, 800, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void coursesButton(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/gestion-quiz-admin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void eventButton(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/AjouterEvent.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 800, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void homeButton(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/FrontHome.fxml"));
            Parent root = loader.load();
            FrontHomeController frontHomeController = loader.getController();
            frontHomeController.initialize(currentUser.getId());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException | UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void profileClicked(ActionEvent event) throws UserNotFoundException {
        User user = userService.getUserbyEmail(currentUser.getEmail());
        try {
            sessionService.setCurrentUser(user);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/Profile.fxml"));
            Parent root = loader.load();
            ProfileController profileController = loader.getController();
            profileController.initializeProfile(user.getId());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void recruitmentButton(MouseEvent event) throws UserNotFoundException{
        User user = userService.getUserbyEmail(currentUser.getEmail());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/FrontJob.fxml"));
            Parent root = loader.load();
            FrontJob frontJob = loader.getController();
            frontJob.initialize(user.getId());
            Scene scene = new Scene(root, 800, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void shopButton(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/ProductCategory.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 800, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void signOut(MouseEvent event) {
        sessionService.logout();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/User.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void backOffice(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/BackOffice.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void donation(MouseEvent event) throws UserNotFoundException {
        User user = userService.getUserbyEmail(currentUser.getEmail());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/AddCharity.fxml"));
            Parent root = loader.load();
            AddCharityController addCharityController = loader.getController();
            addCharityController.initialize(user.getId());
            Scene scene = new Scene(root, 800, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }


