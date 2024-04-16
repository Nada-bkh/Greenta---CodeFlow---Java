package com.example.greenta.Models.Events;
import java.util.Objects;
import java.time.LocalDateTime;
import com.example.greenta.Models.User;

public class Event {
    private int id;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private User organizer;
    private int capacity;

    public Event() {
    }

    public Event(int id, String title, String description, LocalDateTime startDate, LocalDateTime endDate, String location, User organizer, int capacity) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.organizer = organizer;
        this.capacity = capacity;
    }

    public Event(String title, String description, LocalDateTime startDate, LocalDateTime endDate, String location, int organizerId, int capacity) {
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", location='" + location + '\'' +
                ", organizer_id=" + (organizer != null ? organizer.getId() : "null") +
                ", capacity=" + capacity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id &&
                capacity == event.capacity &&
                Objects.equals(title, event.title) &&
                Objects.equals(description, event.description) &&
                Objects.equals(startDate, event.startDate) &&
                Objects.equals(endDate, event.endDate) &&
                Objects.equals(location, event.location) &&
                Objects.equals(organizer, event.organizer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, startDate, endDate, location, organizer, capacity);
    }
}

