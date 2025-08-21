package com.example.itmba_tripbuddy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    // Database details
    private static final String DB_NAME = "TripInfo.db";
    private static final int DB_VERSION = 1;

    // Table and columns
    private static final String TABLE_NAME = "TRIPINFO";
    private static final String COL_ID = "id";
    private static final String COL_EMAIL = "Email";
    private static final String COL_PASSWORD = "Password";
    private static final String COL_DESTINATION = "Destination";
    private static final String COL_NOTES = "Notes";
    private static final String COL_TYPE = "SpinnerInput";   // trip type (from spinner)
    private static final String COL_COST = "TravelCost";     // cost of travel
    private static final String COL_COUNTER = "TripCounter"; // number of trips

    public Database(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table with all columns
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_EMAIL + " TEXT, "
                + COL_PASSWORD + " TEXT, "
                + COL_DESTINATION + " TEXT, "
                + COL_NOTES + " TEXT, "
                + COL_TYPE + " TEXT, "
                + COL_COST + " REAL, "
                + COL_COUNTER + " INTEGER"
                + ");";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old table if upgrading, then recreate
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
