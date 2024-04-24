package com.example.greenta.Services;

import com.example.greenta.Exceptions.*;
import com.example.greenta.Models.User;
import com.example.greenta.Utils.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SessionService {
    Connection connection = MyConnection.getInstance().getConnection();
    private Map<String, Integer> loginAttempts = new HashMap<>();
    private Map<String, Boolean> accountLockStatus = new HashMap<>();
    private final int MAX_LOGIN_ATTEMPTS = 3;

    private static SessionService instance;
    public static User currentUser;


    //Singleton: private constructor to prevent instantiation
    private SessionService() {
    }

    public static SessionService getInstance() {
        if (instance == null) {
            instance = new SessionService();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // Set the current logged-in user
    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public boolean login(String email, String password) throws EmptyFieldException, InvalidEmailException, IncorrectPasswordException, UserNotFoundException, AccountLockedException {

        UserService userService = UserService.getInstance();
        ValidationService validationService = new ValidationService();

        if (email.isEmpty() || password.isEmpty()) {
            throw new EmptyFieldException("Please enter your email and your password.");
        }
        if (!validationService.isValidEmail(email)) {
            throw new InvalidEmailException("Invalid email, please check your email address.");
        }
        if (isAccountLocked(email)) {
            throw new AccountLockedException("Account is locked. Please contact us.");
        }

        User user = userService.getUserbyEmail(email);
        if (user != null) {
            if (userService.verifyPassword(password, user.getPassword())) {
                currentUser = user;
                return true;
            } else {
                throw new IncorrectPasswordException("Password is incorrect.");
            }
        } else {
            throw new UserNotFoundException("User with email " + email + " does not exist, please check your email or" +
                    " create an account!");
        }
    }

    public void logout() {
        currentUser = null;
    }

    public boolean attempts(String email, String password) {

        try {

            if (login(email, password)) {
                // Reset login attempts if login is successful
                loginAttempts.put(email, 0);
                System.out.println("You are Logged in !");
                return true;
            } else {
                // Increment login attempts if login fails
                int attempts = loginAttempts.getOrDefault(email, 0) + 1;
                loginAttempts.put(email, attempts);

                if (attempts >= MAX_LOGIN_ATTEMPTS) {
                    // Lock the account
                    lockAccount(email);
                    System.out.println("Too many incorrect attempts. Account locked. Please contact the admin to unlock your account.");
                } else {
                    System.out.println("Attempts left: " + (MAX_LOGIN_ATTEMPTS - attempts));
                }
                return false;
            }
        } catch (EmptyFieldException | InvalidEmailException | IncorrectPasswordException | UserNotFoundException |
                 AccountLockedException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean isAccountLocked(String email) {
        return accountLockStatus.getOrDefault(email, false);
    }

    private void updateStatus(String email, boolean isActive) {
        String sql = "UPDATE user SET is_active = ? WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBoolean(1, isActive);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void lockAccount(String email) {
        updateStatus(email, false); // Set is_active to false when locking the account
        accountLockStatus.put(email, false);
    }

    public void unlockAccount(String email) {
        updateStatus(email, true); // Set is_active to true when unlocking the account
        accountLockStatus.put(email, true);
        loginAttempts.put(email, 0); // Reset login attempts
        System.out.println("Congratulations, your account has been unlocked !");
    }

    //cardview

}
