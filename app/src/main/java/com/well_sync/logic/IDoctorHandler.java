package com.well_sync.logic;

import com.well_sync.logic.exceptions.InvalidDoctorException;
import com.well_sync.logic.exceptions.InvalidPatientException;
import com.well_sync.objects.Doctor;
import com.well_sync.objects.Patient;

import java.util.List;

/**
 * Saves and retrieves Doctors' user details and manages the lists of patients whose
 * medical records they oversee.
 */
public interface IDoctorHandler {
    /**
     * Retrieve personal details of the doctor.
     */
    Doctor getDetails(String email);


    /**
     * Edit personal details of the doctor.
     */
    void editDetails(Doctor inputDetails) throws InvalidDoctorException;

    /**
     * Registers a given patient to a given doctor, so the doctor can view the
     * patient's records.
     */
    void addPatient(Patient patient, Doctor doctor) throws InvalidDoctorException, InvalidPatientException;

    /**
     * Deregister the given patient from the given doctor.
     */
    void removePatient(Doctor doctor, Patient patient) throws InvalidDoctorException, InvalidPatientException;

    /**
     * Gets all patients registered to the given doctor.
     */
    List<Patient> getAllPatientsForDoctor(Doctor doctor) throws InvalidDoctorException;
}
