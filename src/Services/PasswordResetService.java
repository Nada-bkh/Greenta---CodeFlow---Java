package Services;

import Exceptions.SamePasswordException;
import Utils.MyConnection;
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

    public String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000); // Generate a 4-digit code
        return String.valueOf(code);
    }

    // Send SMS with verification code (Replace this with actual SMS sending code)
    public void sendVerificationCode(String phoneNumber) {
        String verificationCode = generateVerificationCode();
        verificationCodes.put(phoneNumber, verificationCode); // Store the verification code
        // Code to send SMS using an SMS service provider with the generated verificationCode
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
        String sql = "SELECT password FROM user WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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
    public void resetPassword(String email, String newPassword) throws SamePasswordException {
        if (!isNewPasswordDifferent(email, newPassword)) {
            throw new SamePasswordException("New password must be different from the old password.");
        }
        String request = "UPDATE user SET password = ? WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(request)) {
            preparedStatement.setString(1, newPassword);
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
    private void sendEmailNotification(String email) {
        // Code to send email notification using an email service provider
        System.out.println("Sending email notification to " + email);
    }

    // Complete password reset process
    public void resetPasswordProcess(String phoneNumber, String enteredCode, String email, String newPassword) throws SamePasswordException {

        resetPassword(email, newPassword);
        sendEmailNotification(email);
        System.out.println("Password reset successful.");

    }
}
