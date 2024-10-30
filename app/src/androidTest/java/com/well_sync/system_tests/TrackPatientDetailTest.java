package com.well_sync.system_tests;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.well_sync.R;
import com.well_sync.presentation.LoginActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class TrackPatientDetailTest {

    @Before
    public void loginTest() {
        ActivityScenario.launch(LoginActivity.class);
        onView(withId(R.id.editEmail)).perform(typeText("doctor1@example.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editPassword)).perform(typeText("password1"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.Loginbutton)).perform(click());

        // go to patient detail page
        onView(withId(R.id.patientList)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @Test
    public void sendNoteToPatientTest() {
        onView(withId(R.id.advise_from_doctor)).perform(typeText("You should eat more vegetables"));
        onView(withId(R.id.send_rec)).perform(click());
    }
    @Test
    public void checkProgressTest() {
        onView(withId(R.id.see_progress)).perform(click());
    }

    @Test
    public void deletePatientTest() {
        onView(withId(R.id.delete_patient)).perform(click());
    }
}
