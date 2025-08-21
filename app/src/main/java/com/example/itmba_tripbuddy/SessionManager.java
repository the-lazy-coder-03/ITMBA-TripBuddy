package com.example.itmba_tripbuddy;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "TripBuddySession";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_EMAIL = "email";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // Save user session
    public void createLoginSession(int userId, String email) {
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    // Get user id
    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    // Get email
    public String getEmail() {
        return prefs.getString(KEY_EMAIL, null);
    }

    // Check if logged in
    public boolean isLoggedIn() {
        return getUserId() != -1;
    }

    // Logout
    public void logout() {
        editor.clear();
        editor.apply();
    }
}
