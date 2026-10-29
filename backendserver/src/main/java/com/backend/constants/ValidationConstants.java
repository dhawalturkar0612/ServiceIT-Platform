package com.backend.constants;

/**
 * Centralized validation patterns used across the application.
 *
 * Storing validation regex in one place improves readability,
 * avoids duplication, and simplifies maintenance.
 */
public final class ValidationConstants {

    private ValidationConstants() {
        // Prevent instantiation
    }

    /**
     * Indian mobile number
     * Must start with 6, 7, 8, or 9 and contain exactly 10 digits.
     */
    public static final String PHONE_REGEX =
            "^[6-9]\\d{9}$";

    /**
     * Password Requirements:
     * - Minimum 8 characters
     * - At least one uppercase letter
     * - At least one lowercase letter
     * - At least one digit
     * - At least one special character
     */
    public static final String PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    
    /**
     * Indian PIN Code
     * Must contain exactly 6 digits and cannot start with 0.
     */
    public static final String PINCODE_REGEX =
            "^[1-9][0-9]{5}$";

}