package com.well_sync.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.well_sync.R;
import com.well_sync.logic.DoctorHandler;
import com.well_sync.logic.IDoctorHandler;
import com.well_sync.logic.exceptions.InvalidDoctorException;
import com.well_sync.objects.Doctor;

public class DoctorSignUpActivity extends AppCompatActivity {
    private EditText userFirstName,userLastName;
    private IDoctorHandler doctorHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_sign_up);

        userFirstName = findViewById(R.id.editFirstName);
        userLastName = findViewById(R.id.editLastName);
        Button saveButton = findViewById(R.id.savebutton);
        doctorHandler = new DoctorHandler();

        //Get doctor's email
        Intent intent = getIntent();
        String doctorEmail = intent.getStringExtra("email");

        saveButton.setOnClickListener(v -> {

            //get input from user
            String firstName = userFirstName.getText().toString();
            String lastName = userLastName.getText().toString();

            Doctor doctor = new Doctor(doctorEmail ,firstName, lastName);
            try {
                doctorHandler.editDetails(doctor);
                Intent homeIntent =new Intent(DoctorSignUpActivity.this, DoctorPageActivity.class);
                homeIntent.putExtra("email",doctorEmail);
                startActivity(homeIntent);
            } catch (InvalidDoctorException e) {
                Toast.makeText(getApplicationContext(),"Invalid doctor details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}