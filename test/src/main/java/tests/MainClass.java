package tests;

import entities.ProductCategory;
import entities.Products;
import services.ProductCategoryCRUD;
import services.ProductsCRUD;
import utils.MyConnection;

import java.sql.SQLException;
import java.util.List;

public class MainClass {
    public static void main(String[] args) {

// Product Category  *********************************************************************************************

        // add product category Dynamic methode
//        ProductCategoryCRUD pcd = new ProductCategoryCRUD();
//        ProductCategory p = new ProductCategory("Animals", "test2");
//        pcd.addProductCategory(p);

        // add product category static methode
//            ProductCategoryCRUD pcd=new ProductCategoryCRUD();
//            pcd.addProductCategory();

        // show Product Category
//            ProductCategoryCRUD pcd=new ProductCategoryCRUD();
//            System.out.println(pcd.showProductCategory());

        // update product category static methode
//            ProductCategoryCRUD pcd=new ProductCategoryCRUD();
//            pcd.updateProductCategory();

        // delete product category static methode
//            ProductCategoryCRUD pcd=new ProductCategoryCRUD();
//            pcd.deleteProductCategory();

        //test search product category
//        ProductCategoryCRUD productCategoryCRUD = new ProductCategoryCRUD();
//        List<ProductCategory> productCategories = productCategoryCRUD.searchByName("Women");
//        for (ProductCategory productCategory : productCategories) {
//            System.out.println(productCategory);
//        }


// Products  ****************************************************************************************************

        // add product static methode
//        ProductsCRUD pcd=new ProductsCRUD();
//        pcd.addProducts();

        // add product dynamic methode
//        ProductsCRUD pcd= new ProductsCRUD();
//        Products P2=new Products("Pants",5,"M", 34.99F,
//                "Best Hoodie ever","Available","img");
//        pcd.addProducts2(P2);

        // show Products
//        ProductsCRUD pcd=new ProductsCRUD();
//        System.out.println(pcd.showProducts());

        // update product static methode
//        ProductsCRUD pcd=new ProductsCRUD();
//        pcd.updateProducts();

        // delete product static methode
//        ProductsCRUD pcd=new ProductsCRUD();
//        pcd.deleteProducts();


        //test search product
//        ProductsCRUD productsCRUD = new ProductsCRUD();
//        List<Products> products = productsCRUD.searchByName("kho");
//        for (Products product : products) {
//            System.out.println(product);
//        }


    }

    }

    
