package com.example.itmba_tripbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        DarkMode.applySavedMode(getApplicationContext());

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) userId = SessionManager.getUserId(this);

        if (userId == -1) {
            Toast.makeText(this, "User not logged in .Try again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginScreen.class));
            finish();
            return;
        }


        // Plan Trip
        Button planTripBtn = findViewById(R.id.PlanTripbtn);
        planTripBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlanTrip.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        // View Gallery
        Button viewGalleryBtn = findViewById(R.id.ViewGallerybtn);
        viewGalleryBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewGalleryClass.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        // Create Memory
        Button createMemBtn = findViewById(R.id.CreateMembtn);
        createMemBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateMemory.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        // Settings
        Button settingsBtn = findViewById(R.id.SettingsBtn);
        settingsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        // Logout
        Button logoutBtn = findViewById(R.id.btnLogout);
        logoutBtn.setOnClickListener(v -> {
            SessionManager.clearSession(MainActivity.this);
            Intent intent = new Intent(MainActivity.this, LoginScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
