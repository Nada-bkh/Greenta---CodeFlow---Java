package com.example.project.entities;

public class Product {
        private int product_id;
        private String product_name;
        private int product_quantity;
        private String product_size;
        private float product_price;
        private String product_description;
        private String product_disponibility;
        private String product_image;


    public Product(int product_id, String product_name, int product_quantity, String product_size, float product_price, String product_description, String product_disponibility, String product_image) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_quantity = product_quantity;
        this.product_size = product_size;
        this.product_price = product_price;
        this.product_description = product_description;
        this.product_disponibility = product_disponibility;
        this.product_image = product_image;
    }

    public Product(String product_name, String product_quantity, String product_size, String product_price, String product_description, String product_disponibility, String product_image) {

        this.product_name = product_name;
        this.product_quantity = Integer.parseInt(product_quantity);
        this.product_size = product_size;
        this.product_price = Float.parseFloat(product_price);
        this.product_description = product_description;
        this.product_disponibility = product_disponibility;
        this.product_image = product_image;
    }

    public Product(int productcategoryId, String productcategoryName, String productcategoryImage) {
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }
    public void setProduct_name(String product_name) {
        if (product_name == null || product_name.isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }

        if (!product_name.matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("Product name can only contain letters");
        }

        this.product_name = product_name;
        this.product_name = product_name;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }
    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getProduct_size() {
        return product_size;
    }

    public void setProduct_size(String product_size) {
        this.product_size = product_size;
    }

    public float getProduct_price() {
        return product_price;
    }

    public void setProduct_price(float product_price) {
        this.product_price = product_price;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {

        this.product_description = product_description;
    }

    public String getProduct_disponibility() {
        return product_disponibility;
    }

    public void setProduct_disponibility(String product_disponibility) {
        this.product_disponibility = product_disponibility;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }



}
