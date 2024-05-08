package com.example.greenta.Services;

import com.example.greenta.Models.User;
import com.example.greenta.Utils.MyDataBase;
import com.example.greenta.Models.Reservation;
import com.example.greenta.Models.Event;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationService implements IService<Reservation> {
    Connection connection;

    public ReservationService() {
        connection = MyDataBase.getInstance().getConnection();
    }

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
        String sql = "SELECT r.id, r.reservation_date, e.id AS event_id, e.title AS event_title, " +
                "u.id AS user_id, u.firstname AS user_firstname, u.lastname AS user_lastname " +
                "FROM reservation r " +
                "JOIN event e ON r.event_id = e.id " +
                "JOIN user u ON r.user_id = u.id";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            Reservation reservation = new Reservation();
            reservation.setId(resultSet.getInt("id"));

            Event event = new Event();
            event.setId(resultSet.getInt("event_id"));
            event.setTitle(resultSet.getString("event_title"));
            // Set other event attributes as needed
            reservation.setEvent(event);

            User user = new User();
            user.setId(resultSet.getInt("user_id"));
            user.setFirstname(resultSet.getString("user_firstname"));
            user.setLastname(resultSet.getString("user_lastname"));
            // Set other user attributes as needed
            reservation.setUser(user);

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

    public List<Reservation> getReservationsByEventName(String eventName) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.id, r.reservation_date, e.id AS event_id, e.title AS event_title, " +
                "u.id AS user_id, u.firstname AS user_firstname, u.lastname AS user_lastname " +
                "FROM reservation r " +
                "JOIN event e ON r.event_id = e.id " +
                "JOIN user u ON r.user_id = u.id " +
                "WHERE e.title = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, eventName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Reservation reservation = mapResultSetToReservation(resultSet);
                    reservations.add(reservation);
                }
            }
        }
        return reservations;
    }

    private Reservation mapResultSetToReservation(ResultSet resultSet) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setId(resultSet.getInt("id"));

        Event event = new Event();
        event.setId(resultSet.getInt("event_id"));
        event.setTitle(resultSet.getString("event_title"));
        // Set other event attributes as needed
        reservation.setEvent(event);

        User user = new User();
        user.setId(resultSet.getInt("user_id"));
        user.setFirstname(resultSet.getString("user_firstname"));
        user.setLastname(resultSet.getString("user_lastname"));
        // Set other user attributes as needed
        reservation.setUser(user);

        reservation.setReservationDate(resultSet.getTimestamp("reservation_date").toLocalDateTime());
        return reservation;
    }

    private User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM user WHERE id = ?";
        User user = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String password = resultSet.getString("password");
                String type = resultSet.getString("type");

                user = new User(id, firstname, lastname, email, phone, password, type );
            }
        }

        return user;
    }
    public int getReservationsCountByEventId(int eventId) throws SQLException {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM reservation WHERE event_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, eventId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            }
        }
        return count;
    }

}
