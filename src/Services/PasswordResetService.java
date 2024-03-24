package Services;

import Utils.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PasswordResetService {
    Connection connection = MyConnection.getInstance().getConnection();
    private Map<String, String> verificationCodes = new HashMap<>();

    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Generate a 6-digit code
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


    // Reset password
    public void resetPassword(String email, String newPassword) {
        String request = "UPDATE user SET password = ? WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error updating user: " + ex.getMessage());
        }

    }

    // Send email notification (Replace this with actual email sending code)
    private void sendEmailNotification(String email) {
        // Code to send email notification using an email service provider
        System.out.println("Sending email notification to " + email);
    }

    // Complete password reset process
    public void resetPasswordProcess(String phoneNumber, String enteredCode, String email, String newPassword) {

        resetPassword(email, newPassword);
        sendEmailNotification(email);
        System.out.println("Password reset successful.");

    }
}
