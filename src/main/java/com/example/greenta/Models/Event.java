package com.example.greenta.Models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Event {
    private int id;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private String organizer;
    private int capacity;
    private String image;

    public Event() {
    }

    public Event(String title, LocalDateTime startDate, LocalDateTime endDate, String location, String organizer, int capacity, String image) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.organizer = organizer;
        this.capacity = capacity;
        this.image = image;
    }

    public Event(int id, String title, LocalDateTime startDate, LocalDateTime endDate, String location, String organizer, int capacity, String image) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.organizer = organizer;
        this.capacity = capacity;
        this.image = image;
    }

    public Event(String title, LocalDateTime startDate, LocalDateTime endDate, String location, String organizer, int capacity) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.organizer = organizer;
        this.capacity = capacity; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", location='" + location + '\'' +
                ", organizer='" + organizer + '\'' +
                ", capacity=" + capacity +
                ", image='" + image + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id && capacity == event.capacity && Objects.equals(title, event.title) && Objects.equals(startDate, event.startDate) && Objects.equals(endDate, event.endDate) && Objects.equals(location, event.location) && Objects.equals(organizer, event.organizer) && Objects.equals(image, event.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, startDate, endDate, location, organizer, capacity, image);
    }
}
