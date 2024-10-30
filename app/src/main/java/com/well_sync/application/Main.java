package com.well_sync.application;

public class Main {

    private static String dbName = "WellSyncDB";

    public static void setDBPathName(final String name) {
        try {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
        } catch (InstantiationException|IllegalAccessException|ClassNotFoundException e) {
            e.printStackTrace();
        }

        dbName = name;
    }

    public static String getDBPathName() {
        return dbName;
    }

}