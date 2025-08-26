package com.example.itmba_tripbuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    // --- DB ---
    private static final String DB_NAME = "TripInfo.db";
    // Bump version when schema changes
    private static final int DB_VERSION = 6;

    // --- Users ---
    private static final String TABLE_USERS = "Users";
    private static final String USER_ID = "id";
    private static final String EMAIL = "Email";
    private static final String PASSWORD = "Password";

    // --- Trips ---
    private static final String TABLE_TRIPS = "Trips";
    private static final String TRIP_ID = "id";
    private static final String USER_FK = "user_id";
    private static final String TRIP_NAME = "tripName";
    private static final String TRIP_DATE = "tripDate";
    private static final String TRIP_TYPE = "tripType";
    private static final String DESTINATION = "Destination";
    private static final String NOTES = "Notes";
    private static final String COST = "TravelCost";
    private static final String COUNTER = "TripCounter";

    // --- Memories ---
    private static final String TABLE_MEMORIES = "Memories";
    private static final String MEMORY_ID = "id";
    private static final String MEMORY_USER_ID = "user_id";
    private static final String MEMORY_CAPTION = "caption";
    private static final String MEMORY_IMAGE_URI = "imageUri";
    private static final String MEMORY_MUSIC_URI = "musicUri";

    public Database(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // Required for SQLite on Android to enforce FK
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // --- Users ---
        final String createUsers = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " ("
                + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EMAIL + " TEXT UNIQUE, "
                + PASSWORD + " TEXT"
                + ");";

        // --- Trips ---
        final String createTrips = "CREATE TABLE IF NOT EXISTS " + TABLE_TRIPS + " ("
                + TRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_FK + " INTEGER NOT NULL, "
                + TRIP_NAME + " TEXT, "
                + TRIP_DATE + " TEXT, "
                + TRIP_TYPE + " TEXT, "
                + DESTINATION + " TEXT, "
                + NOTES + " TEXT, "
                + COST + " REAL, "
                + COUNTER + " INTEGER, "
                + "FOREIGN KEY(" + USER_FK + ") REFERENCES " + TABLE_USERS + "(" + USER_ID + ") ON DELETE CASCADE"
                + ");";

        // --- Memories ---
        final String createMemories = "CREATE TABLE IF NOT EXISTS " + TABLE_MEMORIES + " ("
                + MEMORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MEMORY_USER_ID + " INTEGER NOT NULL, "
                + MEMORY_CAPTION + " TEXT, "
                + MEMORY_IMAGE_URI + " TEXT, "
                + MEMORY_MUSIC_URI + " TEXT, "
                + "FOREIGN KEY(" + MEMORY_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + USER_ID + ") ON DELETE CASCADE"
                + ");";

        // --- Indices for faster lookups ---
        final String idxTripsUser = "CREATE INDEX IF NOT EXISTS idx_trips_user ON " + TABLE_TRIPS + "(" + USER_FK + ");";
        final String idxMemoriesUser = "CREATE INDEX IF NOT EXISTS idx_memories_user ON " + TABLE_MEMORIES + "(" + MEMORY_USER_ID + ");";
        final String idxUsersEmail = "CREATE INDEX IF NOT EXISTS idx_users_email ON " + TABLE_USERS + "(" + EMAIL + ");";

        db.beginTransaction();
        try {
            db.execSQL(createUsers);
            db.execSQL(createTrips);
            db.execSQL(createMemories);
            db.execSQL(idxTripsUser);
            db.execSQL(idxMemoriesUser);
            db.execSQL(idxUsersEmail);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Simple upgrade strategy:
     * - Recreate tables if missing
     * - Add indices if missing
     * (Keeps existing data; no destructive drops unless absolutely needed)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For schema tweaks, ensure tables/indices exist
        onCreate(db);
    }

    // ---------------------------
    // Users
    // ---------------------------

    public boolean insertUser(String email, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EMAIL, email);
        values.put(PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        try (Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT 1 FROM " + TABLE_USERS + " WHERE " + EMAIL + "=? AND " + PASSWORD + "=? LIMIT 1",
                new String[]{email, password}
        )) {
            return cursor.moveToFirst();
        }
    }

    public int getUserId(String email, String password) {
        try (Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT " + USER_ID + " FROM " + TABLE_USERS + " WHERE " + EMAIL + "=? AND " + PASSWORD + "=? LIMIT 1",
                new String[]{email, password}
        )) {
            if (cursor.moveToFirst()) return cursor.getInt(0);
            return -1;
        }
    }

    // ---------------------------
    // Trips
    // ---------------------------

    public boolean insertTrip(int userId, String name, String date, String type,
                              String destination, String notes, double cost, int counter) {
        SQLiteDatabase db = getWritableDatabase();
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
        return result != -1;
    }

    public int getTripCountForUser(int userId) {
        try (Cursor c = getReadableDatabase().rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_TRIPS + " WHERE " + USER_FK + "=?",
                new String[]{String.valueOf(userId)}
        )) {
            if (c.moveToFirst()) return c.getInt(0);
            return 0;
        }
    }

    // If you ever need it:
    public Cursor getTripsForUser(int userId) {
        // Caller must close the returned Cursor.
        return getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_TRIPS + " WHERE " + USER_FK + "=? ORDER BY " + TRIP_ID + " ASC",
                new String[]{String.valueOf(userId)}
        );
    }

    // ---------------------------
    // Memories
    // ---------------------------

    public boolean insertMemory(int userId, String caption, String imageUri, String musicUri) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MEMORY_USER_ID, userId);
        values.put(MEMORY_CAPTION, caption);
        values.put(MEMORY_IMAGE_URI, imageUri);
        values.put(MEMORY_MUSIC_URI, musicUri);

        long result = db.insert(TABLE_MEMORIES, null, values);
        return result != -1;
    }

    public Cursor getMemoriesForUser(int userId) {
        // Caller must close the returned Cursor.
        return getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_MEMORIES + " WHERE " + MEMORY_USER_ID + "=? ORDER BY " + MEMORY_ID + " ASC",
                new String[]{String.valueOf(userId)}
        );
    }

    public Cursor getLatestMemoryForUser(int userId) {
        // Convenience method; caller must close.
        return getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_MEMORIES + " WHERE " + MEMORY_USER_ID + "=? ORDER BY " + MEMORY_ID + " DESC LIMIT 1",
                new String[]{String.valueOf(userId)}
        );
    }

    // Optional helpers you might want later:
    // public int deleteMemoryById(int memoryId) { ... }
    // public int updateUserPassword(int userId, String newPass) { ... }
}
