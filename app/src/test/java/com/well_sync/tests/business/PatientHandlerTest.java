package com.well_sync.tests.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.well_sync.logic.IPatientHandler;
import com.well_sync.logic.PatientHandler;
import com.well_sync.logic.PatientValidator;
import com.well_sync.logic.ValidationUtils;
import com.well_sync.logic.exceptions.InvalidPatientException;
import com.well_sync.objects.Patient;
import com.well_sync.persistence.stubs.UserPersistenceStub;

import org.junit.Before;
import org.junit.Test;

public class PatientHandlerTest {

    private IPatientHandler IPatientHandler;

    @Before
    public void setup() {
        System.out.println("Starting test for PatientHandlerTest");
        IPatientHandler = new PatientHandler(new UserPersistenceStub());

        // same as in Android config file
        PatientValidator.setMaxAge(122);
        ValidationUtils.setMaxNotesLength(1000);
    }

    @Test
    public void testGetDetails() {
        System.out.println("\nStarting testGetDetails");

        String email = "patient1@example.com";
        Patient patient = IPatientHandler.getDetails(email);

        assertEquals(patient.getEmail(), email);
        assertEquals(patient.getFirstName(), "John");
        assertEquals(patient.getLastName(), "Doe");
        assertEquals(patient.getBloodType(), Patient.BloodType.TYPE_A);
        assertEquals(patient.getSex(), Patient.Sex.MALE);
        assertEquals(patient.getAge(), 30);

        System.out.println("Finished testGetDetails");
    }

    @Test
    public void testAddPatient() throws InvalidPatientException {
        System.out.println("\nStarting testAddPatient");

        String email = "new-patient@example.com";
        Patient patientSet = new Patient(email, "Jane", "Newman", "O", "F", 0, "doctor says yes");
        IPatientHandler.addPatient(patientSet);
        Patient patientRetrieved = IPatientHandler.getDetails(email);

        assertEquals(patientSet, patientRetrieved);

        try {
            Patient invalidPatient = new Patient("invalid email address!");
            IPatientHandler.addPatient(invalidPatient);
            fail("Creating patient with malformed email address did not throw an exception.");
        } catch (InvalidPatientException e) {
            // pass!

        }

        try {
            // Invalid because of the lack of blood type information
            patientSet.setBloodType(null);
            IPatientHandler.addPatient(patientSet);
            fail("Adding patient with null blood type did not throw an exception.");
        } catch (InvalidPatientException e) {
            // pass!
        }

        try {
            // Invalid because of the lack of sex information
            patientSet.setBloodType(Patient.BloodType.TYPE_O);
            patientSet.setSex(null);
            IPatientHandler.addPatient(patientSet);
            fail("Adding patient with null sex did not throw an exception.");
        } catch (InvalidPatientException e) {
            // pass!
        }

        try {
            IPatientHandler.addPatient(null);
            fail("Adding null patient did not throw an exception.");
        } catch (InvalidPatientException e) {
            // pass!
        }

        try {
            Patient invalidNotes = new Patient(email, "Jane", "Newman", "O", "F", 0, null);
            IPatientHandler.addPatient(invalidNotes);
            fail("Adding patient with invalid notes did not throw an exception.");
        } catch (InvalidPatientException e) {
            // pass!
        }

        try {
            Patient invalidAge = new Patient(email, "Jane", "Newman", "O", "F", -10, "hello");
            IPatientHandler.addPatient(invalidAge);
            fail("Adding patient with invalid age did not throw an exception.");
        } catch (InvalidPatientException e) {
            // pass!
        }

        System.out.println("Finished testAddPatient");
    }

    @Test
    public void testEditDetails() throws InvalidPatientException {
        System.out.println("\nStarting testEditDetails");

        String email = "patient2@example.com";
        Patient patientYoung = IPatientHandler.getDetails(email);
        Patient patientOld = new Patient(
                email,
                patientYoung.getFirstName(),
                patientYoung.getLastName(),
                patientYoung.getBloodType().toString(),
                patientYoung.getSex().toString(),
                patientYoung.getAge() + 5,
                "you're getting older!"
        );

        IPatientHandler.editPatientDetails(patientOld);
        Patient test = IPatientHandler.getDetails(email);

        assertEquals(patientOld, test);

        try {
            Patient invalidPatient = new Patient("unused-wellformed-email@example.com");
            IPatientHandler.editPatientDetails(invalidPatient);
            fail("Editing details of non-existent patient did not throw an exception.");
        } catch (InvalidPatientException e) {
            // pass!
        }

        try {
            Patient invalidPatient = new Patient("invalid email address");
            IPatientHandler.editPatientDetails(invalidPatient);
            fail("Editing details given an invalid email address did not throw an exception.");
        } catch (InvalidPatientException e) {
            // pass!
        }

        System.out.println("Finished testEditDetails");
    }
}