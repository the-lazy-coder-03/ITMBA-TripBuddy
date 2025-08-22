package com.example.itmba_tripbuddy;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF = "trip_prefs";
    private static final String KEY_USER_ID = "user_id";

    public static void saveUserId(Context ctx, int userId) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_USER_ID, userId).apply();
    }

    public static int getUserId(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getInt(KEY_USER_ID, -1);
    }

    public static void clear(Context ctx) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit().clear().apply();
    }
}
