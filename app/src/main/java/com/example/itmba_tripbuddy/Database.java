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

    // Users table
    private static final String TABLE_USERS = "Users";
    private static final String USER_ID = "id";
    private static final String EMAIL = "Email";
    private static final String PASSWORD = "Password";

    // Trips table
    private static final String TABLE_TRIPS = "Trips";
    private static final String TRIP_ID = "id";
    private static final String USER_FK = "user_id"; // foreign key to Users table
    private static final String TRIP_NAME = "tripName";
    private static final String TRIP_DATE = "tripDate";
    private static final String TRIP_TYPE = "tripType";
    private static final String DESTINATION = "Destination";
    private static final String NOTES = "Notes";
    private static final String COST = "TravelCost";
    private static final String COUNTER = "TripCounter";

    public Database(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users table
        String createUsers = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " ("
                + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EMAIL + " TEXT UNIQUE, "
                + PASSWORD + " TEXT);";

        // Create Trips table
        String createTrips = "CREATE TABLE IF NOT EXISTS " + TABLE_TRIPS + " ("
                + TRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_FK + " INTEGER, "
                + TRIP_NAME + " TEXT, "
                + TRIP_DATE + " TEXT, "
                + TRIP_TYPE + " TEXT, "
                + DESTINATION + " TEXT, "
                + NOTES + " TEXT, "
                + COST + " REAL, "
                + COUNTER + " INTEGER, "
                + "FOREIGN KEY(" + USER_FK + ") REFERENCES " + TABLE_USERS + "(" + USER_ID + "));";

        db.execSQL(createUsers);
        db.execSQL(createTrips);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // ---------------- USER METHODS ---------------- //

    public boolean insertUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EMAIL, email);
        values.put(PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE " + EMAIL + "=? AND " + PASSWORD + "=?",
                new String[]{email, password}
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public int getUserId(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + USER_ID + " FROM " + TABLE_USERS + " WHERE " + EMAIL + "=? AND " + PASSWORD + "=?",
                new String[]{email, password}
        );
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return userId;
    }

    // ---------------- TRIP METHODS ---------------- //

    public boolean insertTrip(int userId, String name, String date, String type,
                              String destination, String notes, double cost, int counter) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_FK, userId);
        values.put(TRIP_NAME, name);
        values.put(TRIP_DATE, date);
        values.put(TRIP_TYPE, type);
        values.put(DESTINATION, destination);
        values.put(NOTES, notes);
        values.put(COST, cost);
        values.put(COUNTER, counter);

        long result = db.insert(TABLE_TRIPS, null, values);
        db.close();
        return result != -1;
    }

    public Cursor getTripsForUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM " + TABLE_TRIPS + " WHERE " + USER_FK + "=?",
                new String[]{String.valueOf(userId)}
        );
    }
}
