package com.well_sync.logic;

import com.well_sync.logic.exceptions.InvalidCredentialsException;
import com.well_sync.objects.UserCredentials;

import java.util.regex.Pattern;

/**
 * Package-private utility class to validate fields in the UserCredentials DSO.
 */
abstract class UserCredentialsValidator {

    /**
     * Validate credentials object and all its components.
     */
    public static void validateCredentials(UserCredentials credentials) throws InvalidCredentialsException {
        if (credentials == null)
            throw new InvalidCredentialsException("Credentials object undefined.");

        ValidationUtils.validateEmail(InvalidCredentialsException.class, credentials.getEmail());
        validatePassword(credentials.getPassword());
    }

    /**
     * Password must be 8-64 characters long to be valid, and contain no spaces.
     */
    public static void validatePassword(String password) throws InvalidCredentialsException {
        if (password == null)
            throw new InvalidCredentialsException("No password specified.");
        if (!Pattern.matches("((?=.)[^ ]){8,64}", password))
            throw new InvalidCredentialsException("Invalid password. Must be between 8 and 64 characters.");
    }
}
