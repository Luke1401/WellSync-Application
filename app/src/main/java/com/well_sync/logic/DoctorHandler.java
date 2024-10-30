package com.well_sync.logic;

import com.well_sync.application.Services;
import com.well_sync.logic.exceptions.InvalidDoctorException;
import com.well_sync.logic.exceptions.InvalidPatientException;
import com.well_sync.objects.Doctor;
import com.well_sync.objects.Patient;
import com.well_sync.persistence.IUserPersistence;

import java.util.List;

public class DoctorHandler implements IDoctorHandler {
    private final IUserPersistence persistUsers;

    public DoctorHandler() {
        persistUsers = Services.getUserPersistence(true);
    }

    public DoctorHandler(IUserPersistence persistence){
        persistUsers = persistence;
    }

    //getDetails function will get the patient and give the patient to the UI layer
    @Override
    public Doctor getDetails(String email) {
        return persistUsers.getDoctor(email);
    }

    //editDetails function will get the userInput and set the details into the persistence layer
    @Override
    public void editDetails(Doctor inputDetails) throws InvalidDoctorException {
        DoctorValidator.validateDoctor(inputDetails);

        //user info
        String email = inputDetails.getEmail();
        //got from the database
        Doctor d = persistUsers.getDoctor(email);

        //compare p wih inputDetails
        if (d == null || !d.equals(inputDetails)) {
            persistUsers.createDoctor(inputDetails);
        }
    }
    @Override
    public void addPatient(Patient patient, Doctor doctor) throws InvalidDoctorException, InvalidPatientException {
        PatientValidator.validatePatient(patient);
        DoctorValidator.validateDoctor(doctor);
        persistUsers.assignPatientToDoctor(patient,doctor);
    }

    @Override
    public void removePatient(Doctor doctor, Patient patient) throws InvalidDoctorException, InvalidPatientException {
        PatientValidator.validatePatient(patient);
        DoctorValidator.validateDoctor(doctor);
        persistUsers.removePatientFromDoctor(doctor, patient);
    }

    @Override
    public List<Patient> getAllPatientsForDoctor(Doctor doctor) throws InvalidDoctorException {
        DoctorValidator.validateDoctor(doctor);
        return persistUsers.getAllPatientsForDoctor(doctor);
    }
}
