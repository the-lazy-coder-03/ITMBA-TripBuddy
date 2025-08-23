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

    private SessionManager() { /* no instances */ }

    // --- Save / update ---
    public static void saveUserId(Context ctx, int userId) {
        prefs(ctx).edit().putInt(KEY_USER_ID, userId).apply();
    }

    public static void saveName(Context ctx, String name) {
        prefs(ctx).edit().putString(KEY_NAME, name).apply();
    }

    public static void saveToken(Context ctx, String token) {
        prefs(ctx).edit().putString(KEY_TOKEN, token).apply();
    }

    /** Convenience: set everything at once */
    public static void setLoggedIn(Context ctx, int userId, String name, String token) {
        prefs(ctx).edit()
                .putInt(KEY_USER_ID, userId)
                .putString(KEY_NAME, name)
                .putString(KEY_TOKEN, token)
                .apply();
    }

    // --- Read ---
    public static int getUserId(Context ctx) {
        return prefs(ctx).getInt(KEY_USER_ID, -1);
    }

    public static String getName(Context ctx) {
        return prefs(ctx).getString(KEY_NAME, null);
    }

    public static String getToken(Context ctx) {
        return prefs(ctx).getString(KEY_TOKEN, null);
    }

    public static boolean isLoggedIn(Context ctx) {
        return getUserId(ctx) != -1;
    }

    // --- Clear / logout ---
    public static void clearSession(Context ctx) {
        prefs(ctx).edit().clear().apply();
    }

    // --- Optional helper: kick to Login if not logged in ---
    public static void requireLogin(Activity activity, Class<?> loginActivityClass) {
        if (!isLoggedIn(activity)) {
            Intent i = new Intent(activity, loginActivityClass);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(i);
            activity.finish();
        }
    }

    private static SharedPreferences prefs(Context ctx) {
        return ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
