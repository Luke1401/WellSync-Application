package com.well_sync.persistence.hsqldb;

import com.well_sync.logic.exceptions.InvalidDailyLogException;
import com.well_sync.objects.DailyLog;
import com.well_sync.objects.Medication;
import com.well_sync.objects.Patient;
import com.well_sync.objects.Substance;
import com.well_sync.objects.Symptom;
import com.well_sync.persistence.IDailyLogPersistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyLogPersistenceHSQLDB implements IDailyLogPersistence {

    private final String dbPath;

    public DailyLogPersistenceHSQLDB(String dbPath) {
        this.dbPath = dbPath;
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true;create=true", "MD", "");
    }

    @Override
    public void setDailyLog(Patient patient, DailyLog dailyLog) {
        try (Connection connection = connect()) {
            // Insert DailyLog into DAILY_LOG table
            PreparedStatement dailyLogStatement = connection.prepareStatement(
                    "INSERT INTO DAILY_LOG (patient_email, log_date, mood_score, sleep_hours, notes) VALUES (?, ?, ?, ?, ?)"
            );
            dailyLogStatement.setString(1, patient.getEmail());
            dailyLogStatement.setDate(2, new java.sql.Date(dailyLog.getDate().getTime()));
            dailyLogStatement.setInt(3, dailyLog.getMoodScore());
            dailyLogStatement.setInt(4, dailyLog.getSleepHours());
            dailyLogStatement.setString(5, dailyLog.getNotes());
            dailyLogStatement.executeUpdate();

            // Insert Symptoms into SYMPTOMS table
            for (Symptom symptom : dailyLog.getSymptoms()) {
                PreparedStatement symptomStatement = connection.prepareStatement(
                        "INSERT INTO SYMPTOMS (patient_email, log_date, symptom, intensity) VALUES (?, ?, ?, ?)"
                );
                symptomStatement.setString(1, patient.getEmail());
                symptomStatement.setDate(2, new java.sql.Date(dailyLog.getDate().getTime()));
                symptomStatement.setString(3, symptom.getName());
                symptomStatement.setInt(4, symptom.getIntensity());
                symptomStatement.executeUpdate();
            }

            // Insert Medications into MEDICATIONS table
            for (Medication medication : dailyLog.getMedications()) {
                PreparedStatement medicationStatement = connection.prepareStatement(
                        "INSERT INTO MEDICATIONS (patient_email, log_date, medication, quantity, dosage) VALUES (?, ?, ?, ?, ?)"
                );
                medicationStatement.setString(1, patient.getEmail());
                medicationStatement.setDate(2, new java.sql.Date(dailyLog.getDate().getTime()));
                medicationStatement.setString(3, medication.getName());
                medicationStatement.setInt(4, medication.getQuantity());
                medicationStatement.setInt(5, medication.getDosage());
                medicationStatement.executeUpdate();
            }

            // Insert Substances into SUBSTANCES table
            for (Substance substance : dailyLog.getSubstances()) {
                PreparedStatement substanceStatement = connection.prepareStatement(
                        "INSERT INTO SUBSTANCES (patient_email, log_date, substance, quantity) VALUES (?, ?, ?, ?)"
                );
                substanceStatement.setString(1, patient.getEmail());
                substanceStatement.setDate(2, new java.sql.Date(dailyLog.getDate().getTime()));
                substanceStatement.setString(3, substance.getName());
                substanceStatement.setInt(4, substance.getQuantity());
                substanceStatement.executeUpdate();
            }

        } catch (final SQLException e) {
            
            e.printStackTrace();
        }
    }

    public void setMedication(Patient patient, DailyLog dailyLog){
        try (Connection connection = connect()) {
        // Insert Medications into MEDICATIONS table
        for (Medication medication : dailyLog.getMedications()) {
            PreparedStatement medicationStatement = connection.prepareStatement(
                    "INSERT INTO MEDICATIONS (patient_email, log_date, medication, quantity, dosage) VALUES (?, ?, ?, ?, ?)"
            );
            medicationStatement.setString(1, patient.getEmail());
            medicationStatement.setDate(2, new java.sql.Date(dailyLog.getDate().getTime()));
            medicationStatement.setString(3, medication.getName());
            medicationStatement.setInt(4, medication.getQuantity());
            medicationStatement.setInt(5, medication.getDosage());
            medicationStatement.executeUpdate();
        }
        } catch (final SQLException e) {
            e.printStackTrace();
        }

    }
    public void setSymptoms(Patient patient, DailyLog dailyLog){
        try (Connection connection = connect()) {
            // Insert Symptoms into SYMPTOMS table
            for (Symptom symptom : dailyLog.getSymptoms()) {
                PreparedStatement symptomStatement = connection.prepareStatement(
                        "INSERT INTO SYMPTOMS (patient_email, log_date, symptom, intensity) VALUES (?, ?, ?, ?)"
                );
                symptomStatement.setString(1, patient.getEmail());
                symptomStatement.setDate(2, new java.sql.Date(dailyLog.getDate().getTime()));
                symptomStatement.setString(3, symptom.getName());
                symptomStatement.setInt(4, symptom.getIntensity());
                symptomStatement.executeUpdate();
            }
        } catch (final SQLException e) {

            e.printStackTrace();
        }

    }
    public void setSubstances(Patient patient, DailyLog dailyLog){
        try (Connection connection = connect()) {
            for (Substance substance : dailyLog.getSubstances()) {
                PreparedStatement substanceStatement = connection.prepareStatement(
                        "INSERT INTO SUBSTANCES (patient_email, log_date, substance, quantity) VALUES (?, ?, ?, ?)"
                );
                substanceStatement.setString(1, patient.getEmail());
                substanceStatement.setDate(2, new java.sql.Date(dailyLog.getDate().getTime()));
                substanceStatement.setString(3, substance.getName());
                substanceStatement.setInt(4, substance.getQuantity());
                substanceStatement.executeUpdate();
            }
        } catch (final SQLException e) {

            e.printStackTrace();
        }

    }

    @Override
    public DailyLog getDailyLog(Patient patient, Date date) {
        DailyLog dailyLog = null;
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM DAILY_LOG WHERE patient_email = ? AND log_date = ?"
            );
            statement.setString(1, patient.getEmail());
            statement.setDate(2, new java.sql.Date(date.getTime()));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                dailyLog = getDailyLogFromResultSet(connection, resultSet, patient.getEmail());
            }
        } catch (final SQLException e) {
            
            e.printStackTrace();
        }
        return dailyLog;
    }

    @Override
    public List<DailyLog> getAllDailyLogs(Patient patient) {
        List<DailyLog> dailyLogs = new ArrayList<>();
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT DISTINCT log_date FROM DAILY_LOG WHERE patient_email = ?"
            );
            statement.setString(1, patient.getEmail());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Date date = resultSet.getDate("log_date");
                DailyLog dailyLog = getDailyLog(patient, date);
                if (dailyLog != null) {
                    dailyLogs.add(dailyLog);
                }
            }
        } catch (final SQLException e) {
            
            e.printStackTrace();
        }
        return dailyLogs;
    }

    @Override
    public List<Date> getAllDates(Patient patient) {
        List<Date> allDates = new ArrayList<>();
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT DISTINCT log_date FROM DAILY_LOG WHERE patient_email = ?"
            );
            statement.setString(1, patient.getEmail());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Date date = resultSet.getDate("log_date");
                if (date != null) {
                    allDates.add(date);
                }
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return allDates;
    }

    private DailyLog getDailyLogFromResultSet(Connection connection, ResultSet resultSet, String email) throws SQLException {
        int moodScore = resultSet.getInt("mood_score");
        int sleepHours = resultSet.getInt("sleep_hours");
        String notes = resultSet.getString("notes");
        Date date = resultSet.getDate("log_date");
        DailyLog dailyLog = new DailyLog(date, moodScore, sleepHours, notes);

        // Fetch Symptoms
        PreparedStatement symptomStatement = connection.prepareStatement(
                "SELECT * FROM SYMPTOMS WHERE patient_email = ? AND log_date = ?"
        );
        symptomStatement.setString(1, email);
        symptomStatement.setDate(2, new java.sql.Date(date.getTime()));
        ResultSet symptomResultSet = symptomStatement.executeQuery();
        while (symptomResultSet.next()) {
            String symptomName = symptomResultSet.getString("symptom");
            int symptomIntensity = symptomResultSet.getInt("intensity");
            try {
                dailyLog.addSymptom(symptomName, symptomIntensity);
            } catch (InvalidDailyLogException e) {
                continue;
            }
        }

        // Fetch Medications
        PreparedStatement medicationStatement = connection.prepareStatement(
                "SELECT * FROM MEDICATIONS WHERE patient_email = ? AND log_date = ?"
        );
        medicationStatement.setString(1, email);
        medicationStatement.setDate(2, new java.sql.Date(date.getTime()));
        ResultSet medicationResultSet = medicationStatement.executeQuery();
        while (medicationResultSet.next()) {
            String medicationName = medicationResultSet.getString("medication");
            int medicationQuantity = medicationResultSet.getInt("quantity");
            int medicationDosage =medicationResultSet.getInt("dosage");
            try {
                dailyLog.addMedication(medicationName, medicationQuantity,medicationDosage);
            } catch (InvalidDailyLogException e) {
                continue;
            }
        }

        // Fetch Substances
        PreparedStatement substanceStatement = connection.prepareStatement(
                "SELECT * FROM SUBSTANCES WHERE patient_email = ? AND log_date = ?"
        );
        substanceStatement.setString(1, email);
        substanceStatement.setDate(2, new java.sql.Date(date.getTime()));
        ResultSet substanceResultSet = substanceStatement.executeQuery();
        while (substanceResultSet.next()) {
            String substanceName = substanceResultSet.getString("substance");
            int substanceQuantity = substanceResultSet.getInt("quantity");
            try {
                dailyLog.addSubstance(substanceName, substanceQuantity);
            } catch (InvalidDailyLogException e) {
                continue;
            }
        }

        return dailyLog;
    }
}
