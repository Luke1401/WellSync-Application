package com.well_sync.persistence;

import com.well_sync.objects.Patient;
import com.well_sync.objects.DailyLog;

import java.util.*;

public interface IDailyLogPersistence {
    void setDailyLog(Patient patient, DailyLog dailyLog);
    void setMedication(Patient patient, DailyLog dailyLog);
    void setSymptoms(Patient patient, DailyLog dailyLog);
    void setSubstances(Patient patient, DailyLog dailyLog);
    DailyLog getDailyLog(Patient patient, Date date);
    List<DailyLog> getAllDailyLogs(Patient patient);
    List<Date> getAllDates(Patient patient);
}
