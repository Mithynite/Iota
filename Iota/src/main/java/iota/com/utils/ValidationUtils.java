package iota.com.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidationUtils {

    // Universal Regex patterns
    private static final String email_pattern = "^[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z]{2,}$";
    private static final String phone_pattern = "^\\d{8,15}$";
    private static final String name_pattern = "^[A-Za-z\\s'-]{2,50}$";

    /**
     * Validates a name.
     */
    public static void validateName(String name, String fieldName, boolean canBeEmpty) {
        validateNotBlank(name, fieldName, canBeEmpty);
        if (!name.matches(name_pattern) && !canBeEmpty) {
            throw new IllegalArgumentException("Invalid name!");
        }
    }

    /**
     * Validates an email address.
     */
    public static void validateEmail(String email, String fieldName, boolean canBeEmpty) {
        validateNotBlank(email, fieldName, canBeEmpty);
        if (!email.matches(email_pattern) && !canBeEmpty) {
            throw new IllegalArgumentException("Invalid email format! Example: user@example.com");
        }
    }

    /**
     * Validates a phone number (digits only, 8-15 characters).
     */
    public static void validatePhone(String phone) {
        if (!phone.trim().isEmpty() && !phone.matches(phone_pattern)) {
                throw new IllegalArgumentException("Invalid phone number! Use only digits (8-15 characters).");
            }
    }

    /**
     * Validates a generic date in the format yyyy-MM-dd
     */
    public static void validateDate(String date, String fieldName, boolean canBeEmpty) {
        validateNotBlank(date, fieldName, canBeEmpty);

        if (!canBeEmpty) {
            // Define the date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            try {
                // Parse the date to ensure it's valid
                LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid " + fieldName + " format or value! Use 'yyyy-MM-dd'.");
            }
        }
    }

    /**
     * Validates a positive number. Throws an exception if not valid.
     */
    public static void validatePositive(float numberInput, String floatFieldName) {
        if (numberInput <= 0) {
            throw new IllegalArgumentException(floatFieldName + " must be a positive number!");
        }
    }

    /**
     * Validates a boolean input (true/false). Throws an exception if invalid.
     */
    public static void validateBoolean(String booleanInput, String fieldName) {
        if (booleanInput == null || !(booleanInput.equalsIgnoreCase("true") || booleanInput.equalsIgnoreCase("false"))) {
            throw new IllegalArgumentException("Invalid " + fieldName + "! Expected values are 'true' or 'false'.");
        }
    }

    /**
     * Validates if a value belongs to a specific enum type.
     */
    public static void validateEnum(Class<? extends Enum<?>> enumClass, String value, String fieldName, boolean canBeEmpty) {
        validateNotBlank(value, fieldName, canBeEmpty); // Ensure the value is not null/blank
        if (!canBeEmpty) {
            for (Enum<?> enumConstant : enumClass.getEnumConstants()) {
                if (enumConstant.name().equalsIgnoreCase(value)) {
                    return;
                }
            }
            throw new IllegalArgumentException("Invalid " + fieldName + "! Allowed values are: " + getEnumValues(enumClass));
        }
    }

    // Helper method to list valid enum values
    private static String getEnumValues(Class<? extends Enum<?>> enumClass) {
        Enum<?>[] enumConstants = enumClass.getEnumConstants();
        StringBuilder allowedValues = new StringBuilder();
        for (int i = 0; i < enumConstants.length; i++) {
            allowedValues.append(enumConstants[i].name());
            if (i < enumConstants.length - 1) {
                allowedValues.append(", ");
            }
        }
        return allowedValues.toString();
    }

    /**
     * Reusable method to check for blank or null fields.
     */
    public static void validateNotBlank(String field, String fieldName, boolean canBeEmpty) {
        if (field == null || (field.trim().isEmpty()) && !canBeEmpty) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank!");
        }
    }
}
