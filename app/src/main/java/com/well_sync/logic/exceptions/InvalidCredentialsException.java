package com.well_sync.logic.exceptions;

/**
 * InvalidCredentialsException
 * Thrown when the user attempts to perform data operations using invalid credentials.
 */
public class InvalidCredentialsException extends InvalidDataException {
    public InvalidCredentialsException(String error) {
        super("Invalid credentials supplied:\n" + error);
    }
}
