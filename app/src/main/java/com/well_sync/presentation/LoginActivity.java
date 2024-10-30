package com.well_sync.presentation;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.well_sync.R;
import com.well_sync.logic.DailyLogValidator;
import com.well_sync.logic.IUserAuthenticationHandler;
import com.well_sync.logic.PatientValidator;
import com.well_sync.logic.UserAuthenticationHandler;
import com.well_sync.logic.ValidationUtils;
import com.well_sync.logic.exceptions.InvalidCredentialsException;
import com.well_sync.objects.UserCredentials;
import com.well_sync.persistence.utils.DBHelper;


public class LoginActivity extends AppCompatActivity {

    private UserCredentials userCredentials;
    private IUserAuthenticationHandler loginHandler;
    TextView loginButton, signUpButton;
    EditText userPassword, userEmail;


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_login);

        // setup database
        DBHelper.copyDatabaseToDevice(this);

        // setup validators
        // this has to be set here, because the values are set in XML resource files that
        // can only be read in an Android-specific scope
        DailyLogValidator.setMaxima(
                getResources().getInteger(R.integer.max_mood_score),
                getResources().getInteger(R.integer.max_sleep_hours),
                getResources().getInteger(R.integer.max_med_quantity),
                getResources().getInteger(R.integer.max_med_dosage),
                getResources().getInteger(R.integer.max_symptom_intensity),
                getResources().getInteger(R.integer.max_substance_quantity)
        );
        PatientValidator.setMaxAge(getResources().getInteger(R.integer.max_age));
        ValidationUtils.setMaxNotesLength(getResources().getInteger(R.integer.max_notes_length));


        loginHandler = new UserAuthenticationHandler();
        loginButton = findViewById(R.id.Loginbutton);
        signUpButton = findViewById(R.id.SignUp);
        userEmail = findViewById(R.id.editEmail);
        userPassword = findViewById(R.id.editPassword);

        stylizeSignUpLink();


        //LOG IN BUTTON
        loginButton.setOnClickListener(v -> {
            userCredentials = getCredentials();
            try {
                UserCredentials.Role loginRole = loginHandler.login(userCredentials);
                Intent openHome;

                if (loginRole == UserCredentials.Role.PATIENT) {
                    openHome = new Intent(LoginActivity.this, HomePageActivity.class);

                } else {
                    openHome = new Intent(LoginActivity.this, DoctorPageActivity.class);
                }

                openHome.putExtra("email", userCredentials.getEmail());
                startActivity(openHome);

            } catch (InvalidCredentialsException e) {
                Toast.makeText(getApplicationContext(), "The email you entered isn’t connected to an account" , Toast.LENGTH_SHORT).show();
                resetLoginFields();
                userPassword.setError("Email or Password invalid");
                userEmail.setError("Email or Password invalid");
            }
        });

        //SIGN UP BUTTON
        signUpButton.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this,SignUpActivity.class)));

    }
    private void stylizeSignUpLink() {
        TextView signUpLink = findViewById(R.id.SignUp);
        signUpLink.setPaintFlags(signUpLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public UserCredentials getCredentials() {
        UserCredentials returningUser;
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        // role will be filled in by backend, since user already exists
        returningUser = new UserCredentials(email, password, null);
        return returningUser;
    }

    private void resetLoginFields(){
        EditText usernameInput = findViewById(R.id.editEmail);
        EditText passwordInput = findViewById(R.id.editPassword);
        usernameInput.setText("");
        passwordInput.setText("");
    }


}
