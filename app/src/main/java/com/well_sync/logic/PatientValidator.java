package com.well_sync.logic;

import com.well_sync.logic.exceptions.InvalidNotesException;
import com.well_sync.logic.exceptions.InvalidPatientException;
import com.well_sync.objects.Patient;

/**
 * Utility class to validate fields in the Patient DSO.
 */
public abstract class PatientValidator {

    /**
     * These constants are set by UI on app startup, since these are configured in
     * Android-specific config XML files
     */
    private static int maxAge;
    public static void setMaxAge(int age) {
        maxAge = age;
    }

    public static void validatePatient(Patient patient) throws InvalidPatientException {
        if (patient == null)
            throw new InvalidPatientException("Patient details object undefined.");

        ValidationUtils.validateEmail(InvalidPatientException.class, patient.getEmail());
        ValidationUtils.validateName(InvalidPatientException.class, patient.getFirstName());
        ValidationUtils.validateName(InvalidPatientException.class, patient.getLastName());

        validateAge(patient.getAge());

        try {
            ValidationUtils.validateNotes(patient.getDoctorNotes());
        } catch (InvalidNotesException e) {
            throw new InvalidPatientException(e.getMessage());
        }

        if (patient.getBloodType() == null)
            throw new InvalidPatientException("No blood type specified.");

        if (patient.getSex() == null)
            throw new InvalidPatientException("No sex specified.");
    }


    private static void validateAge(int age) throws InvalidPatientException {
        if (age < 0 || age > maxAge)
            throw new InvalidPatientException("Invalid age specified.");
    }

}
