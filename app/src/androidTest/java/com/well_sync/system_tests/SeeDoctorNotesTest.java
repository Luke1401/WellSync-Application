package com.well_sync.system_tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
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
public class SeeDoctorNotesTest {

    @Before
    public void setNotesTest() {
        ActivityScenario.launch(LoginActivity.class);
        onView(withId(R.id.editEmail)).perform(typeText("doctor1@example.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editPassword)).perform(typeText("password1"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.Loginbutton)).perform(click());
        // go to patient detail page
        onView(withId(R.id.patientList)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.advise_from_doctor)).perform(typeText("Start taking vitamin D once a day"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.send_rec)).perform(click());
        // perform log out
        onView(withId(R.id.logout)).perform(click());
    }

    @Test
    public void doctorNoteTest() throws InterruptedException {
        onView(withId(R.id.editEmail)).perform(typeText("patient1@example.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editPassword)).perform(typeText("password1"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.Loginbutton)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.notification)).perform(click());
        Thread.sleep(1500);
        onView(withId(R.id.close)).perform(click());
    }
}
