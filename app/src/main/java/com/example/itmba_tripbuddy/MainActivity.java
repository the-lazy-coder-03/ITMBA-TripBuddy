package com.example.itmba_tripbuddy;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Handle system window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 🔹 Initialize the database (this will call onCreate() if first run)
        Database dbHelper = new Database(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Initialize button and set click listener
        Button planTripBtn = findViewById(R.id.PlanTripbtn);
        planTripBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SaveTrip.class);
            startActivity(intent);
        });
    }
}
