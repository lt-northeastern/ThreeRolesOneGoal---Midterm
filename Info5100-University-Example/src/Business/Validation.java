/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 *
 * @author ogdenc
 */
public class Validation {
     public static final int NUID_LENGTH = 9;
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MIN_USERNAME_LENGTH = 3;

    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    /**
     * Private constructor; Validation is a static helper class and is never instantiated.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private Validation() {
    }

    /**
     * Returns true when the text is null, empty or only spaces.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * Returns true when the text looks like an email address (name@domain.tld).
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public static boolean isValidEmail(String s) {
        return s != null && EMAIL.matcher(s.trim()).matches();
    }
    
        /**
     * A phone number is optional; when given it may contain only digits, spaces, + ( ) and -,
     * 7 to 20 characters. Used by the staff, student and faculty forms so all three follow the
     * same rule.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    public static boolean isValidPhone(String s) {
        return isBlank(s) || s.trim().matches("[0-9 +()\\-]{7,20}");
    }

    /** Message shown when isValidPhone fails. Christopher Ogden NUID: 003959375 Admin use case. */
    public static final String PHONE_RULE = "The phone number is not valid. Use 7-20 digits, spaces, + ( ) and - only.";
    
    /**
     * Returns true when the NUID is exactly 9 digits.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public static boolean isValidNuid(String s) {
        return s != null && s.trim().matches("\\d{" + NUID_LENGTH + "}");
    }

    /**
     * Returns true when the username is at least 3 characters long and contains no spaces.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public static boolean isValidUsername(String s) {
        return s != null && s.trim().length() >= MIN_USERNAME_LENGTH && !s.trim().contains(" ");
    }

    /**
     * Returns true when the password is at least 6 characters long.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public static boolean isValidPassword(String s) {
        return s != null && s.length() >= MIN_PASSWORD_LENGTH;
    }

    /**
     * Parses a date in yyyy-MM-dd format. Returns null instead of throwing when the text is
     * not a valid date.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public static LocalDate parseDate(String s) {
        try {
            return LocalDate.parse(s.trim());
        } catch (DateTimeParseException | NullPointerException e) {
            return null;
        }
    }
    
}
