package com.well_sync.objects;

import com.well_sync.logic.DailyLogValidator;
import com.well_sync.logic.exceptions.InvalidDailyLogException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Represents observations of patient health for one specific date.
 */
public class DailyLog {
    private Date date;
    private int moodScore;
    private int sleepHours;
    private String notes;
    private final List<Symptom> symptomList;
    private final List<Medication> medicationList;
    private final List<Substance> substanceList;

    public DailyLog() {
        this.date = new Date(); // initialized to present

        this.symptomList = new ArrayList<>();
        this.medicationList = new ArrayList<>();
        this.substanceList = new ArrayList<>();
    }

    public DailyLog(String date, int moodScore, int sleepHours, String notes) {
        this((Date) null, moodScore, sleepHours, notes);
        try {
            this.date = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).parse(date);
        } catch (ParseException e) {
            this.date = null;
        }
    }

    public DailyLog(Date date, int moodScore, int sleepHours, String notes) {
        this.date = date;
        this.setMoodScore(moodScore); // bounds checking
        this.setSleepHours(sleepHours); // " "
        this.notes = notes;

        this.symptomList = new ArrayList<>();
        this.medicationList = new ArrayList<>();
        this.substanceList = new ArrayList<>();
    }


    public Date getDate() {
        return date;
    }

    public int getMoodScore() {
        return moodScore;
    }

    public int getSleepHours() {
        return sleepHours;
    }

    public String getNotes() {
        return notes;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setMoodScore(int moodScore) {
        this.moodScore = moodScore;
    }

    public void setSleepHours(int sleepHours) {
        this.sleepHours = Math.max(sleepHours, 0);
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void addSymptom(String name, int intensity) throws InvalidDailyLogException {
        Symptom symptom = new Symptom(name, intensity);
        DailyLogValidator.validateSymptom(symptom);
        this.symptomList.add(symptom);
    }

    public void removeSymptom(Symptom symptom) {
        this.symptomList.remove(symptom);
    }

    public void removeSymptom(String name) {
        this.symptomList.forEach(symptom -> {
            if (symptom.getName().equals(name)) this.symptomList.remove(symptom);
        });
    }

    public List<Symptom> getSymptoms() {
        return this.symptomList;
    }

    public void addMedication(String name, int quantity,int dosage) throws InvalidDailyLogException {
        Medication med = new Medication(name, quantity, dosage);
        DailyLogValidator.validateMedication(med);
        this.medicationList.add(med);
    }

    public void removeMedication(Medication medication) {
        this.medicationList.remove(medication);
    }

    public void removeMedication(String name) {
        this.medicationList.forEach(med -> {
            if (med.getName().equals(name)) this.medicationList.remove(med);
        });
    }

    public List<Medication> getMedications() {
        return this.medicationList;
    }

    public void addSubstance(String name, int quantity) throws InvalidDailyLogException {
        Substance sub = new Substance(name, quantity);
        DailyLogValidator.validateSubstance(sub);
        this.substanceList.add(sub);
    }

    public void removeSubstance(Substance sub) {
        this.substanceList.remove(sub);
    }

    public void removeSubstance(String name) {
        this.substanceList.forEach(sub -> {
            if (sub.getName().equals(name)) this.substanceList.remove(sub);
        });
    }

    public List<Substance> getSubstances() {
        return this.substanceList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyLog dailyLog = (DailyLog) o;

        return moodScore == dailyLog.moodScore
                && sleepHours == dailyLog.sleepHours
                && date.getTime() == dailyLog.date.getTime()
                && Objects.equals(notes, dailyLog.notes)
                && Objects.equals(symptomList, dailyLog.symptomList)
                && Objects.equals(medicationList, dailyLog.medicationList)
                && Objects.equals(substanceList, dailyLog.substanceList);
    }

}
