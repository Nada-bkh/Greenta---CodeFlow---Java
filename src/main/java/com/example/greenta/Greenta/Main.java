package com.example.greenta.Greenta;

import com.example.greenta.Exceptions.*;
import com.example.greenta.Services.*;

public class Main {
    public static void main(String[] args) throws UserNotFoundException, IncorrectPasswordException, InvalidPhoneNumberException, InvalidEmailException, EmptyFieldException {
        UserService userService = UserService.getInstance();
        SessionService sessionService = SessionService.getInstance();
        TwilioService twilioService = new TwilioService();
        MailService mailService = new MailService();
        PasswordResetService passwordResetService = new PasswordResetService();
/*
// Provide a phone number for testing
        String phoneNumber = "+21693144651";

// Send verification code to the provided phone number
        passwordResetService.sendVerificationCode(phoneNumber);

// Simulate the user receiving the SMS and entering the verification code
        String enteredCode = "6914"; // Assuming the user enters the correct verification code

// Simulate email and password for login
        String email = "antika.application@gmail.com";
        String password = "Antika123";

// Reset the password if the verification code is correct
        try {
            String newPassword = "newPassword"; // Set a new password
            passwordResetService.resetPasswordProcess(phoneNumber, enteredCode, email, newPassword);
        } catch (IncorrectPasswordException | SamePasswordException e) {
            System.out.println(e.getMessage());
        }

// Attempt login with the provided email and password
        try {
            if (sessionService.login(email, password)) {
                System.out.println("Login successful!");
                User currentUser = sessionService.getCurrentUser();
                if (currentUser != null) {
                    System.out.println("Current User: " + currentUser.getEmail());
                    try {
                        // Send email to the user
                        mailService.sendEmail(currentUser); // Pass the currentUser object
                        System.out.println("Email sent successfully to: " + currentUser.getEmail());
                    } catch (MessagingException e) {
                        System.err.println("Failed to send email: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Login failed!");
            }
        } catch (EmptyFieldException | AccountLockedException | InvalidEmailException | IncorrectPasswordException
                 | UserNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }*/
// Logout test
        /*
        sessionService.logout();
        System.out.println("Logged out.");
        */

        // Verify password test
        /*
        boolean isValid = userService.verifyPassword(currentUser.getPassword(), "$2y$13$Nbmf/vynL4bW6dl4/WqNHesLY43rTbT5W/gZq9OzPEM2cjRdISOIe");
        */

        // CRUD test
/*
        User user = new User("iheb", "a", "a@gmail.com", "12345678", "nada123", Type.ROLE_CLIENT);
        userService.addUser(user);

        userService.deleteUser(user);
        userService.updateUser(user);
        System.out.println(userService.getUserbyID(50));
        System.out.println(userService.getUserbyEmail("r@h.com"));
        System.out.println(userService.getUsers());
        */


        // Login try
        /*
        try {
            System.out.println("Test case 1 - Valid credentials:");
            if (sessionService.login(currentUser.getEmail(), currentUser.getPassword())) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Login failed!");
            }
            System.out.println();

            // Test case 2: Invalid email
            System.out.println("Test case 2 - Invalid email:");
            if (sessionService.login("t@b.b", "password")) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Login failed!");
            }
            System.out.println();

            // Test case 3: Empty fields
            System.out.println("Test case 3 - Empty fields:");
            if (sessionService.login(" ", " ")) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Login failed!");
            }
            System.out.println();

        } catch (EmptyFieldException | AccountLockedException | InvalidEmailException | IncorrectPasswordException | UserNotFoundException e) {
            // Handle exceptions
            System.out.println(e.getMessage());
        }
        */

        // Login attempts test
        /*
        sessionService.attempts(currentUser.getEmail(), "wrongpassword");
        sessionService.attempts(currentUser.getEmail(), "wrongpassword");
        sessionService.attempts(currentUser.getEmail(), "wrongpassword"); // Account should be locked now
        sessionService.unlockAccount(currentUser.getEmail());
        */
        // User ban test
        /*
        User adminUser = userService.getUserbyID(17);
        User clientUser = userService.getUserbyID(52);
        User nonAdminUser = userService.getUserbyID(39);
        User nonClientUser = userService.getUserbyID(30);
        try {
            userService.unbanUser(adminUser, clientUser);
            userService.unbanUser(adminUser, clientUser);
            userService.banUser(adminUser, clientUser);
            userService.banUser(adminUser, clientUser);
            // Test banning a user with insufficient permissions
            userService.banUser(nonAdminUser,clientUser); // This should fail with a permission error
            // Test banning a non-client user
            userService.banUser(adminUser, nonClientUser); // This should fail with a type error
        } catch (PermissionException | UserNotFoundException e) {
            System.out.println(e.getMessage());
        }
        */
    }
}