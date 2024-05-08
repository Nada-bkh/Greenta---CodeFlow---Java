package com.example.greenta.Models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {
    private int id;
    private Event event;
    private User user;
    private LocalDateTime reservationDate;

    public Reservation() {
    }

    public Reservation(int id, Event event, User user, LocalDateTime reservationDate) {
        this.id = id;
        this.event = event;
        this.user = user;
        this.reservationDate = reservationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    // Add getters for userId and eventId
    public int getUserId() {
        return user.getId();
    }

    public int getEventId() {
        return event.getId();
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", event=" + event +
                ", user=" + user +
                ", reservationDate=" + reservationDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return id == that.id && Objects.equals(event, that.event) && Objects.equals(user, that.user) && Objects.equals(reservationDate, that.reservationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, event, user, reservationDate);
    }
}
