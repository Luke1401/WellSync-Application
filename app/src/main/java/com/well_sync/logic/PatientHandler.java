package com.well_sync.logic;

import com.well_sync.application.Services;
import com.well_sync.logic.exceptions.InvalidPatientException;
import com.well_sync.objects.Patient;
import com.well_sync.persistence.IUserPersistence;

public class PatientHandler implements IPatientHandler {

	private final IUserPersistence persistUsers;

	public PatientHandler() {
		persistUsers = Services.getUserPersistence(true);
	}

	public PatientHandler(IUserPersistence persistence) {
		persistUsers = persistence;
	}

	@Override
	public Patient getDetails(String email) {
		return persistUsers.getPatient(email);
	}

	/**
	 * Edit existing patient details.
	 */
	@Override
	public void editPatientDetails(Patient patient) throws InvalidPatientException {
		PatientValidator.validatePatient(patient);
		String email = patient.getEmail();
		Patient existingPatient = persistUsers.getPatient(email);

		if (existingPatient != null) {
			persistUsers.editPatientDetails(patient);
		} else {
			addPatient(patient);
		}
	}

	/**
	 * Register new patient details in the database.
	 */
	@Override
	public void addPatient(Patient patient) throws InvalidPatientException {
		PatientValidator.validatePatient(patient);
		Patient existingPatient = persistUsers.getPatient(patient.getEmail());

		if (existingPatient == null) {
			persistUsers.createPatient(patient);
			System.out.println("Patient added successfully.");
		} else {
			throw new InvalidPatientException("Patient with email " + patient.getEmail() + " already exists.");
		}
	}
}
