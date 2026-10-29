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

    private Validation() {
    }

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isValidEmail(String s) {
        return s != null && EMAIL.matcher(s.trim()).matches();
    }

    
    public static boolean isValidNuid(String s) {
        return s != null && s.trim().matches("\\d{" + NUID_LENGTH + "}");
    }

    public static boolean isValidUsername(String s) {
        return s != null && s.trim().length() >= MIN_USERNAME_LENGTH && !s.trim().contains(" ");
    }

    public static boolean isValidPassword(String s) {
        return s != null && s.length() >= MIN_PASSWORD_LENGTH;
    }

    /** Parses yyyy-MM-dd, returns null if the text is not a valid date. */
    public static LocalDate parseDate(String s) {
        try {
            return LocalDate.parse(s.trim());
        } catch (DateTimeParseException | NullPointerException e) {
            return null;
        }
    }
    
}
