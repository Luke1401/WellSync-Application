package com.well_sync.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.well_sync.R;
import com.well_sync.logic.DailyLogHandler;
import com.well_sync.logic.IDailyLogHandler;
import com.well_sync.logic.IPatientHandler;
import com.well_sync.logic.PatientHandler;
import com.well_sync.logic.exceptions.InvalidDailyLogException;
import com.well_sync.objects.DailyLog;
import com.well_sync.objects.Patient;


public class SubstanceUseTrackerActivity extends AppCompatActivity {

    private EditText nameSub, amountSub;
    protected String name, amount;

    private IDailyLogHandler dailyLogHandler;
    private String email;
    private DailyLog dailyLog;
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_substance_use);

        ImageView closeIcon = findViewById(R.id.exit_1);
        nameSub = findViewById(R.id.name_of_subs);
        amountSub = findViewById(R.id.num_of_subs);
        Button saveButton = findViewById(R.id.save_1);

        //get email and date from HomePage Activity
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        String date = intent.getStringExtra("date");
        dailyLogHandler = new DailyLogHandler();

        // Get the data from patient
        IPatientHandler patientHandler = new PatientHandler();
        Patient newPatient = patientHandler.getDetails(email);
        dailyLog = dailyLogHandler.getDailyLog(newPatient, date);


        // Set click listeners or any other event listeners as needed
        closeIcon.setOnClickListener(view -> {
            // Handle close button click
            Intent closeIntent = new Intent(SubstanceUseTrackerActivity.this, HomePageActivity.class);
            closeIntent.putExtra("email",email);
            SubstanceUseTrackerActivity.this.startActivity(closeIntent);
        });

        saveButton.setOnClickListener(view -> {
            // Handle save button click

            // Initialize the data from user
            name = nameSub.getText().toString();
            if (name.isEmpty()) {
                // Show a Toast or handle the validation error as needed
                Toast.makeText(SubstanceUseTrackerActivity.this, "Substance name cannot be empty.", Toast.LENGTH_SHORT).show();
                return; // Exit the onClick method to prevent further execution
            }
            amount = amountSub.getText().toString();
            if (amount.isEmpty()) {
                // Show a Toast or handle the validation error as needed
                Toast.makeText(SubstanceUseTrackerActivity.this, "Substance amount cannot be empty.", Toast.LENGTH_SHORT).show();
                return; // Exit the onClick method to prevent further execution
            }

            int amountInt = Integer.parseInt(amount);

            try {
                dailyLog.addSubstance(name, amountInt);
            } catch (InvalidDailyLogException e) {
                Toast.makeText(SubstanceUseTrackerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            try {
                dailyLogHandler.setSubstances(newPatient,dailyLog);
            } catch (InvalidDailyLogException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            Intent saveIntent = new Intent(SubstanceUseTrackerActivity.this, DisplaySubstanceUseActivity.class);
            saveIntent.putExtra("email",email);
            saveIntent.putExtra("name", name);
            saveIntent.putExtra("amount", amount);
            SubstanceUseTrackerActivity.this.startActivity(saveIntent);

        });
    }

}
