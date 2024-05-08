package com.example.greenta.GUI;

import com.example.greenta.Models.Product;
import com.example.greenta.Utils.Connect;
import com.example.greenta.Utils.MyConnection;
import com.itextpdf.text.*;
import com.example.greenta.Models.ProductCategory;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductController implements  Initializable {

    private Product product;

    @FXML
    private Button interpretButton;

    @FXML
    private PieChart chartpie;

    @FXML
    private Button pdf;

    @FXML
    private ComboBox<ProductCategory> category;

    @FXML
    private Button refresh;

    @FXML
    private Button search;

    @FXML
    private Button asc;

    @FXML
    private Button desc;

    @FXML
    private RadioButton Available;

    @FXML
    private RadioButton Unavailable;

    @FXML
    private Button addbutton;

    @FXML
    private Button deletebutton;


    @FXML
    private ComboBox<String> size;

    @FXML
    private ImageView image;

    @FXML
    private ListView<Product> list2;


    @FXML
    private TextField searchfield;

    @FXML
    private TextField productdescription;


    @FXML
    private TextField productname;

    @FXML
    private TextField productprice;

    @FXML
    private TextField productquantity;


    @FXML
    private Button updatebutton;

    @FXML
    private Button upload;

    private String imagePath = null; // Variable to store the image path



    private ObservableList<Product> productList; // List to hold the products


    Connect connect = new Connect();
    private MyConnection connection; // Database connection


    private void populateCategories() {
        try {
            Connection conn = connection.getCnx();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM productcategory");
            ResultSet rs = stmt.executeQuery();

            category.getItems().clear();

            while (rs.next()) {
                int productcategory_id = rs.getInt("productcategory_id");
                String productcategory_name = rs.getString("productcategory_name");
                String productcategory_image = rs.getString("productcategory_image");
                ProductCategory productCategory = new ProductCategory(productcategory_id, productcategory_name, productcategory_image);
                category.getItems().add(productCategory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //-------------------------Add product-----------------------------------------------------------------------------
    @FXML
    public void addbutton(ActionEvent event) {
        String product_name, product_description;
        Integer product_quantity = null;
        Float product_price = null;
        String product_image = imagePath;
        String product_disponibility;

        product_name = productname.getText();
        product_description = productdescription.getText();

        // Get the selected size from the ComboBox
        String product_size = size.getSelectionModel().getSelectedItem();

        // Validate input fields
        if (product_name.isEmpty() || product_size == null || product_description.isEmpty() || image.getImage()==null ){
            System.out.println("Fields cannot be empty!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validation");
            alert.setHeaderText("Fields cannot be empty!");
            alert.setContentText("All fields must be filled!");
            alert.showAndWait();
            return;
        }

        // Validate name (contains only letters)
        if (!product_name.matches("[a-zA-Z]+")) {
            System.out.println("Product name can only contain letters!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validation");
            alert.setHeaderText("Product name can only contain letters!");
            alert.showAndWait();
            return;
        }

        // Validation product quantity
        try {
            product_quantity = Integer.valueOf(productquantity.getText());
            if (product_quantity <= 0) {
                System.out.println("quantity wrong !!!");
                return;
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validation");
            alert.setHeaderText("Quantity can't be null!");
            alert.setContentText("Quantity field must be a number!");
            alert.showAndWait();
            return;
        }

        // Parse product price
        try {
            product_price = Float.valueOf(productprice.getText());
            if (product_price <= 0) {
                System.out.println("Price wrong !!");
                return;
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validation");
            alert.setHeaderText("Price field must be a float");
            alert.showAndWait();
            return;
        }

        // Validate disponibility
        if (Available.isSelected()) {
            product_disponibility = "Available";
        } else if (Unavailable.isSelected()) {
            product_disponibility = "Unavailable";
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validation");
            alert.setHeaderText("Please select disponibility!");
            alert.showAndWait();
            return;
        }

        // Initialize the product object
        product = new Product();


        ProductCategory selectedCategory = category.getSelectionModel().getSelectedItem();


        try {
            PreparedStatement pst = connect.con.prepareStatement("INSERT INTO product (product_name, product_quantity, product_size, product_price, product_description, product_disponibility, product_image, productcategory_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?,?)");
            product.setProductcategory_id(selectedCategory.getProductcategory_id());
            pst.setString(1, product_name);
            if (product_quantity != null) {
                pst.setInt(2, product_quantity);
            } else {
                pst.setNull(2, java.sql.Types.INTEGER);
            }
            pst.setString(3, product_size);
            if (product_price != null) {
                pst.setFloat(4, product_price);
            } else {
                pst.setNull(4, java.sql.Types.FLOAT);
            }
            pst.setString(5, product_description);
            pst.setString(6, product_disponibility);
            pst.setString(7, product_image);
            pst.setInt(8, product.getProductcategory_id());
            pst.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Product Add");
            alert.setHeaderText("Add a product !");
            alert.setContentText("Product added successfully !!!!");
            alert.showAndWait();
        } catch (SQLException ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }
        list2();
    }


    //-------------------------Update--------------------------------------------------
    @FXML
    void updatebutton(ActionEvent event) throws SQLException {
        // Get the selected item
        Product selectedProduct = list2.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("CRUD");
            alert.setHeaderText("Select an item to update!");
            alert.showAndWait();
            return;
        }

        // Check if an item is selected
        if (selectedProduct != null) {
            // Validate the fields
            if (productname.getText().isEmpty() || productquantity.getText().isEmpty() || productprice.getText().isEmpty() || productdescription.getText().isEmpty()) {
                // Display an error message
                System.out.println("All fields must be filled out");
                return;
            }

            // Get the data from the fields and update the selected item with this data
            selectedProduct.setProduct_name(productname.getText());
            selectedProduct.setProduct_quantity(Integer.parseInt(productquantity.getText()));
            selectedProduct.setProduct_size(size.getSelectionModel().getSelectedItem()); // Get the selected size from the ComboBox
            selectedProduct.setProduct_price(Float.parseFloat(productprice.getText()));
            selectedProduct.setProduct_description(productdescription.getText());

            // Update the availability
            if (Available.isSelected()) {
                selectedProduct.setProduct_disponibility("Available");
            } else if (Unavailable.isSelected()) {
                selectedProduct.setProduct_disponibility("Unavailable");
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Validation");
                alert.setHeaderText("Please select availability!");
                alert.showAndWait();
                return;
            }

            // Update the item in the database
            try {
                String updateQuery = "UPDATE product SET product_name = ?, product_quantity = ?, product_size = ?, product_price = ?, product_description = ?, product_disponibility = ? WHERE product_id = ?";
                PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(updateQuery);
                pst.setString(1, selectedProduct.getProduct_name());
                pst.setInt(2, selectedProduct.getProduct_quantity());
                pst.setString(3, selectedProduct.getProduct_size());
                pst.setFloat(4, selectedProduct.getProduct_price());
                pst.setString(5, selectedProduct.getProduct_description());
                pst.setString(6, selectedProduct.getProduct_disponibility());
                pst.setInt(7, selectedProduct.getProduct_id());

                pst.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("CRUD");
                alert.setHeaderText("Product updated successfully!");
                alert.showAndWait();

            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }

            // Reselect the item
            list2.getSelectionModel().select(selectedProduct);
        }
        list2();
    }


    //-----------------------Delete product-----------------------------------------------------------------------------
    @FXML
    void deletebutton(ActionEvent event) {
        Product selectedProduct = list2.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            System.out.println("No item selected!");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("CRUD");
            alert.setHeaderText("Select a product  to delete ! ");
            alert.showAndWait();

            return;
        }

        // Remove the product from the list
        productList.remove(selectedProduct);


        // Create and execute the SQL delete statement
        PreparedStatement pst = null;
        try {
            pst = connect.con.prepareStatement("DELETE FROM product WHERE product_id=?");
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
            System.out.println("Product  deleted successfully ! ");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("CRUD");
            alert.setHeaderText("Product  deleted successfully ! ");
            alert.showAndWait();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        list2();

    }


    //---------------------------------LISTALLPRODUCTS-----------------------------------------------------------------------------
    @FXML
    public void list2() {
        connect = new Connect();
        productList.clear(); // Clear the productList
        try {
            PreparedStatement pst = connect.con.prepareStatement("SELECT  product_id, product_name, product_quantity, product_size, product_price, product_description, product_disponibility, product_image FROM product ");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProduct_id(Integer.parseInt(rs.getString("product_id")));
                product.setProduct_name(rs.getString("product_name"));
                product.setProduct_quantity(rs.getInt("product_quantity"));
                product.setProduct_size(rs.getString("product_size"));
                product.setProduct_price(rs.getFloat("product_price"));
                product.setProduct_description(rs.getString("product_description"));
                product.setProduct_disponibility(rs.getString("product_disponibility"));
                product.setProduct_image(rs.getString("product_image"));
                productList.add(product); // Add the product to productList
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductCategory.class.getName()).log(Level.SEVERE, null, ex);
        }
        list2.setItems(productList); // Set the items of list2 to productList
        list2.setCellFactory(param -> new ListCell<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("FOR : "+item.getProduct_name() +  "   YOU HAVE :  " + item.getProduct_quantity() +  "    SIZE : " + item.getProduct_size() +  "   PRICED AT : " + item.getProduct_price() +  ".  DESCRIPTION : " + item.getProduct_description() +  "  AVAILABLITY: "  + item.getProduct_disponibility() +  "---------"  + item.getProduct_image()  );
                }
            }
        });

        list2.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                productname.setText(newVal.getProduct_name());
                productquantity.setText(String.valueOf(newVal.getProduct_quantity()));
                productprice.setText(String.valueOf(newVal.getProduct_price()));
                productdescription.setText(newVal.getProduct_description());
                // Load the image from the selected product's image path
                File file = new File(newVal.getProduct_image());
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



    //-------------------------- Method to load the products from the database----------------------------------------
    @FXML
    private void loadProducts() {
        try {
            Connection conn = connection.getCnx();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM product");
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

                Product product = new Product();
                productList.add(product);
            }

            // Don't close the connection here
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // -------------------------Method to clear the fields----------------------------------------
    @FXML
    private void clearFields() {
        productname.clear();
        productquantity.clear();
//        productsize.clear();
        productprice.clear();
        productdescription.clear();
//        productimage.clear();
    }

    // -----------------------Upload image----------------------------------------
    @FXML
    void upload(ActionEvent event)  {
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

    //---------------TRI ASC DESC-----------------------------
    @FXML
    void triasc(ActionEvent event) {
        productList.sort(Comparator.comparing(Product::getProduct_name));
        list2.setItems(null);
        list2.setItems(productList);
    }

    @FXML
    void tridesc(ActionEvent event) {
        productList.sort(Comparator.comparing(Product::getProduct_name).reversed());
        list2.setItems(null);
        list2.setItems(productList);
    }

    //------------------Search---------------------------------------
    @FXML
    void search(ActionEvent event) {
        String searchTerm = searchfield.getText();

        ObservableList<Product> filteredList = productList.filtered(product ->
                product.getProduct_name().toLowerCase().contains(searchTerm.toLowerCase())
        );

        list2.setItems(filteredList);
    }

    //-------------------REFRESH--------------//
    @FXML
    void refresh(ActionEvent event) {
    list2();
    }

    //--------------PDF----------------------//

    @FXML
    private void generatePDF(ActionEvent event) throws FileNotFoundException, DocumentException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            // create a new PDF document
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            PdfWriter.getInstance(document, new FileOutputStream(selectedFile));

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
            Paragraph title = new Paragraph("Greenta Product List", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // Add some space


            PdfPTable table = new PdfPTable(7);

            // Add headers with custom font and background color
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            PdfPCell nomPCell = new PdfPCell(new Phrase("Name", headerFont));
            PdfPCell descriptionPCell = new PdfPCell(new Phrase("Description", headerFont));
            PdfPCell sizePCell = new PdfPCell(new Phrase("Size", headerFont));
            PdfPCell disponibilityPCell = new PdfPCell(new Phrase("Disponibility", headerFont));
            PdfPCell prixCell = new PdfPCell(new Phrase("Price", headerFont));
            PdfPCell quantityPCell = new PdfPCell(new Phrase("Quantity", headerFont));
            PdfPCell imageCell = new PdfPCell(new Phrase("Image", headerFont));

            nomPCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            descriptionPCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            sizePCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            disponibilityPCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            prixCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            quantityPCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            imageCell.setBackgroundColor(BaseColor.LIGHT_GRAY);


            table.addCell(nomPCell);
            table.addCell(descriptionPCell);
            table.addCell(sizePCell );
            table.addCell(disponibilityPCell);
            table.addCell(prixCell);
            table.addCell(quantityPCell);
            table.addCell(imageCell);

            // Add rows with custom font
            Font cellFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            for (Product product: list2.getItems()) {
                table.addCell(new Phrase(product.getProduct_name(), cellFont));
                table.addCell(new Phrase(product.getProduct_description(), cellFont));
                table.addCell(new Phrase(product.getProduct_size(), cellFont));
                table.addCell(new Phrase(product.getProduct_disponibility(), cellFont));
                table.addCell(new Phrase(String.valueOf(product.getProduct_price()), cellFont));
                table.addCell(new Phrase(String.valueOf(product.getProduct_quantity()), cellFont));
                table.addCell(new Phrase(product.getProduct_image(), cellFont));
            }

            table.setWidthPercentage(100); // Set table width
            table.setSpacingBefore(10f); // Set spacing before table
            table.setSpacingAfter(10f); // Set spacing after table


            document.add(table);
            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PDF Export");
            alert.setHeaderText("Export successful");
            alert.setContentText("The PRODUCT has been exported to PDF successfully.");
            alert.showAndWait();
        }
    }

    //---------------STAT----------------------------//
    public void populatePieChart() {
        Map<String, Integer> productSales = getProductSalesByProduct();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : productSales.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        chartpie.setData(pieChartData);
    }

//---------------------------INTERPRET----------------------//
    @FXML
    void interpret(ActionEvent event) {
        Map<String, Integer> productSales = getProductSalesByProduct();

        for (Map.Entry<String, Integer> entry : productSales.entrySet()) {
            String product_name = entry.getKey();
            int salesCount = entry.getValue();

            if (salesCount < 2) {
                String message = "The '" + product_name + "' is not being sold enough, only " + salesCount + " were sold. Apply a discount.";
                // Display the message (e.g., in a dialog box or label)
                System.out.println(message);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("SALES");
                alert.setHeaderText("SALES ALERT");
                alert.setContentText("The '" + product_name + "' are not being sold enough, only " + salesCount + " were sold. Apply a discount.");
                alert.showAndWait();
            }
        }
    }
    private Map<String, Integer> getProductSalesByProduct() {
        Map<String, Integer> productSales = new HashMap<>();

        try {
            Connection conn = connection.getCnx();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT product.product_name, SUM(sale.nbr_vente) " +
                            "FROM sale " +
                            "INNER JOIN product ON sale.product_id = product.product_id " +
                            "GROUP BY product.product_name"
            );
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String productName = rs.getString(1);
                int salesCount = rs.getInt(2);
                productSales.put(productName, salesCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productSales;
    }





    //----------------------INITIALIZE--------------------//
    public void initialize(URL location, ResourceBundle resources) {
        // Set the items for the size ComboBox
        ObservableList<String> sizes = FXCollections.observableArrayList("XS", "S", "M", "L", "XL", "XXL");
        size.setItems(sizes);
        size.setValue("M");

        // Initialize the connection object
        connection = MyConnection.getInstance();

        // Populate the ComboBox with product categories
        populateCategories();

        populatePieChart();
        // Initialize productList and populate the list2
        productList = FXCollections.observableArrayList();
        list2();
    }








}