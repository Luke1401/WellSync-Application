package com.well_sync.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.well_sync.R;
import com.well_sync.logic.DailyLogHandler;
import com.well_sync.logic.DoctorHandler;
import com.well_sync.logic.IDailyLogHandler;
import com.well_sync.logic.IDoctorHandler;
import com.well_sync.logic.IPatientHandler;
import com.well_sync.logic.PatientHandler;
import com.well_sync.logic.exceptions.InvalidDoctorException;
import com.well_sync.logic.exceptions.InvalidPatientException;
import com.well_sync.objects.Doctor;
import com.well_sync.objects.Patient;

import java.util.Date;
import java.util.List;

public class PatientInfoActivity extends AppCompatActivity {
    private Patient patient;
    private Doctor doctor;
    private EditText adviseEditText;
    private String doctorEmail, patientEmail;
    private String date;
    private List<Date> listDates;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);

        // Initialize views
        adviseEditText = findViewById(R.id.advise_from_doctor);
        Button logsButton = findViewById(R.id.daily_logs);
        Button sendButton = findViewById(R.id.send_rec);
        Button progressButton = findViewById(R.id.see_progress);
        Button deleteButton = findViewById(R.id.delete_patient);
        ImageView closeIcon = findViewById(R.id.close);


        // Retrieve the selected patient's information from the intent
        Intent intent = getIntent();
        doctorEmail = intent.getStringExtra("doctorEmail");
        date = intent.getStringExtra("date");
        patientEmail = intent.getStringExtra("patientEmail");
        IPatientHandler patientHandler = new PatientHandler();
        patient = patientHandler.getDetails(patientEmail);
        IDoctorHandler doctorHandler = new DoctorHandler();
        doctor = doctorHandler.getDetails(doctorEmail);
        // get all the date from the patient
        IDailyLogHandler logHandler = new DailyLogHandler();
        listDates= logHandler.getAllDates(patient);

        setData(R.id.name,patient.getFirstName()+" "+patient.getLastName());
        setData(R.id.birthday, String.valueOf(patient.getAge()));
        setData(R.id.gender,patient.getSex().name());
        setData(R.id.bloodtype,patient.getBloodType().name());

        closeIcon.setOnClickListener(view -> {
            // Handle close button click
            Intent closeIntent = new Intent(PatientInfoActivity.this, DoctorPageActivity.class);
            closeIntent.putExtra("email", doctorEmail);
            PatientInfoActivity.this.startActivity(closeIntent);
        });

        // handle the Button click listener as needed
        //UPDATE THIS METHOD
        logsButton.setOnClickListener(view -> {
            if(!listDates.isEmpty()){
                Intent currentIntent = new Intent(PatientInfoActivity.this, DateActivity.class);
                currentIntent.putExtra("date", date);
                currentIntent.putExtra("patientEmail",patientEmail);
                currentIntent.putExtra("doctorEmail", doctorEmail);
                PatientInfoActivity.this.startActivity(currentIntent);
            }else{
                Toast.makeText(PatientInfoActivity.this, "No logs to show.", Toast.LENGTH_SHORT).show();
            }
        });
        sendButton.setOnClickListener(v -> {
            // Implement save functionality here
            String advise = adviseEditText.getText().toString();
            if(advise.isEmpty()) {
                // Show a Toast or handle the validation error as needed
                Toast.makeText(PatientInfoActivity.this, "Advise can not be empty.", Toast.LENGTH_SHORT).show();
                return; // Exit the onClick method to prevent further execution
            }
            patient.setDoctorNotes(advise);
            try {
                patientHandler.editPatientDetails(patient);
            } catch (InvalidPatientException e) {
                Toast.makeText(getApplicationContext(), "Changes weren't saved, try again later.", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(getApplicationContext(), "Data saved successfully!", Toast.LENGTH_SHORT).show();
            Intent saveIntent = new Intent(PatientInfoActivity.this, DoctorPageActivity.class);
            saveIntent.putExtra("email", doctorEmail);
            PatientInfoActivity.this.startActivity(saveIntent);
        });
        progressButton.setOnClickListener(view -> {
            if(listDates.size()>2){
                Intent currentIntent = new Intent(PatientInfoActivity.this, UserProgressActivity.class);
                currentIntent.putExtra("date", date);
                currentIntent.putExtra("patientEmail",patientEmail);
                currentIntent.putExtra("doctorEmail", doctorEmail);
                PatientInfoActivity.this.startActivity(currentIntent);
            }else{
                Toast.makeText(PatientInfoActivity.this, "Not enough daily logs to display.", Toast.LENGTH_SHORT).show();
            }

        });

        deleteButton.setOnClickListener(v -> {
            try {
                doctorHandler.removePatient(doctor, patient);
                Toast.makeText(getApplicationContext(), "Patient removed successfully!", Toast.LENGTH_SHORT).show();
            } catch (InvalidDoctorException | InvalidPatientException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            Intent saveIntent = new Intent(PatientInfoActivity.this, DoctorPageActivity.class);
            saveIntent.putExtra("email", doctorEmail);
            PatientInfoActivity.this.startActivity(saveIntent);
        });
    }
    public void setData(int id, String data){
        TextView field = findViewById(id);
        field.setText(data);
    }
}




