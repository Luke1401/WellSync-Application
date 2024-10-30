package com.well_sync.system_tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

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
public class CheckDailyLogTest {

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
    public void checkDailyLogsTest() {
        onView(withId(R.id.daily_logs)).perform(click());
        onView(withId(R.id.date_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.date)).check(matches(withText("2023-01-01")));
        onView(withId(R.id.Notes)).check(matches(withText("Feeling good today")));
        onView(withId(R.id.PillsName)).check(matches(withText("Tylenol")));
        onView(withId(R.id.backbutton)).perform(scrollTo(), click());
    }
    @Test
    public void checkUserProgress() {
        onView(withId(R.id.see_progress)).perform(click());
        onView(withId(R.id.close)).perform(scrollTo(), click());
    }
}
