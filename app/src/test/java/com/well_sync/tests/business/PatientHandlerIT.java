package com.well_sync.tests.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import com.well_sync.application.Services;
import com.well_sync.logic.IPatientHandler;
import com.well_sync.logic.PatientHandler;
import com.well_sync.logic.PatientValidator;
import com.well_sync.logic.ValidationUtils;
import com.well_sync.logic.exceptions.InvalidPatientException;
import com.well_sync.objects.Patient;
import com.well_sync.tests.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class PatientHandlerIT {
    private IPatientHandler handler;
    private File tempDB;

    @Before
    public void setup() throws IOException {
        System.out.println("Starting integration test for PatientHandler");
        this.tempDB = TestUtils.copyDB();
        this.handler = new PatientHandler();

        assertNotNull(this.tempDB);
        assertNotNull(this.handler);

        // same as in Android config file
        PatientValidator.setMaxAge(122);
        ValidationUtils.setMaxNotesLength(1000);
    }

    @Test
    public void addAndGetPatient() {
        Patient p1 = new Patient(
                "integration@test.com",
                "Integration",
                "Test",
                "A+",
                "decline to specify",
                22,
                "doctor approves of this test"
        );

        try {
            handler.addPatient(p1);
        } catch (InvalidPatientException e) {
            e.printStackTrace();
        }

        Patient p2 = handler.getDetails("integration@test.com");

        assertEquals(p1, p2);
    }

    @Test
    public void editAndGetPatient() {
        String email = "patient1@example.com";
        Patient p1 = handler.getDetails(email);
        Patient p2 = new Patient(email, "New", "Name", "AB-", "M", 44, "doctor approves of this test");

        try {
            handler.editPatientDetails(p2);
        } catch (InvalidPatientException e) {
            fail(e.getMessage());
        }

        Patient p3 = handler.getDetails(email);

        assertEquals(p2, p3);
        assertNotEquals(p1, p3);
    }

    @After
    public void tearDown() {
        System.out.println("Reset database.");
        this.tempDB.delete();
        Services.clean();
    }

}
