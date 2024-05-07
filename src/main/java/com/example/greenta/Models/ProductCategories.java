package com.example.greenta.Models;

public class ProductCategories {
    private int productcategory_id;
    private  String productcategory_name;
    private  String productcategory_image;

    // Constructor *********************************************
    public ProductCategories(int productcategory_id, String productcategory_name, String productcategory_image) {
        this.productcategory_id = productcategory_id;
        this.productcategory_name = productcategory_name;
        this.productcategory_image = productcategory_image;
    }
    public ProductCategories(String productcategory_name, String productcategory_image) {
    this.setProductcategory_name(productcategory_name);
    this.setProductcategory_image(productcategory_image);
}
    public ProductCategories() {

    }

    // Getter  methods *********************************************
    public int getProductcategory_id() {
        return productcategory_id;
    }

    public String getProductcategory_name() {
        return productcategory_name;
    }

    public String getProductcategory_image() {
        return this.productcategory_image;
    }

    // Setter methods **********************************************
    public void setProductcategory_id(int productcategory_id) {
        this.productcategory_id = productcategory_id;
    }

    public void setProductcategory_name(String productcategory_name) {
        if (productcategory_name == null || productcategory_name.isEmpty()) {
            throw new IllegalArgumentException("Product category name cannot be empty !!! ");
        }

        if (!productcategory_name.matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("Product category name can only contain letters !!!");
        }
        this.productcategory_name = productcategory_name;
    }

    public void setProductcategory_image(String productcategory_image) {
        if (productcategory_image == null || productcategory_image.isEmpty()) {
            throw new IllegalArgumentException("Product category image cannot be empty !!! ");
        }
        this.productcategory_image = productcategory_image;
    }

    // toString method ************************************************************
    @Override
    public String toString() {
        return "Product Category ID: " + productcategory_id +
                "\nProduct Category Name: " + productcategory_name +
                "\nProduct Category Image: " + productcategory_image;
    }
}