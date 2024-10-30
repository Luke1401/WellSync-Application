package com.well_sync.system_tests;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        LoginSignupTest.class,
        CheckDailyLogTest.class,
        DailyLogTest.class,
        AddPatientForDoctorTest.class,
        EditPatientDetailTest.class,
        SeeDoctorNotesTest.class,
        TrackPatientDetailTest.class,

})
public class AllAcceptanceTests {
}