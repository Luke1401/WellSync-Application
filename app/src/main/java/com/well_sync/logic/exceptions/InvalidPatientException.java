package com.well_sync.logic.exceptions;

public class InvalidPatientException extends InvalidDataException {
    public InvalidPatientException(String error) {
        super("Invalid patient details:\n" + error);
    }
}
