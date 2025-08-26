package com.example.itmba_tripbuddy;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public final class DarkModeManager {
    private static final String PREF = "appearance_prefs";
    private static final String KEY_DARK = "dark_enabled";



    public static void setDarkEnabled(Context ctx, boolean enabled) {
        prefs(ctx).edit().putBoolean(KEY_DARK, enabled).apply();
        AppCompatDelegate.setDefaultNightMode(
                enabled ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }

    public static boolean isDarkEnabled(Context ctx) {
        return prefs(ctx).getBoolean(KEY_DARK, false);
    }

    public static void applySavedMode(Context ctx) {
        AppCompatDelegate.setDefaultNightMode(
                isDarkEnabled(ctx) ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }

    private static SharedPreferences prefs(Context ctx) {
        return ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }
}
