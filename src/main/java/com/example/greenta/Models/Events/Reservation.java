package com.example.greenta.Models.Events;
import java.util.Objects;
import java.time.LocalDateTime;
public class Reservation {
    private int id;
    private Event event;
    private LocalDateTime reservationDate;

    public Reservation() {
    }

    public Reservation(int id, Event event, LocalDateTime reservationDate) {
        this.id = id;
        this.event = event;
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

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", event_id=" + (event != null ? event.getId() : "null") +
                ", reservationDate=" + reservationDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return id == that.id &&
                Objects.equals(event, that.event) &&
                Objects.equals(reservationDate, that.reservationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, event, reservationDate);
    }
}
