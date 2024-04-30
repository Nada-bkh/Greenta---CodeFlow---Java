package com.example.greenta.GUI;

import com.example.greenta.Exceptions.UserNotFoundException;
import com.example.greenta.Models.User;
import com.example.greenta.Services.SessionService;
import com.example.greenta.Services.UserService;
import com.example.greenta.Utils.Type;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;

public class UserListCell extends ListCell<User> {
    private final UserService userService = UserService.getInstance();
    private final SessionService sessionService = SessionService.getInstance();

    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);

        if (empty || user == null) {
            setText(null);
            setGraphic(null);
        } else {
            Type userRole = user.getRoles();
            String roleText = (userRole != null && userRole.equals(Type.ROLE_ADMIN)) ? "admin" : "client";
            String lockedText = (user.getIsActive() != null && !user.getIsActive()) ? " (locked)" : "nada";
            String userText = user.getFirstname() + " " + user.getLastname() + " " + user.getPhone() + " (" + user.getEmail() + ")\n" +
                    "Role: " + roleText +" "+ lockedText;

            setText(userText);

            if (user.getIsActive() != null && !user.getIsActive()) {
                setTextFill(Color.RED);
            } else if (user.getIsBanned() != null && user.getIsBanned()) {
                setTextFill(Color.BLUE);
            } else {
                setTextFill(Color.BLACK);
                setGraphic(null);
            }

        }
    }


    public static ListCell<User> create() {
        return new UserListCell();
    }
}
