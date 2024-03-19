package Greenta;

import Models.User;
import Services.UserService;
import Utils.CustomizedExceptions.*;
import Utils.Type;

public class Main {
    public static void main(String[] args) {
        UserService userService = UserService.getInstance();
     /*  // ----LOGIN TEST----
        String email = "nadaa@gmail.com";
        String password = "$2y$13$As35MHz5TzMJYgPHQL96KuyGSWGzyYrNROizbX9SeNJQU8OikJI/S";
        if (userService.login(email, password)) {
            System.out.println("Login successful!");
            User currentUser = userService.getCurrentUser();
            if (currentUser != null) {
                System.out.println("Current User: " + currentUser.getEmail());
            }
        } else {
            System.out.println("Login failed!");
        }*/

        //----LOGOUT TEST----
        // userService.logout();
        // System.out.println("Logged out.");

        // ----VERIFY PASSWORD TEST ----
        //  boolean isValid = userService.verifyPassword("11111111", "$2y$13$As35MHz5TzMJYgPHQL96KuyGSWGzyYrNROizbX9SeNJQU8OikJI/S");


        //----CRUD TEST----
        /*  User user = new User(52,"a","a","a@gmail.com","12345678","nada123");
            userService.deleteUser(user);
            userService.updateUser( user);
            System.out.println(userService.getUserbyID(50));
            System.out.println(userService.getUsers());
         */
        try {
            System.out.println("Test case 1 - Valid credentials:");
            if (userService.login("a@gmail.com", "Nada123")) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Login failed!");
            }
            System.out.println();

            // Test case 2: Invalid email
            System.out.println("Test case 2 - Invalid email:");
            if (userService.login("t@b.b", "password")) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Login failed!");
            }
            System.out.println();

            // Test case 3: Empty fields
            System.out.println("Test case 3 - Empty fields:");
            if (userService.login("", "")) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Login failed!");
            }
            System.out.println();

        } catch (EmptyFieldException | InvalidEmailException | IncorrectPasswordException | UserNotFoundException e) {
            // Handle exceptions
            System.out.println("Error: " + e.getMessage());
        }
    }
}