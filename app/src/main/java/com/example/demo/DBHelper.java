package com.example.demo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DBVersion = 3;
    public static final String DBName = "DBExample.db";


    public DBHelper (Context context) {
        super(context, DBName, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL(DBContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int OldVersion, int NewVersion) {
        DB.execSQL(DBContract.SQL_DELETE_ENTRIES);
        onCreate(DB);
    }

}
