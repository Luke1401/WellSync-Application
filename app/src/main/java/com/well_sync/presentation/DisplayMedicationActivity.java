package com.well_sync.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import android.app.Activity;

import com.well_sync.R;

public class DisplayMedicationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_med);

        // Initialize TextViews
        TextView nameText = findViewById(R.id.med_name_result);
        TextView amountText = findViewById(R.id.med_amount_result);
        TextView dosageText = findViewById(R.id.med_dosage_result);
        Button buttonContinue = findViewById(R.id.button_continue_1);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String name = intent.getStringExtra("name");
        String amount = intent.getStringExtra("amount");
        String dosage = intent.getStringExtra("dosage");
        nameText.setText("Medication Name: " + name);
        amountText.setText("Amount: " + amount);
        dosageText.setText("Dosage: " + dosage);

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle close button click
                Intent continueIntent = new Intent(DisplayMedicationActivity.this, HomePageActivity.class);
                continueIntent.putExtra("email",email);
                DisplayMedicationActivity.this.startActivity(continueIntent);
            }
        });
    }
}
