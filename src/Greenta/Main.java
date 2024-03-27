package Greenta;

import Exceptions.*;
import Models.User;
import Services.MailService;
import Services.PasswordResetService;
import Services.SessionService;
import Services.UserService;
import jakarta.mail.MessagingException;

public class Main {
    public static void main(String[] args) throws UserNotFoundException {
        UserService userService = UserService.getInstance();
        SessionService sessionService = SessionService.getInstance();
/*
        // ----LOGIN TEST----
        try{
            String email = "greenta@gmail.com;
            String password = "greenta";
            if (sessionService.login(email, password)) {
                System.out.println("Login successful!");
                User currentUser = sessionService.getCurrentUser();
                if (currentUser != null) {
                    System.out.println("Current User: " + currentUser.getEmail());
                }
            } else {
                System.out.println("Login failed!");
            }
        } catch (EmptyFieldException | AccountLockedException | InvalidEmailException  | IncorrectPasswordException
                 | UserNotFoundException e) {
            System.out.println( e.getMessage());
    }

        //----LOGOUT TEST----
         sessionService.logout();
         System.out.println("Logged out.");
*/

        // ----VERIFY PASSWORD TEST ----
        // boolean isValid = userService.verifyPassword("Nada123", "$2y$13$Nbmf/vynL4bW6dl4/WqNHesLY43rTbT5W/gZq9OzPEM2cjRdISOIe");


        //----CRUD TEST----
       /*
         User user = new User(52,"a","a","a@gmail.com","12345678","nada123");
            userService.addUser(user);
            userService.deleteUser(user);
            userService.updateUser( user);
            System.out.println(userService.getUserbyID(50));
            System.out.println(userService.getUserbyEmail("r@h.com"));
            System.out.println(userService.getUsers());
        */
        //---------LOGIN TRY---------
        /*try {
            System.out.println("Test case 1 - Valid credentials:");
            if (sessionService.login("a@gmail.com", "Nada123")) {
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

        } catch (EmptyFieldException | AccountLockedException | InvalidEmailException  | IncorrectPasswordException | UserNotFoundException e) {
            // Handle exceptions
            System.out.println( e.getMessage());
        }
    }*/
       /* // ----Login attempts TEST----
        sessionService.attempts("a@gmail.com", "wrongpassword");
        sessionService.attempts("a@gmail.com", "wrongpassword");
        sessionService.attempts("a@gmail.com", "wrongpassword"); // Account should be locked now

        sessionService.unlockAccount("a@gmail.com");*/

        /*
        PasswordResetService passwordResetService = new PasswordResetService();
        String phoneNumber = "1234567890"; // User's phone number
        String email = "user@example.com"; // User's email
        String newPassword = "newPassword123"; // New password
        String verificationCode = passwordResetService.generateVerificationCode();
        passwordResetService.sendVerificationCode(phoneNumber);
        // Simulate user entering the verification code
        String enteredCode = "123456"; // User's input for the verification code
        passwordResetService.resetPasswordProcess(phoneNumber, enteredCode, email, newPassword);
        */
       /*
        //------USER BAN TEST------
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
            System.out.println( e.getMessage());
        }
        */
        User user = userService.getUserbyEmail("antika.application@gmail.com");
        MailService mailService = new MailService();

        try {
            // Send email to the user
            mailService.sendEmail(user);
            System.out.println("Email sent successfully to: " + user.getEmail());
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}