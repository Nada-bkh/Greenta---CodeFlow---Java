package com.example.greenta.Models;

import javafx.scene.image.ImageView;

import java.time.LocalDate;


public class Job {
    private int id;
    private String organisation ,title ,description ,picture ;
    private LocalDate startdate;
    private ImageView img;

    public Job() {
    }

    public Job(int id, String organisation, String title, String description, LocalDate startdate, String picture, ImageView img) {
        this.id = id;
        this.organisation = organisation;
        this.title = title;
        this.description = description;
        this.startdate = startdate;
        this.picture = picture;

    }

    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public LocalDate getStartdate() {
        return startdate;
    }

    public void setStartdate(LocalDate startdate) {
        this.startdate = startdate;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", organisation='" + organisation + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", picture='" + picture + '\'' +
                ", startdate=" + startdate +
                '}';
    }
}

