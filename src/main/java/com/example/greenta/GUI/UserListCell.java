package com.example.greenta.GUI;

import com.example.greenta.Models.User;
import javafx.scene.control.ListCell;

public class UserListCell extends ListCell<User> {
    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);

        if (empty || user == null) {
            setText(null);
        } else {
            setText(user.getFirstname() + " " + user.getLastname() + " " + user.getPhone() + " (" + user.getEmail() + ")\n" +
                    "Role: " + user.getRoles());
        }
    }

    public static ListCell<User> create() {
        return new UserListCell();
    }
}