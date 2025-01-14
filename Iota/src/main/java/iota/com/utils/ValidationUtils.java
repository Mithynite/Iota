package iota.com.utils;

public class ValidationUtils {

    // Universal Regex patterns
    private static final String email_pattern = "^[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z]{2,}$";
    private static final String phone_pattern = "^\\d{8,15}$";
    private static final String name_pattern = "^[A-Za-z\\s'-]{2,50}$";
    private static final String date_patern = "^\\d{4}-\\d{2}-\\d{2}$";
    private static final String word_pattern = "^[A-Za-z\\s'-]+$";

    /**
     * Validates a name.
     */
    public static void validateName(String name, String fieldName) {
        validateNotBlank(name, fieldName);
        if (!name.matches(name_pattern)) {
            throw new IllegalArgumentException("Invalid name!");
        }
    }

    /**
     * Validates an email address.
     */
    public static void validateEmail(String email, String fieldName) {
        validateNotBlank(email, fieldName);
        if (!email.matches(email_pattern)) {
            throw new IllegalArgumentException("Invalid email format! Example: user@example.com");
        }
    }

    /**
     * Validates a phone number (digits only, 8-15 characters).
     */
    public static void validatePhone(String phone, String fieldName) {
        validateNotBlank(phone, fieldName);
        if (!phone.matches(phone_pattern)) {
            throw new IllegalArgumentException("Invalid phone number! Use only digits (8-15 characters).");
        }
    }

    /**
     * Validates generic "word-based" input (e.g., names, etc.)
     */
    public static void validateWord(String word, String fieldName) {
        validateNotBlank(word, fieldName);
        if (!word.matches(word_pattern)) {
            throw new IllegalArgumentException(fieldName + " contains invalid characters! Only letters, spaces, hyphens, and apostrophes are allowed.");
        }
    }

    /**
     * Validates a generic date in the format yyyy-MM-dd
     */
    public static void validateDate(String date, String fieldName) {
        validateNotBlank(date, fieldName);
        if (!date.matches(date_patern)) {
            throw new IllegalArgumentException("Invalid " + fieldName + " format! Use yyyy-MM-dd.");
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
    public static void validateEnum(Class<? extends Enum<?>> enumClass, String value, String fieldName) {
        validateNotBlank(value, fieldName); // Ensure the value is not null/blank
        for (Enum<?> enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.name().equalsIgnoreCase(value)) { // Case-insensitive comparison
                return; // Match found, validation passes
            }
        }
        throw new IllegalArgumentException("Invalid " + fieldName + "! Allowed values are: " + getEnumValues(enumClass));
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
    public static void validateNotBlank(String field, String fieldName) {
        if (field == null || field.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank!");
        }
    }
}
