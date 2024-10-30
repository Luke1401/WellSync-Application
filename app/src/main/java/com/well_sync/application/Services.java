package com.well_sync.application;

import com.well_sync.persistence.IDailyLogPersistence;
import com.well_sync.persistence.IUserPersistence;
import com.well_sync.persistence.hsqldb.DailyLogPersistenceHSQLDB;
import com.well_sync.persistence.hsqldb.UserPersistenceHSQLDB;
import com.well_sync.persistence.stubs.DailyLogPersistenceStub;
import com.well_sync.persistence.stubs.UserPersistenceStub;


public class Services {
    private static IUserPersistence userPersistence = null;

    private static IDailyLogPersistence dailyLogPersistence = null;

    public static synchronized IUserPersistence getUserPersistence(boolean production) {
        if (userPersistence == null) {
            if (production)
                userPersistence = new UserPersistenceHSQLDB(Main.getDBPathName());
            else
                userPersistence = new UserPersistenceStub();
        }
        return userPersistence;
    }

    public static synchronized IDailyLogPersistence getDailyLogPersistence(boolean production) {
        if (dailyLogPersistence == null) {
            if (production)
                dailyLogPersistence = new DailyLogPersistenceHSQLDB(Main.getDBPathName());
            else
                dailyLogPersistence = new DailyLogPersistenceStub();
        }
        return dailyLogPersistence;
    }

    public static synchronized void clean() {
        userPersistence = null;
        dailyLogPersistence = null;
    }
}
