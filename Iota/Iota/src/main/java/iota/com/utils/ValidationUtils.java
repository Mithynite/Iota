package iota.com.utils;

public class ValidationUtils {
    public static void validateEmail(String email) throws IllegalArgumentException {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format!");
        }
    }

    public static void validatePhone(String phone) throws IllegalArgumentException {
        if (phone == null || !phone.matches("\\d{10,15}")) {
            throw new IllegalArgumentException("Invalid phone number format!");
        }
    }
}

