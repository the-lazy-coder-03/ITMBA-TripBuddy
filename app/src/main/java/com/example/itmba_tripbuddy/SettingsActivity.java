package com.example.itmba_tripbuddy;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Apply saved mode before inflating UI (prevents flicker)
        DarkMode.applySavedMode(getApplicationContext());

        setContentView(R.layout.activity_settings);

        // Dark Mode switch
        Switch darkModeSw = findViewById(R.id.DarkModeSw);
        darkModeSw.setChecked(DarkMode.isDarkEnabled(this));
        darkModeSw.setOnCheckedChangeListener((btn, isChecked) -> {
            DarkMode.setDarkEnabled(this, isChecked);
            recreate(); // refresh this screen immediately
        });

        // Back button: just finish to return to previous screen
        Button back = findViewById(R.id.btnBackToMain);
        back.setOnClickListener(v -> finish());
    }
}
