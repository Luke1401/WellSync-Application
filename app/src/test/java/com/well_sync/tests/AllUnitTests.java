package com.well_sync.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

// Import the individual test files
import com.well_sync.tests.business.DailyLogHandlerTest;
import com.well_sync.tests.business.DoctorHandlerTest;
import com.well_sync.tests.business.PatientHandlerTest;
import com.well_sync.tests.business.UserAuthenticationHandlerTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        UserAuthenticationHandlerTest.class,
        DailyLogHandlerTest.class,
        PatientHandlerTest.class,
        DoctorHandlerTest.class
})
public class AllUnitTests {}
