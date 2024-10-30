package com.well_sync.tests;

import com.well_sync.tests.business.DailyLogHandlerIT;
import com.well_sync.tests.business.DoctorHandlerIT;
import com.well_sync.tests.business.PatientHandlerIT;
import com.well_sync.tests.business.UserAuthenticationHandlerIT;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        DailyLogHandlerIT.class,
        DoctorHandlerIT.class,
        PatientHandlerIT.class,
        UserAuthenticationHandlerIT.class
})
public class AllIntegrationTests {}
