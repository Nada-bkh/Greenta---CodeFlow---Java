package com.example.greenta.Models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Cour {
    private int id;
    private String description;
    private String titre;
    private String niveau;
    private String categorie;
    private String pdfpath;
    private LocalDateTime created_at;

    public Cour() {
    }

    public Cour(String description, String titre, String niveau, String categorie, String pdfpath, LocalDateTime created_at) {
        this.description = description;
        this.titre = titre;
        this.niveau = niveau;
        this.categorie = categorie;
        this.pdfpath = pdfpath;
        this.created_at = created_at;
    }

    public Cour(int id, String description, String titre, String niveau, String categorie, String pdfpath, LocalDateTime created_at) {
        this.id = id;
        this.description = description;
        this.titre = titre;
        this.niveau = niveau;
        this.categorie = categorie;
        this.pdfpath = pdfpath;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getPdfpath() {
        return pdfpath;
    }

    public void setPdfpath(String pdfpath) {
        this.pdfpath = pdfpath;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cour cour)) return false;
        return Objects.equals(getDescription(), cour.getDescription()) && Objects.equals(getTitre(), cour.getTitre());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), getTitre());
    }

    @Override
    public String toString() {
        return "Cour{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", titre='" + titre + '\'' +
                ", niveau='" + niveau + '\'' +
                ", categorie='" + categorie + '\'' +
                ", pdfpath='" + pdfpath + '\'' +
                ", created_at=" + created_at +
                '}'+"\n";
    }
}
