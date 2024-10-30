package com.well_sync.persistence.stubs;

import com.well_sync.logic.exceptions.InvalidDailyLogException;
import com.well_sync.objects.DailyLog;
import com.well_sync.objects.Patient;
import com.well_sync.persistence.IDailyLogPersistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DailyLogPersistenceStub implements IDailyLogPersistence {

    private final Map<String, List<DailyLog>> dailyLogList;

    public DailyLogPersistenceStub() {
        dailyLogList = new HashMap<>();

        // Default Sample Data
        String patient1 = "patient1@example.com";
        String patient2 = "patient2@example.com";

        List<DailyLog> dailyLogsPatient1 = new ArrayList<>();
        List<DailyLog> dailyLogsPatient2 = new ArrayList<>();

        DailyLog patient1Day1 = new DailyLog("2023-01-01", 7, 8, "Feeling good today");
        DailyLog patient1Day2 = new DailyLog("2023-01-02", 5, 7, "Had trouble sleeping");
        DailyLog patient2Day1 = new DailyLog("2023-01-01", 6, 7, "Normal day");

        try {
            patient1Day1.addSymptom("Headache", 3);
            patient1Day1.addSymptom("Sadness/Despair", 2);
            patient1Day1.addSymptom("Helplessness/Hopelessness", 2);
            patient1Day1.addSymptom("Low Self-Esteem", 4);
            patient1Day1.addSymptom("Feeling isolated", 1);
            patient1Day1.addSymptom("Low Motivation/ Loss of Interest", 0);
            patient1Day1.addSymptom("Impulsivity", 0);
            patient1Day1.addSymptom("Inability to Concentrate", 1);
            patient1Day1.addSymptom("Aggressive Behavior", 3);
            patient1Day1.addSymptom("Feeling like you can do anything", 3);
            patient1Day1.addSymptom("Racing Thoughts", 2);
            patient1Day1.addSymptom("High Anxiety/Excessive Worry", 2);
            patient1Day1.addSymptom("Sleep Problems", 2);
            patient1Day1.addSymptom("Body Ache/Pain", 3);
            patient1Day1.addSymptom("Decreased or Increased Appetit", 1);
            patient1Day1.addSymptom("Feelings of Guilt or Self-Blame", 2);
            patient1Day1.addSymptom("Thoughts of Death or Suicide", 1);

            patient1Day2.addSymptom("Headache", 3);
            patient1Day2.addSymptom("Sadness/Despair", 2);
            patient1Day2.addSymptom("Helplessness/Hopelessness", 2);
            patient1Day2.addSymptom("Low Self-Esteem", 4);
            patient1Day2.addSymptom("Feeling isolated", 1);
            patient1Day2.addSymptom("Low Motivation/ Loss of Interest", 0);
            patient1Day2.addSymptom("Impulsivity", 0);
            patient1Day2.addSymptom("Inability to Concentrate", 1);
            patient1Day2.addSymptom("Aggressive Behavior", 3);
            patient1Day2.addSymptom("Feeling like you can do anything", 3);
            patient1Day2.addSymptom("Racing Thoughts", 2);
            patient1Day2.addSymptom("High Anxiety/Excessive Worry", 2);
            patient1Day2.addSymptom("Sleep Problems", 2);
            patient1Day2.addSymptom("Body Ache/Pain", 3);
            patient1Day2.addSymptom("Decreased or Increased Appetit", 1);
            patient1Day2.addSymptom("Feelings of Guilt or Self-Blame", 2);
            patient1Day2.addSymptom("Thoughts of Death or Suicide", 1);

            patient2Day1.addSymptom("Headache", 3);
            patient2Day1.addSymptom("Sadness/Despair", 2);
            patient2Day1.addSymptom("Helplessness/Hopelessness", 2);
            patient2Day1.addSymptom("Low Self-Esteem", 4);
            patient2Day1.addSymptom("Feeling isolated", 1);
            patient2Day1.addSymptom("Low Motivation/ Loss of Interest", 0);
            patient2Day1.addSymptom("Impulsivity", 0);
            patient2Day1.addSymptom("Inability to Concentrate", 1);
            patient2Day1.addSymptom("Aggressive Behavior", 3);
            patient2Day1.addSymptom("Feeling like you can do anything", 3);
            patient2Day1.addSymptom("Racing Thoughts", 2);
            patient2Day1.addSymptom("High Anxiety/Excessive Worry", 2);
            patient2Day1.addSymptom("Sleep Problems", 2);
            patient2Day1.addSymptom("Body Ache/Pain", 3);
            patient2Day1.addSymptom("Decreased or Increased Appetit", 1);
            patient2Day1.addSymptom("Feelings of Guilt or Self-Blame", 2);
            patient2Day1.addSymptom("Thoughts of Death or Suicide", 1);


            patient1Day1.addMedication("Tylenol", 2, 3);
            patient1Day2.addMedication("Melatonin", 1, 1);
            patient2Day1.addMedication("Cough syrup", 3, 5);

            patient1Day1.addSubstance("Alcohol", 2);
            patient1Day2.addSubstance("Caffiene", 2);
            patient2Day1.addSubstance("Caffiene", 2);
        } catch (InvalidDailyLogException e) {
            e.printStackTrace();
        }

        dailyLogsPatient1.add(patient1Day1);
        dailyLogsPatient1.add(patient1Day2);
        dailyLogsPatient2.add(patient2Day1);

        dailyLogList.put(patient1, dailyLogsPatient1);
        dailyLogList.put(patient2, dailyLogsPatient2);
    }

    @Override
    public void setDailyLog(Patient patient, DailyLog dailyLog) {
        if (!dailyLogList.containsKey(patient.getEmail())) {
            dailyLogList.put(patient.getEmail(), new ArrayList<>());
        }
        Objects.requireNonNull(dailyLogList.get(patient.getEmail())).add(dailyLog);
    }
    public void setMedication(Patient patient, DailyLog dailyLog){}
    public void setSymptoms(Patient patient, DailyLog dailyLog){}
    public void setSubstances(Patient patient, DailyLog dailyLog){}

    @Override
    public DailyLog getDailyLog(Patient patient, Date date) {
        List<DailyLog> patientDailyLogs = dailyLogList.get(patient.getEmail());
        if (patientDailyLogs != null) {
            for (DailyLog dailyLog : patientDailyLogs) {
                if (dailyLog.getDate().equals(date)) {
                    return dailyLog;
                }
            }
        }
        return null;
    }

    @Override
    public List<DailyLog> getAllDailyLogs(Patient patient) {
        return dailyLogList.getOrDefault(patient.getEmail(), new ArrayList<>());
    }

    @Override
    public List<Date> getAllDates(Patient patient) {
        List<DailyLog> patientDailyLogs = dailyLogList.get(patient.getEmail());
        List<Date> allDates = new ArrayList<>();

        if (patientDailyLogs != null) {
            for (DailyLog dailyLog : patientDailyLogs) {
                allDates.add(dailyLog.getDate());
            }
        }

        return allDates;
    }
}
