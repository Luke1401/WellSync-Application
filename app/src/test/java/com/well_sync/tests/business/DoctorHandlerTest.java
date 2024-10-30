package com.well_sync.tests.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.well_sync.logic.DoctorHandler;
import com.well_sync.logic.IDoctorHandler;
import com.well_sync.logic.exceptions.InvalidDoctorException;
import com.well_sync.logic.exceptions.InvalidPatientException;
import com.well_sync.objects.Doctor;
import com.well_sync.objects.Patient;
import com.well_sync.persistence.IUserPersistence;
import com.well_sync.persistence.stubs.UserPersistenceStub;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoctorHandlerTest {

    private IDoctorHandler doctorHandler;

    @Before
    public void setup() {
        System.out.println("Starting test for DoctorHandler");
        doctorHandler = new DoctorHandler(new UserPersistenceStub());
    }

    @Test
    public void testGetDetails() {
        System.out.println("\nStarting testGetDetails");

        String email = "doctor1@example.com";
        Doctor doctor = doctorHandler.getDetails(email);

        assertEquals("doctor1@example.com", doctor.getEmail());
        assertEquals("Dr. Gabriel", doctor.getFirstName());
        assertEquals("Young", doctor.getLastName());

        System.out.println("Finished testGetDetails");
    }

    @Test
    public void testEditDetails() throws InvalidDoctorException {
        System.out.println("\nStarting testEditDetails");

        // this should succeed
        Doctor doctor1 = new Doctor("new-doctor@example.com", "Gregory", "House");
        doctorHandler.editDetails(doctor1);

        try {
            // this should fail
            Doctor doctor2 = new Doctor("invalid email address!");
            doctorHandler.editDetails(doctor2);
            fail("Creating doctor with malformed email address did not throw an exception.");
        } catch (InvalidDoctorException e) {
            // pass!
        }

        System.out.println("Finished testEditDetails");
    }

    @Test
    public void testAddRemovePatient() throws InvalidDoctorException, InvalidPatientException {
        IUserPersistence mockedPersistence = mock(IUserPersistence.class);
        IDoctorHandler toTest = new DoctorHandler(mockedPersistence);

        // Test adding two patients

        Patient p1 = new Patient("test-patient-1@example.com", "Sven", "Svensson", "O-", "M", 28, "test");
        Patient p2 = new Patient("test-patient-2@example.com", "Gunnar", "Gunnarsson", "O-", "M", 28, "test");
        Doctor d1 = new Doctor("test-doctor@example.com", "Bjorn", "Bjornsson");
        toTest.addPatient(p1, d1);
        toTest.addPatient(p2, d1);

        List<Patient> patientListExpd = new ArrayList<>(Arrays.asList(p1, p2));
        when(mockedPersistence.getAllPatientsForDoctor(d1)).thenReturn(patientListExpd);

        List<Patient> patientListActl = toTest.getAllPatientsForDoctor(d1);
        assertEquals(patientListExpd, patientListActl);

        verify(mockedPersistence).assignPatientToDoctor(p1, d1);
        verify(mockedPersistence).assignPatientToDoctor(p2, d1);
        verify(mockedPersistence).getAllPatientsForDoctor(d1);

        // Test removing one of the above patients

        toTest.removePatient(d1, p2);
        patientListExpd.remove(p2);

        patientListActl = toTest.getAllPatientsForDoctor(d1);
        assertEquals(patientListExpd, patientListActl);

        verify(mockedPersistence).removePatientFromDoctor(d1, p2);
        verify(mockedPersistence, times(2)).getAllPatientsForDoctor(d1); // second invocation
    }
}