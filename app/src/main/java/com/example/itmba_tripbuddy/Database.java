package com.example.itmba_tripbuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    // Database details
    private static final String DB_NAME = "TripInfo.db";
    private static final int DB_VERSION = 1;

    // Table and columns
    private static final String TABLE_NAME = "TRIPINFO";
    private static final String ID = "id";
    private static final String EMAIL = "Email";
    private static final String PASSWORD = "Password";
    private static final String DESTINATION = "Destination";
    private static final String NOTES = "Notes";
    private static final String TYPE = "SpinnerInput";   // trip type (from spinner)
    private static final String COST = "TravelCost";     // cost of travel
    private static final String COUNTER = "TripCounter"; // number of trips

    public Database(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table with all columns
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EMAIL + " TEXT, "
                + PASSWORD + " TEXT, "
                + DESTINATION + " TEXT, "
                + NOTES + " TEXT, "
                + TYPE + " TEXT, "
                + COST + " REAL, "
                + COUNTER + " INTEGER"
                + ");";

        db.execSQL(createTable);
        System.out.println("The db was created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old table if upgrading, then recreate
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    // Insert a new user
    public boolean insertUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Email", email);
        values.put("Password", password);

        long result = db.insert("TRIPINFO", null, values);
        db.close();
        return result != -1; // true if successful
    }
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to check if user exists with matching email and password
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + EMAIL + " = ? AND " + PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.rawQuery(query, selectionArgs);
        boolean userExists = cursor.getCount() > 0;

        cursor.close();
        db.close();
        return userExists;
    }

}

