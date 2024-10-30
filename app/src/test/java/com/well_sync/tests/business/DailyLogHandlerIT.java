package com.well_sync.tests.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.well_sync.application.Services;
import com.well_sync.logic.DailyLogHandler;
import com.well_sync.logic.DailyLogValidator;
import com.well_sync.logic.ValidationUtils;
import com.well_sync.logic.exceptions.InvalidDailyLogException;
import com.well_sync.objects.DailyLog;
import com.well_sync.objects.Patient;
import com.well_sync.tests.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DailyLogHandlerIT {
    private DailyLogHandler handler;
    private File tempDB;

    @Before
    public void setup() throws IOException {
        System.out.println("Starting integration test for DailyLogHandler");
        this.tempDB = TestUtils.copyDB();
        this.handler = new DailyLogHandler();

        assertNotNull(this.tempDB);
        assertNotNull(this.handler);

        // same as in Android config file
        DailyLogValidator.setMaxima(4, 16, 5, 500, 10, 500);
        ValidationUtils.setMaxNotesLength(1000);
    }

    @Test
    public void setAndGetLog() throws InvalidDailyLogException {
        Patient patient = new Patient("patient1@example.com");
        Date date = new Date(124, Calendar.MARCH, 6);
        DailyLog log1 = new DailyLog(date, 3, 10, "Slept in");
        log1.addSymptom("dizziness", 6);
        log1.addSubstance("alcohol", 5);
        log1.addMedication("paracetamol", 1, 400);

        handler.setMedication(patient, log1);
        handler.setSubstances(patient, log1);
        handler.setSymptoms(patient, log1);

        List<Date> list1 = handler.getAllDates(patient);
        handler.setDailyLog(patient, log1);
        List<Date> list2 = handler.getAllDates(patient);

        DailyLog log2 = handler.getDailyLog(patient, date);

        assertEquals(log1, log2);
        assertEquals(list1.size() + 1, list2.size());
    }

    @Test
    public void getAllLogs() throws InvalidDailyLogException {
        // tests the persistence layer "get all logs" function indirectly by requesting
        // the average sleep hours of all logs registered to a patient

        Patient p1 = new Patient("patient1@example.com");
        double avgSleep = handler.getAverageSleep(p1);
        double expected =(7+8+6+7+8+6+9) / 7.0;
        // Round the average to one decimal place
        expected = Math.round(expected * 10.0) / 10.0;

        // based on existing logs
        assertEquals(expected, avgSleep, 0.0001);

        DailyLog dl1 = new DailyLog("2023-03-24", 3, 9, "typical");
        DailyLog dl2 = new DailyLog("2023-03-25", 4, 11, "sleepy");
        DailyLog dl3 = new DailyLog("2023-03-26", 2, 4, "cranky");

        handler.setDailyLog(p1, dl1);
        handler.setDailyLog(p1, dl2);
        handler.setDailyLog(p1, dl3);

        avgSleep = handler.getAverageSleep(p1);
        expected =(7+8+6+7+8+6+9+9+11+4)/10.0;
        // Round the average to one decimal place
        expected = Math.round(expected * 10.0) / 10.0;
        // existing + new logs
        assertEquals((7+8+6+7+8+6+9+9+11+4)/10.0, avgSleep, 0.0001);
    }

    @After
    public void tearDown() {
        System.out.println("Reset database.");
        this.tempDB.delete();
        Services.clean();
    }
}
