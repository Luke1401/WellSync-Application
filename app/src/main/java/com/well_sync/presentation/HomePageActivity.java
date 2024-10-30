package com.well_sync.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.well_sync.R;
import com.well_sync.logic.DailyLogHandler;
import com.well_sync.logic.IDailyLogHandler;
import com.well_sync.logic.IPatientHandler;
import com.well_sync.logic.PatientHandler;
import com.well_sync.objects.DailyLog;
import com.well_sync.objects.Patient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomePageActivity extends AppCompatActivity {

    private String email;
    private DailyLog dailyLog;
    RelativeLayout layout;
    Patient newPatient;


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);

        //get email from Activity
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        String date = getCurrentDate();

        IDailyLogHandler dailyLogHandler = new DailyLogHandler();

        // Get the data from patient
        IPatientHandler patientHandler = new PatientHandler();
        newPatient = patientHandler.getDetails(email);
        dailyLog = dailyLogHandler.getDailyLog(newPatient, date);

        // Find your widgets by their IDs
        layout= findViewById(R.id.layout);
        LinearLayout moodLayout = findViewById(R.id.moodID);
        LinearLayout symptomLayout = findViewById(R.id.symptomID);
        LinearLayout medicationLayout = findViewById(R.id.medicationID);
        LinearLayout substanceLayout = findViewById(R.id.substanceUseID);
        Button logoutButton = findViewById(R.id.logout);
        ImageView notification = findViewById(R.id.notification);
        ImageView userAccess = findViewById(R.id.user);
        TextView notificationDot =findViewById(R.id.notification_count);
        notificationDot.setText(setNotification());

        // Set click listeners for each widget
        userAccess.setOnClickListener(view -> {
            // Handle close button click
            Intent userIntent = new Intent(HomePageActivity.this, UserSettingsActivity.class);
            userIntent.putExtra("email",email);
            HomePageActivity.this.startActivity(userIntent);
        });
        notification.setOnClickListener(view -> {
            notificationDot.setText("");
            createPopUpWindow();

        });
        // Set click listeners for each widget
        moodLayout.setOnClickListener(v -> {
            if(dailyLog==null) {
                Intent moodIntent = new Intent(HomePageActivity.this, MoodTrackerActivity.class);
                moodIntent.putExtra("email",email);
                moodIntent.putExtra("date",date);
                HomePageActivity.this.startActivity(moodIntent);
            }else{
                Toast.makeText(HomePageActivity.this, "You already filled your mood for today!", Toast.LENGTH_SHORT).show();
            }
        });
        symptomLayout.setOnClickListener(v -> {
            if(dailyLog!=null) {
                if(dailyLog.getSymptoms().size()>0){
                    Toast.makeText(HomePageActivity.this, "You already filled your symptoms for today!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent symptomIntent = new Intent(HomePageActivity.this, SymptomsTrackerActivity.class);
                    symptomIntent.putExtra("email", email);
                    symptomIntent.putExtra("date", date);
                    HomePageActivity.this.startActivity(symptomIntent);
                }
            }else{
                Toast.makeText(HomePageActivity.this, "You must fill the mood tracker first.", Toast.LENGTH_SHORT).show();
            }
        });
        medicationLayout.setOnClickListener(v -> {
            if(dailyLog!=null) {
                if(dailyLog.getMedications().size()>0){
                    Toast.makeText(HomePageActivity.this, "You already filled your medication for today!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent medicationIntent = new Intent(HomePageActivity.this, MedicationTrackerActivity.class);
                    medicationIntent.putExtra("email", email);
                    medicationIntent.putExtra("date", date);
                    HomePageActivity.this.startActivity(medicationIntent);
                }
            }else{
                Toast.makeText(HomePageActivity.this, "You must fill the mood tracker first.", Toast.LENGTH_SHORT).show();
            }
        });
        substanceLayout.setOnClickListener(v -> {
            if(dailyLog!=null) {
                if(dailyLog.getSubstances().size()>0){
                    Toast.makeText(HomePageActivity.this, "You already filled your substance use for today!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent substanceIntent = new Intent(HomePageActivity.this, SubstanceUseTrackerActivity.class);
                    substanceIntent.putExtra("email", email);
                    substanceIntent.putExtra("date", date);
                    HomePageActivity.this.startActivity(substanceIntent);
                }
            }else{
                Toast.makeText(HomePageActivity.this, "You must fill the mood tracker first.", Toast.LENGTH_SHORT).show();
            }
        });

        logoutButton.setOnClickListener(v ->
                startActivity(new Intent(HomePageActivity.this,LoginActivity.class)));


    }
    private String getCurrentDate(){
        //get current date
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        return dateFormat.format(date);
    }
    private void createPopUpWindow(){
        LayoutInflater inflater= (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView=inflater.inflate(R.layout.message_popup, null);
        TextView message =  popUpView.findViewById(R.id.message);
        ImageView close =popUpView.findViewById(R.id.close);
        int width= ViewGroup.LayoutParams.MATCH_PARENT;
        int height= ViewGroup.LayoutParams.WRAP_CONTENT;
        boolean focusable=true;
        message.setText(newPatient.getDoctorNotes());
        PopupWindow popupWindow=new PopupWindow(popUpView, width, height,focusable);
        layout.post(() -> popupWindow.showAtLocation(layout, Gravity.TOP, 0, 0));
        close.setOnClickListener(v -> popupWindow.dismiss());
    }
    private String setNotification(){
       String text="";
        if(newPatient.getDoctorNotes()!=null){
            text="  1  ";
        }
        return text;
    }
}