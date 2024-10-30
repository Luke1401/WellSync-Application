package com.well_sync.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.well_sync.R;
import com.well_sync.logic.IPatientHandler;
import com.well_sync.logic.PatientHandler;
import com.well_sync.logic.exceptions.InvalidPatientException;
import com.well_sync.objects.Patient;

import java.util.Calendar;
public class UserDetailsActivity extends AppCompatActivity {
    private IPatientHandler patientHandler;
    //User data
    private EditText userFirstName,userLastName;
    private DatePicker datePicker;
    private RadioGroup genderPicker;
    private Spinner bloodTypeSpinner;
    private String[] bloodTypeList;

    private Intent intent;
    private String bloodType, gender, firstName, lastName, email;
    private int age;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_userdetails);

        userFirstName = findViewById(R.id.editFirstName);
        userLastName = findViewById(R.id.editLastName);
        genderPicker = findViewById(R.id.GenderPicker);
        Button saveButton = findViewById(R.id.savebutton);
        datePicker = findViewById(R.id.datePicker);
        bloodTypeList = getResources().getStringArray(R.array.blood_types);
        bloodTypeSpinner = findViewById(R.id.BloodTypes);
        patientHandler = new PatientHandler();
        setBloodTypeValues();


        bloodTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                bloodType = adapterView.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
                bloodType = null;
                Toast.makeText(getApplicationContext(), "Please select blood type", Toast.LENGTH_SHORT).show();
            }
        });

        saveButton.setOnClickListener(v -> {
            //get email from Sign Up Activity
            intent = getIntent();
            email = intent.getStringExtra("email");
            //get input from user
            firstName = userFirstName.getText().toString();
            lastName = userLastName.getText().toString();
            gender=getGender();
            bloodType=getBloodType();
            age=getAge();

            Patient newPatient= new Patient(email,firstName,lastName,bloodType,gender,age," ");
            try {
                patientHandler.editPatientDetails(newPatient);
                Intent homeIntent =new Intent(UserDetailsActivity.this, HomePageActivity.class);
                homeIntent.putExtra("email",email);
                startActivity(homeIntent);
            } catch (InvalidPatientException e) {
                Toast.makeText(getApplicationContext(),"Invalid patient details", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setBloodTypeValues(){
        ArrayAdapter<String> adapterBloodType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, bloodTypeList);
        bloodTypeSpinner.setAdapter(adapterBloodType);
    }
    private String getGender() {
        String gender=null;
        if (genderPicker.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "Please select gender", Toast.LENGTH_SHORT).show();
        } else {
            RadioButton selectedGender = findViewById(genderPicker.getCheckedRadioButtonId());
            gender = selectedGender.getText().toString();
        }
        return gender;
    }

    private int getAge() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int userAge = datePicker.getYear();
        return currentYear - userAge;
    }
    private String getBloodType(){
        return bloodType;
    }

}