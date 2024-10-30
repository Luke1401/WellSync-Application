package com.well_sync.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.well_sync.R;

public class DisplayDataActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        // Initialize TextViews
        TextView emotionsText = findViewById(R.id.emotions_result);
        TextView sleepHoursText = findViewById(R.id.sleep_hours_result);
        TextView userNotesText = findViewById(R.id.note_result);
        Button buttonContinue = findViewById(R.id.button_continue);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String emotion = intent.getStringExtra("emotion");
        String sleepHours = intent.getStringExtra("sleepHours");
        String userNotes = intent.getStringExtra("userNotes");
        emotionsText.setText("Emotion: " + emotion);
        sleepHoursText.setText("Sleep Hours: " + sleepHours);
        userNotesText.setText("User Notes: " + userNotes);

        buttonContinue.setOnClickListener(view -> {
            // Handle close button click
            Intent continueIntent = new Intent(DisplayDataActivity.this, HomePageActivity.class);
            continueIntent.putExtra("email",email);
            DisplayDataActivity.this.startActivity(continueIntent);
        });
    }
}
