package com.well_sync.logic;

import com.well_sync.logic.exceptions.InvalidNotesException;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;

/**
 * Utility class for general helper functions related to validation.
 */
public abstract class ValidationUtils {

    /**
     * These constants are set by UI on app startup, since these are configured in
     * Android-specific config XML files
     */
    private static int maxNotesLength;
    public static void setMaxNotesLength(int length) {
        maxNotesLength = length;
    }

    /**
     * Valid name uses 2 or more alphabetic characters; nothing else.
     */
    public static <E extends Exception> void validateName(Class<E> exceptionType, String name) throws E {
        if (name == null || !Pattern.matches("[\\w ./-]{2,}", name)) {
            try {
                throw exceptionType
                        .getConstructor(String.class)
                        .newInstance("Invalid name specified");
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                     InvocationTargetException e) {
                throw new RuntimeException("Failed to instantiate invalid data exception.");
            }
        }
    }

    /**
     * Email address must contain the usual well-formed components (mailbox name,
     * host domain, and TLD suffix).
     */
    public static <E extends Exception> void validateEmail(Class<E> exceptionType, String email) throws E {

        if (email == null || !Pattern.matches("[\\w_.-]+@[\\w-]+.[a-z]{2,}", email)) {
            try {
                throw exceptionType
                        .getConstructor(String.class)
                        .newInstance("Invalid email address specified");
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                     InvocationTargetException e) {
                throw new RuntimeException("Failed to instantiate invalid data exception.");
            }
        }
    }

    public static void validateNotes(String notes) throws InvalidNotesException {
        if (notes == null)
            throw new InvalidNotesException("Absent notes field.");

        else if (notes.length() > maxNotesLength)
            throw new InvalidNotesException("Exceeded word limit.");
    }
}
