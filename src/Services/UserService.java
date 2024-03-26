package Services;

import Exceptions.*;
import Interfaces.UserInterface;
import Models.User;
import Utils.MyConnection;
import Utils.Type;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserService implements UserInterface {
    Connection connection = MyConnection.getInstance().getConnection();
    ValidationService validationService = new ValidationService();
    private static UserService instance;

    private UserService() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    @Override
    public void addUser(User user) throws EmptyFieldException, InvalidPhoneNumberException, InvalidEmailException, IncorrectPasswordException {
        // Vérifier que les champs obligatoires ne sont pas vides
        if (user.getFirstname().isEmpty() || user.getLastname().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
            throw new EmptyFieldException("Please fill in all required fields.");
        }
        // Valider le format de l'email
        if (!validationService.isValidEmail(user.getEmail())) {
            throw new InvalidEmailException("Invalid email, please check your email address.");
        }
        // Valider le format du numéro de téléphone (s'il est fourni)
        if (!user.getPhone().isEmpty() && !validationService.isValidPhoneNumber(user.getPhone())) {
            throw new InvalidPhoneNumberException("Invalid phone number format.");
        }
        // Valider le format du mot de passe
        if (!validationService.isValidPassword(user.getPassword())) {
            throw new IncorrectPasswordException("Password must contain at least one uppercase letter, one lowercase letter, one digit, and be at least 6 characters long.");
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
                .collect(Collectors.joining(""));
    }

    public boolean verifyPassword(String passwordToBeVerified, String encryptedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(passwordToBeVerified.toCharArray(), encryptedPassword);
        boolean verified = result.verified;
        if (!verified) {
            System.out.println("Password incorrect. Forgotten your password? ");
        }
        return verified;
    }

    @Override
    public void updateUser(User user) throws EmptyFieldException, InvalidPhoneNumberException, InvalidEmailException, IncorrectPasswordException, UserNotFoundException {
        // Check if the user object is null
        if (user == null) {
            throw new UserNotFoundException("This account doesn't exist. Cannot update account.");
        }

        // Check if the user ID is valid
        if (user.getId() <= 0) {
            throw new UserNotFoundException("This account doesn't exist. Cannot update account.");
        }

        // Check if any of the mandatory fields are empty
        if (user.getFirstname().isEmpty() || user.getLastname().isEmpty() || user.getEmail().isEmpty()) {
            throw new EmptyFieldException("Please fill in all required fields.");
        }

        // Validate email format
        if (!validationService.isValidEmail(user.getEmail())) {
            throw new InvalidEmailException("Invalid email address.");
        }

        // Validate phone number format (if provided)
        if (!user.getPhone().isEmpty() && !validationService.isValidPhoneNumber(user.getPhone())) {
            throw new InvalidPhoneNumberException("Invalid phone number format.");
        }

        if (!validationService.isValidPassword(user.getPassword())) {
            throw new IncorrectPasswordException("Password must contain at least one uppercase letter, one lowercase letter, one digit, and be at least 6 characters long.");
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

        String request = "DELETE FROM `user` WHERE `Id` =" + user.getId() + ";";
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
    public User getUserbyID(int id) throws UserNotFoundException {
        User user = null;
        try {
            String request = "SELECT * FROM user WHERE Id = " + id + "";
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            ResultSet resultSet = preparedStatement.executeQuery(request);
            if (resultSet.next()) {
                user = createUserFromResultSet(resultSet);
            } else {
                throw new UserNotFoundException("User with ID " + id + " doesn't exist.");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return user;
    }

    @Override
    public User getUserbyEmail(String email) throws UserNotFoundException {
        User user = null;
        try {
            String request = "SELECT * FROM user WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = createUserFromResultSet(resultSet);
            } else {
                throw new UserNotFoundException("User with email " + email + " doesn't exist.");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            System.err.println("Error retrieving user by email: " + ex.getMessage());
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
        String roleString = resultSet.getString("roles");
        Type roles = null;
        try {
            roles = Type.valueOf(roleString);
        } catch (IllegalArgumentException ignored) {
        }
        boolean is_banned = resultSet.getBoolean(8);
        return new User(id, firstname, lastname, email, phone, password, roles, is_banned);
    }

    public void banUser(User admin, User clientToBan) throws PermissionException, UserNotFoundException {
        // Check if admin has permission to ban
        if (admin.getRoles() != Type.ROLE_ADMIN) {
            throw new PermissionException("You don't have permission to ban " + clientToBan.getFirstname());
        }

        // Check if clientToUnban is already unbanned
        if (clientToBan.getIsBanned()) {
            System.out.println("This user is already banned.");
            return;
        }

        try {
            // Update the database to unban the user
            String query = "UPDATE user SET is_banned = true WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, clientToBan.getId());
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                clientToBan.setIsBanned(true); // Update the User object
                System.out.println(clientToBan.getFirstname() + " has been banned.");
            } else {
                throw new UserNotFoundException("User to ban not found.");
            }
        } catch (SQLException ex) {
            System.err.println("Error banning user: " + ex.getMessage());
        }
    }

    public void unbanUser(User admin, User clientToUnban) throws PermissionException, UserNotFoundException {
        // Check if admin has permission to unban
        if (admin.getRoles() != Type.ROLE_ADMIN) {
            throw new PermissionException("You don't have permission to unban " + clientToUnban.getFirstname());
        }

        // Check if clientToUnban is already unbanned
        if (!clientToUnban.getIsBanned()) {
            System.out.println("This user is already unbanned.");
            return;
        }

        try {
            // Update the database to unban the user
            String query = "UPDATE user SET is_banned = false WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, clientToUnban.getId());
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                clientToUnban.setIsBanned(false); // Update the User object
                System.out.println(clientToUnban.getFirstname() + " has been unbanned.");
            } else {
                throw new UserNotFoundException("User to unban not found.");
            }
        } catch (SQLException ex) {
            System.err.println("Error unbanning user: " + ex.getMessage());
        }
    }
}
