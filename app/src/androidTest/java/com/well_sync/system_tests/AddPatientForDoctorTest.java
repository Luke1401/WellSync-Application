package com.well_sync.system_tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.well_sync.R;
import com.well_sync.presentation.LoginActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddPatientForDoctorTest {
    @Before
    public void loginTest() {
        ActivityScenario.launch(LoginActivity.class);
        onView(withId(R.id.editEmail)).perform(typeText("doctor1@example.com"), closeSoftKeyboard());
        onView(withId(R.id.editPassword)).perform(typeText("password1"), closeSoftKeyboard());
        onView(withId(R.id.Loginbutton)).perform(click());
    }
    @Test
    public void addPatientTest() {
        onView(withId(R.id.patient_email_input)).perform(replaceText("patient2@example.com"), closeSoftKeyboard());
        onView(withId(R.id.add_patient)).perform(click());

        onView(withId(R.id.patientList)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.name)).check(matches(withText("Jane Smith")));
        onView(withId(R.id.gender)).check(matches(withText("FEMALE")));
    }
}
