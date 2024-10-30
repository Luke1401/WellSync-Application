package com.well_sync.tests.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.well_sync.application.Services;
import com.well_sync.logic.DoctorHandler;
import com.well_sync.logic.IDoctorHandler;
import com.well_sync.logic.IPatientHandler;
import com.well_sync.logic.PatientHandler;
import com.well_sync.logic.PatientValidator;
import com.well_sync.logic.ValidationUtils;
import com.well_sync.logic.exceptions.InvalidCredentialsException;
import com.well_sync.logic.exceptions.InvalidDoctorException;
import com.well_sync.logic.exceptions.InvalidPatientException;
import com.well_sync.objects.Doctor;
import com.well_sync.objects.Patient;
import com.well_sync.tests.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DoctorHandlerIT {
    private IDoctorHandler doctorHandler;
    private IPatientHandler patientHandler;
    private File tempDB;

    @Before
    public void setup() throws IOException {
        System.out.println("Starting integration test for DoctorHandler");
        this.tempDB = TestUtils.copyDB();
        this.doctorHandler = new DoctorHandler();
        this.patientHandler = new PatientHandler();

        assertNotNull(this.tempDB);
        assertNotNull(this.doctorHandler);
        assertNotNull(this.patientHandler);

        PatientValidator.setMaxAge(100);
        ValidationUtils.setMaxNotesLength(100);
    }

    @Test
    public void setAndGetDoctor() throws InvalidDoctorException {
        Doctor d1 = new Doctor("new-doctor@example.com", "Gregory", "House");

        doctorHandler.editDetails(d1);

        Doctor d2 = doctorHandler.getDetails("new-doctor@example.com");

        assertEquals(d1, d2);
    }

    @Test
    public void assignAndRemovePatient() throws InvalidPatientException, InvalidDoctorException, InvalidCredentialsException {
        Doctor d1 = new Doctor("new-doctor-1@example.com", "Gregory", "House");
        doctorHandler.editDetails(d1); // creates new doctor

        List<Patient> patientList = doctorHandler.getAllPatientsForDoctor(d1);
        assertNotNull(patientList);
        assertEquals(0, patientList.size());

        Patient p1 = new Patient("new-patient-1@example.com", "Dmitri", "Mendeleev", "O", "M", 33, "hi");
        Patient p2 = new Patient("new-patient-2@example.com", "Galileo", "Galilei", "O", "M", 33, "hi");

        // necessary because assignment of patient to doctors validates existence of both patients
        // and doctors in persistence (foreign key relationship)
        patientHandler.addPatient(p1);
        patientHandler.addPatient(p2);

        doctorHandler.addPatient(p1, d1);
        doctorHandler.addPatient(p2, d1);

        patientList = doctorHandler.getAllPatientsForDoctor(d1);
        assertEquals(new ArrayList<Patient>() {{ add(p1); add(p2); }}, patientList);

        doctorHandler.removePatient(d1, p2);
        patientList = doctorHandler.getAllPatientsForDoctor(d1);
        assertEquals(new ArrayList<Patient>() {{ add(p1); }}, patientList);
    }

    @After
    public void tearDown() {
        System.out.println("Reset database.");
        this.tempDB.delete();
        Services.clean();
    }

}
