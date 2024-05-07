package com.example.greenta.Services;

import com.example.greenta.Interfaces.IServices;
import com.example.greenta.Models.Reservation;
import com.example.greenta.Models.Event;
import com.example.greenta.Utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationService implements IServices<Reservation> {
    Connection connection = MyConnection.getInstance().getConnection();

    @Override
    public void add(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservation (event_id, user_id, reservation_date) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, reservation.getEvent().getId());
        statement.setInt(2, reservation.getUser().getId());
        statement.setTimestamp(3, Timestamp.valueOf(reservation.getReservationDate()));
        statement.executeUpdate();

        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            reservation.setId(generatedKeys.getInt(1));
        }
    }

    @Override
    public void update(Reservation reservation) throws SQLException {
        String sql = "update reservation set event_id=?, reservation_date=? where id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, reservation.getEvent().getId());
        preparedStatement.setTimestamp(2, Timestamp.valueOf(reservation.getReservationDate()));
        preparedStatement.setInt(3, reservation.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        // Delete reservation
        String sql = "delete from reservation where id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Reservation> select() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "select * from reservation";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            Reservation reservation = new Reservation();
            reservation.setId(resultSet.getInt("id"));

            int eventId = resultSet.getInt("event_id");
            // Assuming the event is loaded elsewhere
            Event event = getEventById(eventId);
            reservation.setEvent(event);

            reservation.setReservationDate(resultSet.getTimestamp("reservation_date").toLocalDateTime());
            reservations.add(reservation);
        }
        return reservations;
    }

    private Event getEventById(int eventId) throws SQLException {
        String sql = "select * from event where id=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, eventId);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            Event event = new Event();
            event.setId(resultSet.getInt("id"));
            event.setTitle(resultSet.getString("title"));
            //event.setDescription(resultSet.getString("description"));
            // Set other event attributes as needed
            return event;
        }
        return null;
    }
}
