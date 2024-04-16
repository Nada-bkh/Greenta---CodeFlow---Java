package com.example.greenta.Services.Events;

import utils.MyDataBase;
import com.example.greenta.Models.User;
import com.example.greenta.Models.Events.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventService implements IService<Event> {
    Connection connection;

    public EventService() {
        connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void add(Event event) throws SQLException {
        String sql = "insert into event (title, description, start_date, end_date, location, organizer_id, capacity) values (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, event.getTitle());
        statement.setString(2, event.getDescription());
        statement.setTimestamp(3, Timestamp.valueOf(event.getStartDate()));
        statement.setTimestamp(4, Timestamp.valueOf(event.getEndDate()));
        statement.setString(5, event.getLocation());
        statement.setInt(6, event.getOrganizer().getId());
        statement.setInt(7, event.getCapacity());
        statement.executeUpdate();

        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            event.setId(generatedKeys.getInt(1));
        }
    }

    @Override
    public void update(Event event) throws SQLException {
        String sql = "update event set title=?, description=?, start_date=?, end_date=?, location=?, organizer_id=?, capacity=? where id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, event.getTitle());
        preparedStatement.setString(2, event.getDescription());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(event.getStartDate()));
        preparedStatement.setTimestamp(4, Timestamp.valueOf(event.getEndDate()));
        preparedStatement.setString(5, event.getLocation());
        preparedStatement.setInt(6, event.getOrganizer().getId());
        preparedStatement.setInt(7, event.getCapacity());
        preparedStatement.setInt(8, event.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        // Delete event
        String sql = "delete from event where id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Event> select() throws SQLException {
        List<Event> events = new ArrayList<>();
        String sql = "select * from event";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            Event event = new Event();
            event.setId(resultSet.getInt("id"));
            event.setTitle(resultSet.getString("title"));
            event.setDescription(resultSet.getString("description"));
            event.setStartDate(resultSet.getTimestamp("start_date").toLocalDateTime());
            event.setEndDate(resultSet.getTimestamp("end_date").toLocalDateTime());
            event.setLocation(resultSet.getString("location"));

            int organizerId = resultSet.getInt("organizer_id");
            // Assuming the organizer is loaded elsewhere
            User organizer = getUserById(organizerId);
            event.setOrganizer(organizer);

            event.setCapacity(resultSet.getInt("capacity"));
            events.add(event);
        }
        return events;
    }

    private User getUserById(int userId) throws SQLException {
        String sql = "select * from user where id=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setFirstname(resultSet.getString("name"));
            // Set other user attributes as needed
            return user;
        }
        return null;
    }
}

