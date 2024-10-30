package com.well_sync.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.well_sync.R;
import com.well_sync.logic.DailyLogHandler;
import com.well_sync.logic.IDailyLogHandler;
import com.well_sync.logic.IPatientHandler;
import com.well_sync.logic.PatientHandler;
import com.well_sync.objects.DailyLog;
import com.well_sync.objects.Medication;
import com.well_sync.objects.Patient;
import com.well_sync.objects.Substance;
import com.well_sync.objects.Symptom;
import java.util.List;

public class DailyLogPatientActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_log_patient);

        IDailyLogHandler dailyLogHandler = new DailyLogHandler();
        Intent intent = getIntent();
        String date = intent.getStringExtra("date");

        //get email and date from HomePage Activity
        String patientEmail = intent.getStringExtra("patientEmail");
        String doctorEmail = intent.getStringExtra("doctorEmail");
        // Get the data from patient
        IPatientHandler patientHandler = new PatientHandler();
        Patient newPatient = patientHandler.getDetails(patientEmail);
        DailyLog dailyLog = dailyLogHandler.getDailyLog(newPatient, date);
        //Get lists
        List<Symptom> symptomList = dailyLog.getSymptoms();
        List<Medication> medicationList =dailyLog.getMedications();
        List<Substance> substanceList = dailyLog.getSubstances();
        //DATE
        TextView viewDate = findViewById(R.id.date);
        viewDate.setText(date);
        //MOOD LOG
        showValue(R.id.mood,dailyLog.getMoodScore());
        showValue(R.id.sleep,dailyLog.getSleepHours());
        showText(R.id.Notes,dailyLog.getNotes());
        if(symptomList.size()>0) {
            //SYMPTOMS LOG
            showValue(R.id.Sadness, symptomList.get(0).getIntensity());
            showValue(R.id.Helplessness, symptomList.get(1).getIntensity());
            showValue(R.id.LowSelfEsteem, symptomList.get(2).getIntensity());
            showValue(R.id.Isolation, symptomList.get(3).getIntensity());
            showValue(R.id.LowMotivation, symptomList.get(4).getIntensity());
            showValue(R.id.Impulsivity, symptomList.get(5).getIntensity());
            showValue(R.id.Concentration, symptomList.get(6).getIntensity());
            showValue(R.id.Aggressiveness, symptomList.get(7).getIntensity());
            showValue(R.id.Inability, symptomList.get(8).getIntensity());
            showValue(R.id.RacingThoughts, symptomList.get(9).getIntensity());
            showValue(R.id.Anxiety, symptomList.get(10).getIntensity());
            showValue(R.id.sleep_problems, symptomList.get(11).getIntensity());
            showValue(R.id.Headache, symptomList.get(12).getIntensity());
            showValue(R.id.Pain, symptomList.get(13).getIntensity());
            showValue(R.id.Appetite, symptomList.get(14).getIntensity());
            showValue(R.id.Guilt, symptomList.get(15).getIntensity());
            showValue(R.id.Suicide, symptomList.get(16).getIntensity());
        }
        //MEDICATION LOG
        if(medicationList.size()>0) {
            showText(R.id.PillsName, medicationList.get(0).getName());
            showValue(R.id.PillsAmount, medicationList.get(0).getQuantity());
            showValue(R.id.PillsDosage, medicationList.get(0).getQuantity());
        }
            //SUBSTANCES LOG
        if(substanceList.size()>0) {
            showText(R.id.SubsName, substanceList.get(0).getName());
            showValue(R.id.amount, substanceList.get(0).getQuantity());
        }
        //BUTTON
        Button backButton = findViewById(R.id.backbutton);

        backButton.setOnClickListener(v -> {
            Intent saveIntent = new Intent(DailyLogPatientActivity.this, PatientInfoActivity.class);
            saveIntent.putExtra("date", date);
            saveIntent.putExtra("patientEmail",patientEmail);
            saveIntent.putExtra("doctorEmail", doctorEmail);
            DailyLogPatientActivity.this.startActivity(saveIntent);
        });
    }
    private void showValue(int id, int  index){
        TextView value = findViewById(id);
        value.setText(String.valueOf(index));
    }
    private void showText(int id, String text){
        TextView value = findViewById(id);
        value.setText(text);
    }
}
