package com.example.greenta.Services;

import com.example.greenta.Interfaces.IServices;
import com.example.greenta.Models.Event;
import com.example.greenta.Utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventService implements IServices<Event> {
    Connection connection = MyConnection.getInstance().getConnection();

    @Override
    public void add(Event event) throws SQLException {
        String sql = "INSERT INTO event (title, start_date, end_date, location, organizer, capacity, image) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, event.getTitle());
            statement.setTimestamp(2, event.getStartDate() != null ? Timestamp.valueOf(event.getStartDate()) : null);
            statement.setTimestamp(3, event.getEndDate() != null ? Timestamp.valueOf(event.getEndDate()) : null);
            statement.setString(4, event.getLocation());
            statement.setString(5, event.getOrganizer());
            statement.setInt(6, event.getCapacity());
            statement.setString(7, event.getImage());
            statement.executeUpdate();
        }
    }

    @Override
    public void update(Event event) throws SQLException {
        String sql = "UPDATE event SET title=?, start_date=?, end_date=?, location=?, organizer=?, capacity=?, image=? WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, event.getTitle());
            preparedStatement.setTimestamp(2, event.getStartDate() != null ? Timestamp.valueOf(event.getStartDate()) : null);
            preparedStatement.setTimestamp(3, event.getEndDate() != null ? Timestamp.valueOf(event.getEndDate()) : null);
            preparedStatement.setString(4, event.getLocation());
            preparedStatement.setString(5, event.getOrganizer());
            preparedStatement.setInt(6, event.getCapacity());
            preparedStatement.setString(7, event.getImage());
            preparedStatement.setInt(8, event.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM event WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Event> select() throws SQLException {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM event";
        try {
                Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Event event = new Event();
                event.setId(resultSet.getInt("id"));
                event.setTitle(resultSet.getString("title"));
                Timestamp startTimestamp = resultSet.getTimestamp("start_date");
                Timestamp endTimestamp = resultSet.getTimestamp("end_date");
                event.setStartDate(startTimestamp != null ? startTimestamp.toLocalDateTime() : null);
                event.setEndDate(endTimestamp != null ? endTimestamp.toLocalDateTime() : null);
                event.setLocation(resultSet.getString("location"));
                event.setOrganizer(resultSet.getString("organizer"));
                event.setCapacity(resultSet.getInt("capacity"));
                event.setImage(resultSet.getString("image"));
                events.add(event);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return events;
    }
}
