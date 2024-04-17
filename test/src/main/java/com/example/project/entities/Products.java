package com.example.project.entities;

import java.util.Arrays;

public class Products{
    private int product_id;
    private String product_name;
    private int product_quantity;
    private String product_size;
    private float product_price;
    private String product_description;
    private String product_disponibility;
    private String product_image;

    //Constructeur **************************************************
    public Products (int product_id , String product_name, int product_quantity, String product_size, float product_price, String product_description, String product_disponibility, String product_image) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_quantity = product_quantity;
        this.product_size = product_size;
        this.product_price = product_price;
        this.product_description = product_description;
        this.product_disponibility = product_disponibility;
        this.product_image = product_image;
    }




    public Products (String product_name, int product_quantity, String product_size, float product_price, String product_description, String product_disponibility, String product_image) {

        this.setProduct_name(product_name);
        this.setProduct_quantity(product_quantity);
        this.setProduct_size(product_size);
        this.setProduct_price(product_price);
        this.setProduct_description(product_description);
        this.setProduct_disponibility(product_disponibility);
        this.setProduct_image(product_image);
    }

    public Products() {

    }




    // Getter methods *********************************************
    public int getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public String getProduct_size() {
        return product_size;
    }

    public float getProduct_price() {
        return product_price;
    }

    public String getProduct_description() {
        return product_description;
    }

    public String getProduct_disponibility() {
        return product_disponibility;
    }

    public String getProduct_image() {
        return product_image;
    }

    // Setter methods ********************************************
    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setProduct_name(String product_name) {
        if (product_name == null || product_name.isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }

        if (!product_name.matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("Product name can only contain letters");
        }

        this.product_name = product_name;
    }

    public void setProduct_quantity(int product_quantity) {
        if (product_quantity <= 0) {
            throw new IllegalArgumentException("Product quantity must be greater than 0");
        }

        this.product_quantity = product_quantity;
    }

    public void setProduct_size(String product_size) {
        if (product_size == null || product_size.isEmpty()) {
            throw new IllegalArgumentException("Product size cannot be empty");
        }
        if (!Arrays.asList("XS", "S", "M", "L", "XL", "2XL", "3XL").contains(product_size)) {
            throw new IllegalArgumentException("Product size must be one of the following: XS, S, M, L, XL, 2XL, 3XL");
        }

        this.product_size = product_size;
    }

    public void setProduct_price(float product_price) {
        if (product_price <= 0) {
            throw new IllegalArgumentException("Product price must be greater than 0");
        }

        this.product_price = product_price;
    }

    public void setProduct_description(String product_description) {
        if (product_description == null || product_description.isEmpty()) {
            throw new IllegalArgumentException("Product description cannot be empty");
        }

        this.product_description = product_description;
    }

    public void setProduct_disponibility(String product_disponibility) {
        if (product_disponibility == null || product_disponibility.isEmpty()) {
            throw new IllegalArgumentException("Product disponibility cannot be empty");
        }

        this.product_disponibility = product_disponibility;
    }

    public void setProduct_image(String product_image) {
        if (product_image == null || product_image.isEmpty()) {
            throw new IllegalArgumentException("Product image cannot be empty");
        }

        this.product_image = product_image;
    }


    //toString ******************************************************
    @Override
    public String toString() {
        return  "Product ID: " + product_id +
                "\nProduct Name: " + product_name +
                "\nProduct Quantity: " + product_quantity +
                "\nProduct Size: " + product_size +
                "\nProduct Price: " + product_price +
                "\nProduct Description: " + product_description +
                "\nProduct Availability: " + product_disponibility +
                "\nProduct Image: " + product_image;
    }

}



