package com.example.greenta.GUI;

import com.example.greenta.Exceptions.UserNotFoundException;
import com.example.greenta.Models.User;
import com.example.greenta.Services.SessionService;
import com.example.greenta.Services.UserService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import java.awt.image.BufferedImage;
import com.example.greenta.Utils.MyConnection;
import com.example.greenta.Models.ProductCategory;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ObservableList;

import javax.imageio.ImageIO;
import java.sql.ResultSet;

public class ProductCategoryController {



    @FXML
    private Button retour;

    @FXML
    private Button pdf;

    @FXML
    private Button asc;

    @FXML
    private Button desc;

    @FXML
    private Button refresh;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private Button addbutton;

    @FXML
    private Button upload;

    @FXML
    private Button deletebutton;

    @FXML
    private ImageView image;

    @FXML
    private ListView<ProductCategory> list1;

    @FXML
    private TextField productcategoryname;

    private ObservableList<ProductCategory> productcategoryList; // List to hold the products

    Connection connection = MyConnection.getInstance().getConnection();
    private String imagePath = null; // Variable to store the image path

    @FXML
    private Button updatebutton;
    private ProductCategory productcategory;
    @FXML
    private Button profileLabel;
    private final UserService userService = UserService.getInstance();
    private final SessionService sessionService = SessionService.getInstance();
    private User currentUser;

    //Add product-----------------------------------------------------------------------------
    @FXML
    public void addbutton(ActionEvent event) {
        String productcategory_name;

        productcategory_name = productcategoryname.getText();
        String productcategory_image = imagePath;

        //-------------Control saisie name -------------//
        if (productcategory_name.isEmpty() ) {
            System.out.println("Name Field cannot be empty!");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validation");
            alert.setHeaderText("Name Field cannot be empty! ! ");
            alert.showAndWait();
            return;

        }
        //-------------Control saisie image -------------//
        if ( image.getImage() ==null) {
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

        //----------Control Saisie-------------//


        try {
            PreparedStatement pst = connection.prepareStatement("INSERT INTO product_category (category_name,category_image) " +
                    "VALUES (?, ?)");
            pst.setString(1, String.valueOf(productcategory_name));
            pst.setString(2, String.valueOf(productcategory_image));
            pst.executeUpdate();


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            System.out.println("Product category added successfully ! ");
            alert.setTitle("Product Category Add");
            alert.setHeaderText("Add a product category ! ");
            alert.setContentText("Product category added successfully !!!!");
            alert.showAndWait();


        } catch (SQLException ex) {
            Logger.getLogger(ProductCategoryController.class.getName()).log(Level.SEVERE, null, ex);
        }

        list1();
    }

    //Delete product-----------------------------------------------------------------------------
    @FXML
    void deletebutton(ActionEvent event) {
        ProductCategory selectedProductcategory = list1.getSelectionModel().getSelectedItem();
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
        list1();

    }

    //Update----------------------------------------------------------------------
    @FXML
    void updatebutton(ActionEvent event) throws SQLException {

        // Get the selected item
        ProductCategory selectedProductcategory = list1.getSelectionModel().getSelectedItem();

        // Check if an item is selected
        // Validate the fields
        if (productcategoryname.getText().isEmpty() ) {
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

        // Update the item in the database
        try {
            String updateQuery = "UPDATE product_category SET category_name = ?,category_image = ? WHERE id = ?";
            PreparedStatement pst = MyConnection.getInstance().getConnection().prepareStatement(updateQuery);
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

        // Reselect the item
        list1.getSelectionModel().select(selectedProductcategory);

        // Refresh the table
        list1();
    }

    //ListView--------------------------------------------------------------------------------
    @FXML
    public void list1() {
        ObservableList<ProductCategory> productCategories = FXCollections.observableArrayList();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT id, categoryname, categoryimage FROM product_category ");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                ProductCategory pt = new ProductCategory();
                pt.setProductcategory_id(Integer.parseInt(rs.getString("id")));
                pt.setProductcategory_name(rs.getString("categoryname"));
                pt.setProductcategory_image(rs.getString("categoryimage"));
                productCategories.add(pt);
            }
            // print out contents of produits
            for (ProductCategory p : productCategories) {
                System.out.println(p.getProductcategory_id() + "-" + p.getProductcategory_name() +  "-" + p.getProductcategory_image());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductCategory.class.getName()).log(Level.SEVERE, null, ex);
        }
        productcategoryList = productCategories; // Assign productCategories to productcategoryList
        list1.setItems(productcategoryList);

        list1.setItems(productCategories);
        list1.setCellFactory(param -> new ListCell<ProductCategory>() {
            @Override
            protected void updateItem(ProductCategory item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("FOR: " +item.getProductcategory_name() + "  --------  " + item.getProductcategory_image() );
                }
            }
        });

        list1.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {

                productcategoryname.setText(newVal.getProductcategory_name());

                // Load the image from the selected product category's image path
                File file = new File(newVal.getProductcategory_image());
                try {
                    BufferedImage bufferedImage = ImageIO.read(file);
                    WritableImage fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
                    image.setImage(fxImage); // Set the image to the ImageView
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    //Upload--------------------------------------------------------------------------------
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

    //trii--------------------------------------------------------------------------------
    @FXML
    void triasc(ActionEvent event) {
        productcategoryList.sort(Comparator.comparing(ProductCategory::getProductcategory_name));
        list1.setItems(productcategoryList);
    }

    @FXML
    void tridesc(ActionEvent event) {
        productcategoryList.sort(Comparator.comparing(ProductCategory::getProductcategory_name).reversed());
        list1.setItems(productcategoryList);
    }
    //Search--------------------------------------------------------------------------------
    @FXML
    void search(ActionEvent event) {
        String searchTerm = searchField.getText();

        ObservableList<ProductCategory> filteredList = productcategoryList.filtered(productCategory ->
                productCategory.getProductcategory_name().toLowerCase().contains(searchTerm.toLowerCase())
        );

        list1.setItems(filteredList);
    }

    //-------------------REFRESH--------------//
    @FXML
    void refresh(ActionEvent event) {
        list1();
    }

    //----------------PDF------------------------//
    @FXML
    private void generatePDF(ActionEvent event) throws FileNotFoundException, DocumentException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            // create a new PDF document
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(selectedFile));

            document.open();

            // Add the logo
            try {
                Image logo = Image.getInstance("C:\\Users\\FK Info\\Desktop\\Extras\\Project\\src\\main\\resources\\com\\example\\project\\img\\logo.png"); // Replace with the path to your logo
                logo.scaleAbsolute(50, 50);
                logo.setAlignment(Element.ALIGN_CENTER);
                document.add(logo);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Add a title to the document
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            titleFont.setColor(BaseColor.GREEN.darker());
            Paragraph title = new Paragraph("Greenta Product Category List", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // Add some space

            PdfPTable table = new PdfPTable(7);

            // Add headers with custom font and background color
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            PdfPCell nomPCell = new PdfPCell(new Phrase("product_name", headerFont));
            PdfPCell imageCell = new PdfPCell(new Phrase("product_image", headerFont));

            nomPCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            imageCell.setBackgroundColor(BaseColor.LIGHT_GRAY);

            // Add padding to cells
            nomPCell.setPadding(10);
            imageCell.setPadding(10);

            table.addCell(nomPCell);
            table.addCell(imageCell);

            // Add rows with custom font
            Font cellFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            for (ProductCategory productcategory: list1.getItems()) {
                PdfPCell nameCell = new PdfPCell(new Phrase(productcategory.getProductcategory_name(), cellFont));
                PdfPCell imageCellContent = new PdfPCell(new Phrase(productcategory.getProductcategory_image(), cellFont));

                // Add padding to cells
                nameCell.setPadding(10);
                imageCellContent.setPadding(10);

                table.addCell(nameCell);
                table.addCell(imageCellContent);
            }

            table.setWidthPercentage(100); // Set table width
            table.setSpacingBefore(10f); // Set spacing before table
            table.setSpacingAfter(10f); // Set spacing after table

            document.add(table);
            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PDF Export");
            alert.setHeaderText("Export successful");
            alert.setContentText("The PRODUCT CATEGORY has been exported to PDF successfully.");
            alert.showAndWait();
        }
    }

    //---------------------------Back Button----------------------//
    @FXML
    void retour(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/MainPage.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(scene);
    }


    //----------------------INITIALIZE--------------------//
    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @FXML
    public void initialize(int userId) throws UserNotFoundException {
        currentUser = userService.getUserbyID(userId);
        profileLabel.setText(currentUser.getFirstname());
        productcategoryList = FXCollections.observableArrayList();
        list1();
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


}