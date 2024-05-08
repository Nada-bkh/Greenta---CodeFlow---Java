package com.example.greenta.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPageController {

    @FXML
    private Button productButton;

    @FXML
    private Button productCategoryButton;

    @FXML
    private void goToProductCategoryPage(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/ProductCategory.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }

    @FXML
    private void goToProductPage(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/Product.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }

    }