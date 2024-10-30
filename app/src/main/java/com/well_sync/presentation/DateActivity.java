package com.well_sync.presentation;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.well_sync.R;
import com.well_sync.logic.DailyLogHandler;
import com.well_sync.logic.IDailyLogHandler;
import com.well_sync.logic.IPatientHandler;
import com.well_sync.logic.PatientHandler;
import com.well_sync.objects.Patient;

import java.util.Date;
import java.util.List;

public class DateActivity extends AppCompatActivity {

    private String patientEmail;
    private String doctorEmail;
    private List<Date> listOfDate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_date_list);

        RecyclerView dateList = findViewById(R.id.date_list);

        // retrieve the patient email and date from the intent
        Intent intent = getIntent();
        patientEmail = intent.getStringExtra("patientEmail");
        doctorEmail = intent.getStringExtra("doctorEmail");
        String date = intent.getStringExtra("date");

        // Initialize handler
        IPatientHandler patientHandler = new PatientHandler();
        Patient patient = patientHandler.getDetails(patientEmail);

        // get all the date from the patient
        IDailyLogHandler logHandler = new DailyLogHandler();
        listOfDate = logHandler.getAllDates(patient);

        //show list of dates
        RecyclerView recyclerView = findViewById(R.id.date_list);
        DateAdapter dateAdapter = new DateAdapter(this, listOfDate);
        dateList.setAdapter(dateAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // set a click listener for RecycleView items
        dateAdapter.setOnItemClickListener(position -> {
            Date selectedDate = listOfDate.get(position);
            // Start the DailyLogPatientActivity and pass the selected patient's information
            Intent dateIntent = new Intent(DateActivity.this, DailyLogPatientActivity.class);
            //patient's information
            dateIntent.putExtra("patientEmail",patientEmail);
            //doctor's information
            dateIntent.putExtra("doctorEmail", doctorEmail);
            dateIntent.putExtra("date", selectedDate.toString());
            DateActivity.this.startActivity(dateIntent);
        });
    }
}
