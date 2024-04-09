package com.example.greenta.Services;

public class ValidationService {
    public boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^\\d{8}$";
        return phoneNumber.matches(phoneRegex);
    }
    public boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";
        return password.matches(passwordRegex);
    }
    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}
