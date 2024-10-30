package com.well_sync.logic;

import com.well_sync.logic.exceptions.InvalidDailyLogException;
import com.well_sync.objects.DailyLog;
import com.well_sync.objects.Patient;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Saves and retrieves health log information saved by patients to particular calendar dates.
 */
public interface IDailyLogHandler {

    /**
     * Save the entire daily log for the given patient.
     */
    void setDailyLog(Patient patient, DailyLog dailyLog) throws InvalidDailyLogException;

    /**
     * Save the medication list included in the given daily log for the given patient.
     */
    void setMedication(Patient patient, DailyLog dailyLog) throws InvalidDailyLogException;

    /**
     * Save the substance list included in the given daily log for the given patient.
     */
    void setSubstances(Patient patient, DailyLog dailyLog) throws InvalidDailyLogException;

    /**
     * Save the symptoms list included in the given daily log for the given patient.
     */
    void setSymptoms(Patient patient, DailyLog dailyLog) throws InvalidDailyLogException;

    /**
     * Retrieve the daily log recorded by the given patient on the given date.
     * Date must be given in ISO 8601 (YYYY-MM-DD) format.
     */
    DailyLog getDailyLog(Patient patient, String date);

    DailyLog getDailyLog(Patient patient, Date date);

    /**
     * Get all dates on which the given patient recorded a log.
     */
    List<Date> getAllDates(Patient patient);

    double getAverageSleep(Patient patient);
    /**
     * Get all dates on which the given patient recorded a log as Strings.
     */
    List<String> getAllDatesAsString(Patient patient);
    /**
     * Get all mood scores on which the given patient recorded a log.
     */
    float[] getAllMoodScores(Patient patient);
    /**
     * Get all sleep hours on which the given patient recorded a log.
     */
    float[] getAllSleepHours(Patient patient);
    /**
     * Get averages for all symptoms on which the given patient recorded a log.
     */
    Map<String, Float> getAverageSymptoms(Patient patient);
}
