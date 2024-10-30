package com.well_sync.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.well_sync.R;
import com.well_sync.logic.DoctorHandler;
import com.well_sync.logic.IDoctorHandler;
import com.well_sync.logic.IPatientHandler;
import com.well_sync.logic.PatientHandler;
import com.well_sync.logic.exceptions.InvalidDoctorException;
import com.well_sync.logic.exceptions.InvalidPatientException;
import com.well_sync.objects.Doctor;
import com.well_sync.objects.Patient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DoctorPageActivity extends AppCompatActivity{

    private Doctor doctor;
    private String doctorEmail;
    private List<Patient> patientList;
    private IPatientHandler patientHandler;
    private PatientAdapter patientAdapter;

    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        //Get doctor's email
        Intent intent = getIntent();
        doctorEmail = intent.getStringExtra("email");
        String date = getCurrentDate();

        //Initialize handler
        patientHandler = new PatientHandler();
        IDoctorHandler doctorHandler = new DoctorHandler();

        doctor = doctorHandler.getDetails(doctorEmail);
        try {
            patientList = doctorHandler.getAllPatientsForDoctor(doctor);
        } catch (InvalidDoctorException e) {
            patientList = null;
        }

        //show list of patients
        RecyclerView recyclerView = findViewById(R.id.patientList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        patientAdapter = new PatientAdapter(patientList);
        recyclerView.setAdapter(patientAdapter);

        // Set a click listener for RecyclerView items
        patientAdapter.setOnItemClickListener(position -> {
            Patient selectedPatient = patientList.get(position);
            // Start the PatientInfoActivity and pass the selected patient's information
            Intent patientIntent = new Intent(DoctorPageActivity.this, PatientInfoActivity.class);
            patientIntent.putExtra("patientEmail",selectedPatient.getEmail());
            //doctor's information
            patientIntent.putExtra("doctorEmail", doctorEmail);
            patientIntent.putExtra("date", date);
            DoctorPageActivity.this.startActivity(patientIntent);
        });

        emailEditText = findViewById(R.id.patient_email_input);
        Button addPatientButton = findViewById(R.id.add_patient);
        Button logoutButton = findViewById(R.id.logout);

        addPatientButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            // check if the input for email is empty
            if(email.isEmpty()) {
                // Show a Toast or handle the validation error as needed
                Toast.makeText(DoctorPageActivity.this, "Email cannot be empty.", Toast.LENGTH_SHORT).show();
                return; // Exit the onClick method to prevent further execution
            }
            Patient existedPatient = patientHandler.getDetails(email);
            if(existedPatient == null) {
                // Show a Toast or handle the validation error as needed
                Toast.makeText(DoctorPageActivity.this, "Sorry, the patient does not exist.", Toast.LENGTH_SHORT).show();
                return; // Exit the onClick method to prevent further execution
            }
            for(int i = 0; i < patientList.size(); i++) { // go over the list of patients
                if(existedPatient.equals(patientList.get(i))) { // check if we add a duplicate patient
                    // Show a Toast or handle the validation error as needed
                    Toast.makeText(DoctorPageActivity.this, "Duplicated patient, please try again", Toast.LENGTH_SHORT).show();
                    return; // Exit the onClick method to prevent further execution
                }
            }
            try {
              doctorHandler.addPatient(existedPatient, doctor);
              patientList.add(existedPatient);
            } catch (InvalidDoctorException | InvalidPatientException e) {
                Toast.makeText(DoctorPageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            //Notify the adapter of the data change
            patientAdapter.notifyDataSetChanged();
        });

        logoutButton.setOnClickListener(v ->
                startActivity(new Intent(DoctorPageActivity.this,LoginActivity.class)));
    }
    private String getCurrentDate(){
        //get current date
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        return dateFormat.format(date);
    }
}
