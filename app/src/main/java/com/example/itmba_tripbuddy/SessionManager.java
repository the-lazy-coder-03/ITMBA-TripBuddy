package com.example.itmba_tripbuddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public final class SessionManager {

    private static final String PREF_NAME   = "UserSession";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_NAME    = "name";      // optional
    private static final String KEY_TOKEN   = "token";     // optional



    // --- Save / update ---
    public static void saveUserId(Context ctx, int userId) {
        prefs(ctx).edit().putInt(KEY_USER_ID, userId).apply();
    }



    // --- Read ---
    public static int getUserId(Context ctx) {
        return prefs(ctx).getInt(KEY_USER_ID, -1);
    }


    // --- Clear / logout ---
    public static void clearSession(Context ctx) {
        prefs(ctx).edit().clear().apply();
    }

    // --- Optional helper: kick to Login if not logged in ---


    private static SharedPreferences prefs(Context ctx) {
        return ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
