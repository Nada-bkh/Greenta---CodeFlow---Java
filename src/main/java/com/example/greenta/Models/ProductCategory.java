package com.example.greenta.Models;

public class ProductCategory {
    private int productcategory_id;
    private String productcategory_name;
    private String productcategory_image;

    public ProductCategory(int productcategory_id, String productcategory_name, String productcategory_image) {
        this.productcategory_id = productcategory_id;
        this.productcategory_name = productcategory_name;
        this.productcategory_image= productcategory_image;
    }

    public ProductCategory(String productcategory_name, String productcategory_image) {
        this.productcategory_name = productcategory_name;
        this.productcategory_image = productcategory_image;
    }

    public int getProductcategory_id() {
        return productcategory_id;
    }

    public void setProductcategory_id(int productcategory_id) {
        this.productcategory_id = productcategory_id;
    }

    public String getProductcategory_name() {
        return productcategory_name;
    }

    public void setProductcategory_name(String productcategory_name) {
        this.productcategory_name = productcategory_name;
    }

    public String getProductcategory_image() {
        return productcategory_image;
    }

    public void setProductcategory_image(String productcategory_image) {
        this.productcategory_image = productcategory_image;
    }
}
