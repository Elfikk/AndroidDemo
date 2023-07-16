package com.example.demo;

import android.provider.BaseColumns;

public final class DBContract {

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ExampleColumns.TABLE_NAME + " (" +
                    ExampleColumns._ID + " INTEGER PRIMARY KEY," +
                    ExampleColumns.COLUMN_NAME_TITLE + " INTEGER," +
                    ExampleColumns.COLUMN_NAME_SUBTITLE + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ExampleColumns.TABLE_NAME;


    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DBContract() {}

    /* Inner class that defines the table contents */
    public static class ExampleColumns implements BaseColumns {
        public static final String TABLE_NAME = "example";
        public static final String COLUMN_NAME_TITLE = "numba";
        public static final String COLUMN_NAME_SUBTITLE = "sub_numba";
    }


}
