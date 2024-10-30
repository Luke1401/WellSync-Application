package com.well_sync.logic.exceptions;

public class InvalidNotesException extends InvalidDataException {
    public InvalidNotesException (String error) {
        super("Invalid notes field:\n" + error);
    }
}
