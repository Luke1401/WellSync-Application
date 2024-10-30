package com.well_sync.persistence.hsqldb;

import com.well_sync.objects.Doctor;
import com.well_sync.objects.Patient;
import com.well_sync.objects.UserCredentials;
import com.well_sync.persistence.IUserPersistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserPersistenceHSQLDB implements IUserPersistence {

    private final String dbPath;

    public UserPersistenceHSQLDB(String dbPath) {
        this.dbPath = dbPath;
        loadPatients();
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true;create=true", "MD", "");
    }


    private void loadPatients() {
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM PATIENTS");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                createPatientFromResultSet(resultSet);
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public UserCredentials getUserCredentials(UserCredentials userCredentials) {
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM USER_CREDENTIALS WHERE email = ?");
            statement.setString(1, userCredentials.getEmail());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new UserCredentials(resultSet.getString("email"), resultSet.getString("password"),resultSet.getString("role"));
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setUserCredentials(UserCredentials user) {
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO USER_CREDENTIALS VALUES (?, ?, ?)");
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().toString());
            statement.executeUpdate();
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createPatient(Patient patient) {
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO PATIENTS VALUES (?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, patient.getEmail());
            statement.setString(2, patient.getFirstName());
            statement.setString(3, patient.getLastName());
            statement.setString(4, patient.getBloodType().toString());
            statement.setString(5, patient.getSex().toString());
            statement.setInt(6, patient.getAge());
            statement.setString(7, patient.getDoctorNotes());
            statement.executeUpdate();
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void editPatientDetails(Patient patient) {
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE PATIENTS " +
                    "SET firstName = ?, lastName = ?, bloodType = ?, sex = ?, age = ?, doctorNotes = ? " +
                    "WHERE email = ?");
            statement.setString(1, patient.getFirstName());
            statement.setString(2, patient.getLastName());
            statement.setString(3, patient.getBloodType().toString());
            statement.setString(4, patient.getSex().toString());
            statement.setInt(5, patient.getAge());
            statement.setString(6, patient.getDoctorNotes());
            statement.setString(7, patient.getEmail());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Patient details updated successfully.");
            } else {
                System.out.println("Patient not found, details not updated.");
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Patient getPatient(String email) {
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM PATIENTS WHERE email = ?");
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return createPatientFromResultSet(resultSet);
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void createDoctor(Doctor doctor) {
        try (Connection connection = connect()) {
            PreparedStatement doctorStatement = connection.prepareStatement("INSERT INTO DOCTORS VALUES (?, ?, ?)");
            doctorStatement.setString(1, doctor.getEmail());
            doctorStatement.setString(2, doctor.getFirstName());
            doctorStatement.setString(3, doctor.getLastName());
            doctorStatement.executeUpdate();

        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removePatientFromDoctor(Doctor doctor, Patient patient) {
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM ASSIGNED_PATIENTS WHERE doctor_email = ? AND patient_email = ?");
            statement.setString(1, doctor.getEmail());
            statement.setString(2, patient.getEmail());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Patient not found in the assigned patients list for the doctor.");
            } else {
                System.out.println("Patient removed successfully from the assigned patients list for the doctor.");
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Doctor getDoctor(String email) {
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM DOCTORS WHERE email = ?");
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return createDoctorFromResultSet(resultSet);
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void assignPatientToDoctor(Patient patient, Doctor doctor) {
        try (Connection connection = connect()) {
            PreparedStatement assignStatement = connection.prepareStatement("INSERT INTO ASSIGNED_PATIENTS VALUES (?, ?)");
            assignStatement.setString(1, doctor.getEmail());
            assignStatement.setString(2, patient.getEmail());
            assignStatement.executeUpdate();
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    private Patient createPatientFromResultSet(ResultSet resultSet) throws SQLException {
        String email = resultSet.getString("email");
        String firstName = resultSet.getString("firstName");
        String lastName = resultSet.getString("lastName");
        String bloodType = resultSet.getString("bloodType");
        String sex = resultSet.getString("sex");
        int age = resultSet.getInt("age");
        String doctorNotes = resultSet.getString("doctorNotes");

        return new Patient(email, firstName, lastName, bloodType, sex, age, doctorNotes);
    }

    private Doctor createDoctorFromResultSet(ResultSet resultSet) throws SQLException {
        String email = resultSet.getString("email");
        String firstName = resultSet.getString("firstName");
        String lastName = resultSet.getString("lastName");

        return new Doctor(email, firstName, lastName);
    }

    @Override
    public List<Patient> getAllPatientsForDoctor(Doctor doctor) {
        List<Patient> patientsForDoctor = new ArrayList<>();
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ASSIGNED_PATIENTS WHERE doctor_email = ?");
            statement.setString(1, doctor.getEmail());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String patientEmail = resultSet.getString("patient_email");
                Patient patient = getPatient(patientEmail);
                if (patient != null) {
                    patientsForDoctor.add(patient);
                }
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return patientsForDoctor;
    }
}
