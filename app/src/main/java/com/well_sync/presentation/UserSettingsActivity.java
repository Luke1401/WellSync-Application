package com.well_sync.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.well_sync.R;
import com.well_sync.logic.IPatientHandler;
import com.well_sync.logic.PatientHandler;
import com.well_sync.objects.Patient;


public class UserSettingsActivity extends AppCompatActivity {
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        ImageView closeIcon = findViewById(R.id.close);
        Button editButton = findViewById(R.id.Editbutton);

        //get email from Sign Up Activity
        //private TextView doctor;
        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        IPatientHandler patientHandler = new PatientHandler();
        Patient patient = patientHandler.getDetails(email);

        setData(R.id.name, patient.getFirstName()+" "+patient.getLastName());
        setData(R.id.birthday, patient.getAge()+" ");
        setData(R.id.gender, patient.getSex().name());
        setData(R.id.bloodtype, patient.getBloodType().name());

        closeIcon.setOnClickListener(view -> {
            // Handle close button click
            Intent closeIntent = new Intent(UserSettingsActivity.this, HomePageActivity.class);
            closeIntent.putExtra("email",email);
            UserSettingsActivity.this.startActivity(closeIntent);
        });

        editButton.setOnClickListener(view -> {
            // Handle close button click
            Intent editIntent = new Intent(UserSettingsActivity.this, UserDetailsActivity.class);
            editIntent.putExtra("email",email);
            UserSettingsActivity.this.startActivity(editIntent);
        });

    }
    public void setData(int id, String data){
        TextView field = findViewById(id);
        field.setText(data);
    }
}