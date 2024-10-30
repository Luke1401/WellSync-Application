package com.well_sync.logic.exceptions;

/**
 * Superclass for custom exceptions used throughout the application relating to invalid data.
 * These are usually raised upon the validation of data entered by the user.
 * Note that this class extends Exception, not RuntimeException, so that it must be caught
 * explicitly by code downstream from the validation logic.
 */
public class InvalidDataException extends Exception {
    public InvalidDataException(String error) {
        super(error);
    }
}
