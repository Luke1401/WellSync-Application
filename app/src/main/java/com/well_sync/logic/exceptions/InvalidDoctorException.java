package com.well_sync.logic.exceptions;

public class InvalidDoctorException extends InvalidDataException {
        public InvalidDoctorException(String error) {
            super("Invalid doctor details:\n" + error);
        }
}
