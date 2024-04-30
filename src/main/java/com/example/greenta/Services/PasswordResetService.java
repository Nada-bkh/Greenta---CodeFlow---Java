package com.example.greenta.Services;

import com.example.greenta.Exceptions.IncorrectPasswordException;
import com.example.greenta.Exceptions.SamePasswordException;
import com.example.greenta.Exceptions.UserNotFoundException;
import com.example.greenta.Models.User;
import com.example.greenta.Utils.MyConnection;
import jakarta.mail.MessagingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PasswordResetService {
    Connection connection = MyConnection.getInstance().getConnection();
    private Map<String, String> verificationCodes = new HashMap<>();
    ValidationService validationService = new ValidationService();
    UserService userService = UserService.getInstance();
    private static PasswordResetService instance;

    public static PasswordResetService getInstance() {
        if (instance == null) {
            instance = new PasswordResetService();
        }
        return instance;
    }

    public String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000); // Generate a 4-digit code
        return String.valueOf(code);
    }

    // Send SMS with verification code (Replace this with actual SMS sending code)
    public void sendVerificationCode(String phoneNumber) {
        String verificationCode = generateVerificationCode();
        verificationCodes.put(phoneNumber, verificationCode); // Store the verification code
        TwilioService twilioService = new TwilioService();
        System.out.println("verificationCode = " + verificationCode);
        twilioService.sendSms(phoneNumber, verificationCode);
        verificationCodes.put(phoneNumber, verificationCode);
    }

    // Verify SMS code
    public boolean verifySMSCode(String phoneNumber, String enteredCode) {
        String storedCode = verificationCodes.get(phoneNumber); // Retrieve stored code
        if (storedCode != null) {
            return enteredCode.equals(storedCode);
        }
        return false; // If no stored code found for the phone number
    }

    // Check if the new password is different from the old password
    private boolean isNewPasswordDifferent(String email, String newPassword) {
        String request = "SELECT password FROM user WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(request)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String oldPassword = resultSet.getString("password");
                return !newPassword.equals(oldPassword);
            }
        } catch (SQLException e) {
            System.err.println();
        }
        return true; // Assume new password is different by default or handle error case
    }

    // Reset password
    public void resetPassword(String email, String newPassword) throws SamePasswordException, IncorrectPasswordException {
        if (!isNewPasswordDifferent(email, newPassword)) {
            throw new SamePasswordException("New password must be different from the old password.");
        }
        if (!validationService.isValidPassword(newPassword)) {
            throw new IncorrectPasswordException("Password must contain at least one uppercase letter, one lowercase letter, one digit, and be at least 6 characters long.");
        }
        String request = "UPDATE user SET password = ? WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(request)) {
            preparedStatement.setString(1, UserService.cryptPassword(newPassword));
            preparedStatement.setString(2, email);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Password reset successfully for email: " + email);
                // You can send an email notification here if required
            } else {
                System.out.println("Failed to reset password for email: " + email);
            }
        } catch (SQLException e) {
            System.err.println();
        }
    }

    // Send email notification (Replace this with actual email sending code)
    public void sendEmailNotification(User user) {
        MailService mailService = new MailService();
        try {
            // Assuming you have a User object available with the necessary details
//            User user = UserService.getInstance().getUserbyEmail(email);
            mailService.sendEmail(user);
        } catch (MessagingException e) {
            System.err.println("Failed to send email notification.");
        }
    }

    // Complete password reset process
    public void resetPasswordProcess(String phoneNumber, String newPassword) throws SamePasswordException, IncorrectPasswordException, UserNotFoundException {
            User user = userService.getUserbyPhoneNumber(phoneNumber);
            resetPassword(user.getEmail(), newPassword);
            sendEmailNotification(user);
            System.out.println("Password reset successful.");
    }
}
