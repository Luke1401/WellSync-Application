package com.well_sync.logic;

import com.well_sync.logic.exceptions.InvalidPatientException;
import com.well_sync.objects.Patient;

/**
 * Saves and retrieves personal information on Patient users.
 */
public interface IPatientHandler {
    Patient getDetails(String email);

    void editPatientDetails(Patient patient) throws InvalidPatientException;

    void addPatient(Patient patient) throws InvalidPatientException;

}
