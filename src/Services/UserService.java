package Services;

import Interfaces.UserInterface;
import Models.User;
import Utils.MyConnection;
import at.favre.lib.crypto.bcrypt.BCrypt;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserService implements UserInterface {
    Connection connection = MyConnection.getInstance().getConnection();
    public static User currentUser;

    public boolean login(String email, String password) {
        //email and password not empty
        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Please enter your email and your password.");
            return false;
        }
        //email format
        if (!isValidEmail(email)) {
            System.out.println("Email adress invalid.");
            return false;
        }

        if ("nadaa@gmail.com".equals(email) && "$2y$13$As35MHz5TzMJYgPHQL96KuyGSWGzyYrNROizbX9SeNJQU8OikJI/S".equals(password)) {
            currentUser = new User("nadaa@gmail.com", "$2y$13$As35MHz5TzMJYgPHQL96KuyGSWGzyYrNROizbX9SeNJQU8OikJI/S");
            return true;
        } else {
            System.out.println("Credentials invalid.");
            return false;
        }
    }
    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void addUser(User user) {
        // Vérifier que les champs obligatoires ne sont pas vides
        if (user.getFirstname().isEmpty() || user.getLastname().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
            System.out.println("Please fill in all required fields.");
            return;
        }
        // Valider le format de l'email
        if (!isValidEmail(user.getEmail())) {
            System.out.println("Invalid email address.");
            return;
        }
        // Valider le format du numéro de téléphone (s'il est fourni)
        if (!user.getPhone().isEmpty() && !isValidPhoneNumber(user.getPhone())) {
            System.out.println("Phone number should contain at least 8 digits.");
            return;
        }

        // Valider le format du mot de passe
        if (!isValidPassword(user.getPassword())) {
            System.out.println("Password must contain at least one uppercase letter, one lowercase letter, one digit, and be at least 6 characters long.");
            return;
        }
        String request = "INSERT INTO `user`(`firstname`, `lastname`, `email` ,`phone`,`password`, `roles`) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPhone());
            preparedStatement.setString(5, cryptPassword(user.getPassword()));
            preparedStatement.setString(6, user.getRoles().toString());
            preparedStatement.executeUpdate();
            System.out.println("User added successfully !");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private String cryptPassword(String passwordToCrypt) {
        char[] bcryptChars = BCrypt.with(BCrypt.Version.VERSION_2Y).hashToChar(13, passwordToCrypt.toCharArray());
        return Stream
                .of(bcryptChars)
                .map(String::valueOf)
                .collect(Collectors.joining( "" ));
    }

    public boolean verifyPassword(String passwordToBeVerified, String encryptedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(passwordToBeVerified.toCharArray(), encryptedPassword);
        boolean verified = result.verified;
        if (!verified) {
            System.out.println("Password incorrect, forgot your password?");
        }
        return verified;
    }


    @Override
    public void updateUser( User user) {
        // Check if the user object is null
        if (user == null) {
            System.out.println("User object is null. Cannot update user.");
            return;
        }

        // Check if the user ID is valid
        if (user.getId() <= 0) {
            System.out.println("Invalid user ID. Cannot update user.");
            return;
        }

        // Check if any of the mandatory fields are empty
        if (user.getFirstname().isEmpty() || user.getLastname().isEmpty() || user.getEmail().isEmpty()) {
            System.out.println("Please fill in all required fields.");
            return;
        }

        // Validate email format
        if (!isValidEmail(user.getEmail())) {
            System.out.println("Invalid email address.");
            return;
        }

        // Validate phone number format (if provided)
        if (!user.getPhone().isEmpty() && !isValidPhoneNumber(user.getPhone())) {
            System.out.println("Invalid phone number format.");
            return;
        }

        if (!isValidPassword(user.getPassword())) {
            System.out.println("Password must contain at least one uppercase letter, one lowercase letter, one digit, and be at least 6 characters long.");
            return;
        }

        // Prepare SQL update statement
        String request = "UPDATE user SET firstname = ?, lastname = ?, email = ?, phone = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPhone());
            preparedStatement.setInt(5, user.getId());

            // Execute the update statement
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User updated successfully!");
            } else {
                System.out.println("Failed to update user. User not found or no changes made.");
            }
        } catch (SQLException ex) {
            System.err.println("Error updating user: " + ex.getMessage());
        }
    }

    @Override
    public void deleteUser(User user) {

        String request = "DELETE FROM `user` WHERE `Id` ="+user.getId()+";";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(request);
            System.out.println("User is deleted successfully");

        } catch (SQLException ex) {
            System.err.println();
        }
}

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        try {
            String request = "SELECT * FROM user";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(request);
            while (resultSet.next()) {
                User user = createUserFromResultSet(resultSet);
                users.add(user);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return users;
    }

    @Override
    public User getUserbyID(int id) {
        User user = null;
        try {
            String request = "SELECT * FROM user WHERE Id = " +id+"";
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            ResultSet resultSet = preparedStatement.executeQuery(request);
            while (resultSet.next()){
                user = createUserFromResultSet(resultSet);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return user;
    }

    private User createUserFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String firstname = resultSet.getString(2);
        String lastname = resultSet.getString(3);
        String email = resultSet.getString(4);
        String phone = resultSet.getString(5);
        String password = resultSet.getString(6);
        return new User(id, firstname, lastname, email, phone, password);
    }
    //      --CONTROLE DE SAISIE--
    // Méthode utilitaire pour valider le format du numéro de téléphone
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Format valide : 10 chiffres
        String phoneRegex = "^\\d{8}$";
        return phoneNumber.matches(phoneRegex);
    }

    // Méthode utilitaire pour valider le format du mot de passe
    private boolean isValidPassword(String password) {
        // Le mot de passe doit contenir au moins une lettre majuscule, une lettre minuscule, un chiffre et être d'au moins 6 caractères
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";
        return password.matches(passwordRegex);
    }
    // Méthode utilitaire pour valider le format de l'email
    private boolean isValidEmail(String email) {
        // Utiliser une expression régulière simple pour valider le format de l'email
        String emailRegex = "^[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}
