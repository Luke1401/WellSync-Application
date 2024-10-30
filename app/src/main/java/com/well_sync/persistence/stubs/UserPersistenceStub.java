package com.well_sync.persistence.stubs;

import com.well_sync.objects.Doctor;
import com.well_sync.objects.Patient;
import com.well_sync.objects.UserCredentials;
import com.well_sync.persistence.IUserPersistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserPersistenceStub implements IUserPersistence {
    private final List<Patient> patientList;
    private final List<UserCredentials> userCredentialsList;
    private final List<Doctor> doctorList;
    private final Map<String, List<Patient>> doctorsToPatients;

    public UserPersistenceStub() {
        this.patientList = new ArrayList<>();
        this.userCredentialsList = new ArrayList<>();
        this.doctorList = new ArrayList<>();
        this.doctorsToPatients = new HashMap<>();

        // Sample data for USER_CREDENTIALS table
        UserCredentials userCredentials1 = new UserCredentials("patient1@example.com", "password1", "PATIENT");
        UserCredentials userCredentials2 = new UserCredentials("patient2@example.com", "password1", "PATIENT");
        UserCredentials userCredentials3 = new UserCredentials("doctor1@example.com", "password2", "DOCTOR");
        UserCredentials userCredentials4 = new UserCredentials("doctor2@example.com", "password2", "DOCTOR");
        userCredentialsList.add(userCredentials1);
        userCredentialsList.add(userCredentials2);
        userCredentialsList.add(userCredentials3);
        userCredentialsList.add(userCredentials4);

        // Sample data for PATIENTS table
        Patient patient1 = new Patient("patient1@example.com", "John", "Doe", "TYPE_A", "MALE", 30, "Sleep more");
        Patient patient2 = new Patient("patient2@example.com", "Jane", "Smith", "TYPE_O", "FEMALE", 25, "Stop Studying");
        patientList.add(patient1);
        patientList.add(patient2);

        // Sample data for DOCTORS table
        Doctor doctor1 = new Doctor("doctor1@example.com", "Dr. Gabriel", "Young");
        Doctor doctor2 = new Doctor("doctor2@example.com", "Dr. Alvee", "Jawad");
        doctorList.add(doctor1);
        doctorList.add(doctor2);

        ArrayList<Patient> doctor1List = new ArrayList<>();
        doctor1List.add(patient1);
        ArrayList<Patient> doctor2List = new ArrayList<>();
        doctor2List.add(patient2);

        doctorsToPatients.put(doctor1.getEmail(), doctor1List);
        doctorsToPatients.put(doctor2.getEmail(), doctor2List);
    }

    @Override
    public UserCredentials getUserCredentials(UserCredentials userCredentials) {
        for(int i = 0; i < userCredentialsList.size(); i ++) {
            if(userCredentialsList.get(i).getEmail().equals(userCredentials.getEmail())) {
                return userCredentialsList.get(i);
            }
        }
        return null;
    }
    public Patient getPatient(String email){
        for (int  i = 0 ;i < patientList.size(); i++){
            if(patientList.get(i).getEmail().equals(email)){
                 return patientList.get(i);
            }
        }
        return null;
    }

    @Override
    public void createDoctor(Doctor doctor) {
        boolean doctorExists = false;
        for (int i = 0; i < doctorList.size(); i++) {
            if (doctorList.get(i).getEmail().equals(doctor.getEmail())) {
                doctorList.set(i, doctor);
                doctorExists = true;
                break;
            }
        }
        if (!doctorExists) {
            doctorList.add(doctor);
        }
    }

    @Override
    public Doctor getDoctor(String email) {
        for (Doctor doctor : doctorList) {
            if (doctor.getEmail().equals(email)) {
                return doctor;
            }
        }
        return null;
    }
    public void assignPatientToDoctor(Patient patient, Doctor doctor){}

    @Override
    public void setUserCredentials(UserCredentials user) {
        userCredentialsList.add(user);
    }

    @Override
    public void createPatient(Patient patient) {
        boolean patientExists = false;

        // Check if a patient with the same email already exists
        for (int i = 0; i < patientList.size(); i++) {
            if (patientList.get(i).getEmail().equals(patient.getEmail())) {
                patientList.set(i, patient);
                patientExists = true;
                break;
            }
        }

        // If no patient with the same email is found, add the new patient
        if (!patientExists) {
            patientList.add(patient);
        }
    }

    @Override
    public void editPatientDetails(Patient patient) {
        for (int i = 0; i < patientList.size(); i++) {
            if (patientList.get(i).getEmail().equals(patient.getEmail())) {
                Patient updatedPatient = patientList.get(i);
                updatedPatient.setFirstName(patient.getFirstName());
                updatedPatient.setLastName(patient.getLastName());
                updatedPatient.setBloodType(patient.getBloodType());
                updatedPatient.setSex(patient.getSex());
                updatedPatient.setAge(patient.getAge());
                updatedPatient.setDoctorNotes(patient.getDoctorNotes());

                System.out.println("Patient details updated successfully.");
                return;
            }
        }
        System.out.println("Patient not found, details not updated.");
    }

    @Override
    public void removePatientFromDoctor(Doctor doctor, Patient patient) {
        if (doctor != null && patient != null) {
            List<Patient> patientList = doctorsToPatients.get(doctor.getEmail());
            if (patientList != null)
                patientList.remove(patient);
        } else {
            System.out.println("Doctor or patient not specified.");
        }
    }

    @Override
    public List<Patient> getAllPatientsForDoctor(Doctor doctor) {
        return doctorsToPatients.get(doctor.getEmail());
    }


}
