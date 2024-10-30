package com.well_sync.presentation;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.well_sync.R;
import com.well_sync.logic.IUserAuthenticationHandler;
import com.well_sync.logic.UserAuthenticationHandler;
import com.well_sync.logic.exceptions.InvalidCredentialsException;
import com.well_sync.objects.UserCredentials;

public class SignUpActivity extends AppCompatActivity {
    private UserCredentials newUser;
    private IUserAuthenticationHandler signUpHandler;
    private EditText userEmail;
    private EditText userPassword;
    private EditText userConfirmPassword;
    private Spinner roleSpinner;
    private final String[] roleList = new String[]{"PATIENT","DOCTOR"};
    private String role;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_signup);
        //Logic Layer Handler
        signUpHandler = new UserAuthenticationHandler();
        //User data
        userEmail = findViewById(R.id.editEmail);
        userPassword = findViewById(R.id.editPassword);
        userConfirmPassword = findViewById(R.id.editConfirmPassword);
        //Roles
        roleSpinner = findViewById(R.id.Roles);
        //next Button
        Button nextButton = findViewById(R.id.Nextbutton);
        setRoles();

        //ROLES SPINNER
        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                role = adapterView.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
                role = null;
                Toast.makeText(getApplicationContext(), "Please select role", Toast.LENGTH_SHORT).show();
            }
        });

        //NEXT BUTTON
        nextButton.setOnClickListener(v -> {
            //get input from user
            String email = userEmail.getText().toString();
            String password = userPassword.getText().toString();
            String confirmPassword = userConfirmPassword.getText().toString();

            if(!password.equals(confirmPassword)) {
                //if passwords don't match
                userPassword.setError("Passwords don't match. Try again");
                userPassword.requestFocus();
                resetPasswords();
            }else{

                //create user credentials
                newUser = new UserCredentials(email, password, role);
                try {
                    signUpHandler.register(newUser);

                    // if fields were valid
                    if (newUser.getRole() == UserCredentials.Role.PATIENT) {
                        Intent openUserDetails=  new Intent(SignUpActivity.this, UserDetailsActivity.class);
                        openUserDetails.putExtra("email",email);
                        startActivity(openUserDetails);

                    } else {
                        Intent openDoctorView=  new Intent(SignUpActivity.this, DoctorSignUpActivity.class);
                        openDoctorView.putExtra("email",email);
                        startActivity(openDoctorView);
                    }
                } catch (InvalidCredentialsException e) {
                    userPassword.setError("Email or Password invalid");
                    userPassword.requestFocus();
                    userEmail.requestFocus();
                    resetSignUpFields();
                    Toast.makeText(getApplicationContext(), "Not Registered. Invalid values, try again" , Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
    /**
     Function: Set values for roles
     */
    private void setRoles(){
        ArrayAdapter<String> adapterRole = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roleList);
        roleSpinner.setAdapter(adapterRole);
    }
    /**
     Function: reset the password entry fields in case passwords don't match
     */
    private void resetPasswords(){
        EditText Password = findViewById(R.id.editPassword);
        EditText ConfirmPassword = findViewById(R.id.editConfirmPassword);
        Password.setText("");
        ConfirmPassword.setText("");
    }

    /**
     Function: reset all of the data entry fields in case registration fails
     */
    private void resetSignUpFields(){
        userEmail = findViewById(R.id.editEmail);
        userEmail.setText("");
        resetPasswords();

    }


}