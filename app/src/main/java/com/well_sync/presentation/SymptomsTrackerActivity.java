package com.well_sync.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.well_sync.R;
import com.well_sync.logic.DailyLogHandler;
import com.well_sync.logic.IDailyLogHandler;
import com.well_sync.logic.IPatientHandler;
import com.well_sync.logic.PatientHandler;
import com.well_sync.logic.exceptions.InvalidDailyLogException;
import com.well_sync.objects.DailyLog;
import com.well_sync.objects.Patient;

public class SymptomsTrackerActivity extends AppCompatActivity {
    private IDailyLogHandler dailyLogHandler;
    private String email;
    private DailyLog dailyLog;
    private String[] symptomNames;
    private RecyclerView recyclerView;

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_symptomstracker);
        //initialize handler
        dailyLogHandler = new DailyLogHandler();
        //initialize buttons
        ImageView closeIcon = findViewById(R.id.close);
        Button saveButton = findViewById(R.id.savebutton);

        //get email and date from HomePage Activity
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        String date = intent.getStringExtra("date");

        // Get the data from patient
        IPatientHandler patientHandler = new PatientHandler();
        Patient newPatient = patientHandler.getDetails(email);
        dailyLog = dailyLogHandler.getDailyLog(newPatient, date);

        //get symptoms list
        symptomNames = getResources().getStringArray(R.array.symptoms);
        recyclerView = findViewById(R.id.symptoms_list);
        SymptomAdapter symptomAdapter = new SymptomAdapter(symptomNames);
        recyclerView.setAdapter(symptomAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set click listeners or any other event listeners as needed
        closeIcon.setOnClickListener(view -> {
            // Handle close button click
            Intent closeIntent = new Intent(SymptomsTrackerActivity.this, HomePageActivity.class);
            closeIntent.putExtra("email", email);
            SymptomsTrackerActivity.this.startActivity(closeIntent);
        });

        saveButton.setOnClickListener(view -> {
            saveRatings();
            try {
                dailyLogHandler.setSymptoms(newPatient, dailyLog);
            } catch (InvalidDailyLogException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            // Handle close button click
            Intent continueIntent = new Intent(SymptomsTrackerActivity.this, HomePageActivity.class);
            continueIntent.putExtra("email", email);
            SymptomsTrackerActivity.this.startActivity(continueIntent);
        });

    }

    private void saveRatings() {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View childView = recyclerView.getChildAt(i);
            SymptomAdapter.SymptomViewHolder viewHolder = (SymptomAdapter.SymptomViewHolder) recyclerView.getChildViewHolder(childView);
            int rating = viewHolder.getCurrentRating();
            try {
                dailyLog.addSymptom(symptomNames[i],rating);
            } catch (InvalidDailyLogException e) {
                e.printStackTrace();
            }
        }
    }
}

