package com.well_sync.tests.business;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.well_sync.logic.DailyLogHandler;
import com.well_sync.logic.DailyLogValidator;
import com.well_sync.logic.IDailyLogHandler;
import com.well_sync.logic.IPatientHandler;
import com.well_sync.logic.PatientHandler;
import com.well_sync.logic.ValidationUtils;
import com.well_sync.logic.exceptions.InvalidDailyLogException;
import com.well_sync.objects.DailyLog;
import com.well_sync.objects.Patient;
import com.well_sync.persistence.IDailyLogPersistence;
import com.well_sync.persistence.stubs.DailyLogPersistenceStub;
import com.well_sync.persistence.stubs.UserPersistenceStub;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyLogHandlerTest {

    private DailyLogHandler dailyLogHandler;
    private IPatientHandler patientHandler; // needed to recall patients

    @Before
    public void setup() {
        System.out.println("Starting test for DailyLogHandler");
        dailyLogHandler = new DailyLogHandler(new DailyLogPersistenceStub());
        patientHandler = new PatientHandler(new UserPersistenceStub());

        // same as in Android config file
        DailyLogValidator.setMaxima(4, 16, 5, 500, 10, 500);
        ValidationUtils.setMaxNotesLength(1000);
    }

    @Test
    public void testGetDailyLog() {
        System.out.println("\nStarting testGetDailyLog");

        // retrieving known DailyLog
        Date date = new Date(123, Calendar.JANUARY, 1);
        Patient patient = patientHandler.getDetails("patient1@example.com");
        DailyLog log = dailyLogHandler.getDailyLog(patient, "2023-01-01");

        assertNotNull(log);
        assertEquals(date, log.getDate());
        assertEquals(7, log.getMoodScore());
        assertEquals(8, log.getSleepHours());
        assertEquals("Feeling good today", log.getNotes());

        System.out.println("Finished testGetDailyLog");
    }

    @Test
    public void testSetDailyLog() throws InvalidDailyLogException {
        System.out.println("\nStarting testSetDailyLog...");

        // set valid log for patient
        Patient patient = new Patient("new-patient@example.com");
        DailyLog dailyLog = new DailyLog("2024-01-17", 2, 4, "Very tired");

        dailyLogHandler.setDailyLog(patient, dailyLog);

        // set invalid log
        dailyLog = new DailyLog("2024-01-18", -42, -1, "weird day");

        try {
            dailyLogHandler.setDailyLog(patient, dailyLog);
            fail("Setting an invalid daily log did not raise an exception.");
        } catch (InvalidDailyLogException e) {
            // pass!
        }

        System.out.println("Finished testSetDailyLog.");
    }

    @Test
    public void testGetAllDates() {
        Patient p1 = patientHandler.getDetails("patient1@example.com");
        List<String> datesActual = dailyLogHandler.getAllDatesAsString(p1);
        List<String> datesExpected = new ArrayList<String>() {{
            add("2023-01-01");
            add("2023-01-02");
        }};

        assertEquals(datesExpected, datesActual);

        String newDate = "2023-01-17";
        DailyLog newLog = new DailyLog(newDate, 2, 6, "meh");
        try {
            dailyLogHandler.setDailyLog(p1, newLog);
        } catch (InvalidDailyLogException e) {
            fail(e.getMessage());
        }

        datesActual = dailyLogHandler.getAllDatesAsString(p1);
        datesExpected.add(newDate);
        assertEquals(datesExpected, datesActual);
    }

    @Test
    public void testSetMedication() {
        IDailyLogPersistence mockedPersistence = mock(IDailyLogPersistence.class);
        IDailyLogHandler toTest = new DailyLogHandler(mockedPersistence);

        Date currDate = new Date();
        Patient p1 = new Patient("mock1@example.com");
        DailyLog dl1 = new DailyLog();

        try {
            dl1.addMedication("ibuprofen", 2, 200);
            toTest.setMedication(p1, dl1);
        } catch (InvalidDailyLogException e) {
            fail(e.getMessage());
        }

        when(mockedPersistence.getDailyLog(p1, currDate)).thenReturn(dl1);

        DailyLog dl2 = toTest.getDailyLog(p1, currDate);

        verify(mockedPersistence).setMedication(p1, dl1);
        verify(mockedPersistence).getDailyLog(p1, currDate);

        assertEquals(dl1, dl2);
    }

    @Test
    public void testSetSymptom() {
        IDailyLogPersistence mockedPersistence = mock(IDailyLogPersistence.class);
        IDailyLogHandler toTest = new DailyLogHandler(mockedPersistence);

        Date currDate = new Date();
        Patient p1 = new Patient("mock1@example.com");
        DailyLog dl1 = new DailyLog();

        try {
            dl1.addSymptom("headache", 3);
            toTest.setSymptoms(p1, dl1);
        } catch (InvalidDailyLogException e) {
            fail(e.getMessage());
        }

        when(mockedPersistence.getDailyLog(p1, currDate)).thenReturn(dl1);

        DailyLog dl2 = toTest.getDailyLog(p1, currDate);

        verify(mockedPersistence).setSymptoms(p1, dl1);
        verify(mockedPersistence).getDailyLog(p1, currDate);

        assertEquals(dl1, dl2);
    }

    @Test
    public void testSetSubstance() {
        IDailyLogPersistence mockedPersistence = mock(IDailyLogPersistence.class);
        IDailyLogHandler toTest = new DailyLogHandler(mockedPersistence);

        Date currDate = new Date();
        Patient p1 = new Patient("mock1@example.com");
        DailyLog dl1 = new DailyLog();

        try {
            dl1.addSubstance("caffeine", 5);
            toTest.setSubstances(p1, dl1);
        } catch (InvalidDailyLogException e) {
            fail(e.getMessage());
        }

        when(mockedPersistence.getDailyLog(p1, currDate)).thenReturn(dl1);

        DailyLog dl2 = toTest.getDailyLog(p1, currDate);

        verify(mockedPersistence).setSubstances(p1, dl1);
        verify(mockedPersistence).getDailyLog(p1, currDate);

        assertEquals(dl1, dl2);
    }

    @Test
    public void testGetAverageSleep() {
        IDailyLogPersistence mockedPersistence = mock(IDailyLogPersistence.class);
        IDailyLogHandler toTest = new DailyLogHandler(mockedPersistence);

        Patient p1 = new Patient("mock2@example.com");

        DailyLog dl1 = new DailyLog("2024-03-21", 3, 7, "");
        DailyLog dl2 = new DailyLog("2024-03-22", 3, 9, "");
        DailyLog dl3 = new DailyLog("2024-03-23", 3, 10, "");
        List<DailyLog> logList = new ArrayList<>();
        logList.add(dl1);
        logList.add(dl2);
        logList.add(dl3);

        when(mockedPersistence.getAllDailyLogs(p1)).thenReturn(logList);
        double avgSleep = toTest.getAverageSleep(p1);
        verify(mockedPersistence).getAllDailyLogs(p1);

        assertEquals(26.0/3, avgSleep, 0.1);
    }

    @Test
    public void testGetAverageSymptoms() throws InvalidDailyLogException {
        IDailyLogPersistence mockedPersistence = mock(IDailyLogPersistence.class);
        IDailyLogHandler toTest = new DailyLogHandler(mockedPersistence);

        Patient p1 = new Patient("mock1@example.com");

        DailyLog dl1 = new DailyLog("2024-03-21", 3, 7, "");
        DailyLog dl2 = new DailyLog("2024-03-22", 3, 9, "");
        DailyLog dl3 = new DailyLog("2024-03-23", 3, 10, "");
        List<DailyLog> logList = new ArrayList<>();
        logList.add(dl1);
        logList.add(dl2);
        logList.add(dl3);

        dl1.addSymptom("delusions", 4);
        dl2.addSymptom("delusions", 3);
        dl3.addSymptom("delusions", 1);
        dl1.addSymptom("tennis elbow", 2);
        dl2.addSymptom("tennis elbow", 5);
        dl3.addSymptom("tennis elbow", 4);

        Map<String, Float> expected = new HashMap<>();
        expected.put("delusions", 8f/3);
        expected.put("tennis elbow", 11f/3);

        when(mockedPersistence.getAllDailyLogs(p1)).thenReturn(logList);
        Map<String, Float> actual = toTest.getAverageSymptoms(p1);
        verify(mockedPersistence).getAllDailyLogs(p1);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetAllMoodScores() {
        IDailyLogPersistence mockedPersistence = mock(IDailyLogPersistence.class);
        IDailyLogHandler toTest = new DailyLogHandler(mockedPersistence);

        Patient p1 = new Patient("mock1@example.com");

        DailyLog dl1 = new DailyLog("2024-03-21", 1, 7, "");
        DailyLog dl2 = new DailyLog("2024-03-22", 2, 9, "");
        DailyLog dl3 = new DailyLog("2024-03-23", 3, 10, "");
        List<DailyLog> logList = new ArrayList<>();
        logList.add(dl1);
        logList.add(dl2);
        logList.add(dl3);

        float[] expected = {1, 2, 3};

        when(mockedPersistence.getAllDailyLogs(p1)).thenReturn(logList);
        float[] actual = toTest.getAllMoodScores(p1);
        verify(mockedPersistence).getAllDailyLogs(p1);

        assertArrayEquals(expected, actual, 0f);
    }

    @Test
    public void testInvalidDailyLogs() {
        IDailyLogPersistence mockedPersistence = mock(IDailyLogPersistence.class);
        IDailyLogHandler toTest = new DailyLogHandler(mockedPersistence);

        Patient p1 = new Patient("mock1@example.com");

        List<DailyLog> logList = new ArrayList<DailyLog>() {{
            // null object
            add(null);
            // invalid date
            add(new DailyLog("invalid date", 3, 7, "hi"));
            // future date
            add(new DailyLog("2038-01-31", 3, 7, "hi"));
            // invalid mood score
            add(new DailyLog("2024-03-21", -99, 7, "hi"));
            // invalid sleep hours
            add(new DailyLog("2024-03-21", 3, 25, "hi"));
            // invalid notes
            add(new DailyLog("2024-03-21", 3, 7, null));
        }};

        for (DailyLog log : logList) {
            try {
                toTest.setDailyLog(p1, log);
                fail("Invalid daily log did not throw an exception");
            } catch (InvalidDailyLogException e) {
                // pass!
            }
        }
    }
}
