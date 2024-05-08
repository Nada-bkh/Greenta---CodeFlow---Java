package com.example.greenta.Models;

public class Sale {
    private int id_sale;
    private int nbr_vente;
    private int product_id;


    public Sale() {
        this.id_sale = id_sale;
        this.nbr_vente = nbr_vente;
        this.product_id = product_id;
    }

    public int getId_sale() {
        return id_sale;
    }

    public void setId_sale(int id_sale) {
        this.id_sale = id_sale;
    }

    public int getNbr_vente() {
        return nbr_vente;
    }

    public void setNbr_vente(int nbr_vente) {
        this.nbr_vente = nbr_vente;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public Sale(int id_sale, int nbr_vente, int product_id) {
        this.id_sale = id_sale;
        this.nbr_vente = nbr_vente;
        this.product_id = product_id;
    }
}
