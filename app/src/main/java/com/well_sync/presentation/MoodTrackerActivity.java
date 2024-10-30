package com.well_sync.presentation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class MoodTrackerActivity extends AppCompatActivity {

    private ImageView smileImageView;
    private ImageView neutralImageView;
    private ImageView angryImageView;
    private ImageView sickImageView;
    private ImageView selectedImageView;
    private EditText sleepHoursEditText, userNotesEditText;
    private TextView emotionText;

    private String email;
    private String date;
    private IDailyLogHandler dailyLogHandler;
    protected String emotion, sleepHoursText, userNotes;
    private int moodScores;
    private int sleepHours;
    private String[] moodList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood); // Replace with your actual layout file name
        dailyLogHandler = new DailyLogHandler();
        moodScores = 0;
        moodList = getResources().getStringArray(R.array.moods);

        //get email and date from HomePage Activity
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        date = intent.getStringExtra("date");

        // Initialize views
        ImageView closeImageView = findViewById(R.id.close);
        smileImageView = findViewById(R.id.smile);
        neutralImageView = findViewById(R.id.neutral);
        angryImageView = findViewById(R.id.angry);
        sickImageView = findViewById(R.id.sick);
        emotionText = findViewById(R.id.emotions);
        sleepHoursEditText = findViewById(R.id.sleep_hours);
        userNotesEditText = findViewById(R.id.user_notes);
        Button saveButton = findViewById(R.id.button);

        // Set click listeners for icon selection
        smileImageView.setOnClickListener(view -> {
            // Reset color filter for all image views
            resetColorFilter();
            // Set color filter for the selected image view
            smileImageView.setColorFilter(Color.BLUE);
            selectedImageView = smileImageView;

            // Set the emotion text below the selected image view
            emotionText.setText(moodList[0]);
            // Set the mood score
            moodScores = 0;
        });

        neutralImageView.setOnClickListener(view -> {
            resetColorFilter();
            neutralImageView.setColorFilter(Color.BLUE);
            selectedImageView = neutralImageView;
            // Set the emotion text below the selected image view
            emotionText.setText(moodList[1]);

            // Set the mood score
            moodScores = 1;
        });

        angryImageView.setOnClickListener(view -> {
            resetColorFilter();
            angryImageView.setColorFilter(Color.BLUE);
            selectedImageView = angryImageView;
            // Set the emotion text below the selected image view
            emotionText.setText(moodList[2]);

            // Set the mood score
            moodScores = 2;
        });

        sickImageView.setOnClickListener(view -> {
            resetColorFilter();
            sickImageView.setColorFilter(Color.BLUE);
            selectedImageView = sickImageView;
            // Set the emotion text below the selected image view
            emotionText.setText(moodList[3]);

            // Set the mood score
            moodScores = 3;
        });
        // Set click listeners or any other event listeners as needed
        closeImageView.setOnClickListener(view -> {
            // Handle close button click
            Intent closeIntent = new Intent(MoodTrackerActivity.this, HomePageActivity.class);
            closeIntent.putExtra("email",email);
            MoodTrackerActivity.this.startActivity(closeIntent);
        });

        // Similar listeners can be added for other ImageViews, EditTexts, and Button

        saveButton.setOnClickListener(view -> {
            // Initialize the data from user
            emotion = emotionText.getText().toString();
            // check if emotion is empty before parsing
            if (emotion.isEmpty()) {
                // Show a Toast or handle the validation error as needed
                Toast.makeText(MoodTrackerActivity.this, "Emotion cannot be empty.", Toast.LENGTH_SHORT).show();
                return; // Exit the onClick method to prevent further execution
            }
            sleepHoursText = sleepHoursEditText.getText().toString();
            // check if sleepHoursText is empty before parsing
            if (sleepHoursText.isEmpty()) {
                // Show a Toast or handle the validation error as needed
                Toast.makeText(MoodTrackerActivity.this, "Sleep hours cannot be empty.", Toast.LENGTH_SHORT).show();
                return; // Exit the onClick method to prevent further execution
            }
            userNotes = userNotesEditText.getText().toString();
            // check if userNotes is empty before parsing
            if (userNotes.isEmpty()) {
                // Show a Toast or handle the validation error as needed
                Toast.makeText(MoodTrackerActivity.this, "User notes cannot be empty.", Toast.LENGTH_SHORT).show();
                return; // Exit the onClick method to prevent further execution
            }
            sleepHours = Integer.parseInt(sleepHoursText);

            DailyLog dailyLog = new DailyLog(date, moodScores, sleepHours, userNotes);
            // Get the data from patient
            IPatientHandler patientHandler = new PatientHandler();
            Patient newPatient = patientHandler.getDetails(email);

            try {
                // Get the data from mood
                dailyLogHandler.setDailyLog(newPatient, dailyLog);
                Intent saveIntent = new Intent(MoodTrackerActivity.this, DisplayDataActivity.class);
                saveIntent.putExtra("email", email);
                saveIntent.putExtra("emotion", emotion);
                saveIntent.putExtra("sleepHours", sleepHoursText);
                saveIntent.putExtra("userNotes", userNotes);
                MoodTrackerActivity.this.startActivity(saveIntent);
            } catch (InvalidDailyLogException e) {
                Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void resetColorFilter() {
        if (selectedImageView != null)
            selectedImageView.clearColorFilter();
    }
}


