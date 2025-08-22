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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        // Ensure DB is created if first run (optional – you don't need to keep a reference)
        new Database(this).getWritableDatabase().close();

        // 1) Try to get userId from the Intent
        userId = getIntent().getIntExtra("userId", -1);
        // 2) Fallback to Session (handles cases where Intent extra wasn't forwarded)
        if (userId == -1) userId = SessionManager.getUserId(this);

        if (userId == -1) {
            Toast.makeText(this, "No user logged in. Please log in again.", Toast.LENGTH_SHORT).show();
            // Optionally redirect to LoginScreen
            startActivity(new Intent(this, LoginScreen.class));
            finish();
            return;
        }

        Button planTripBtn = findViewById(R.id.PlanTripbtn);
        planTripBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SaveTrip.class);
            intent.putExtra("userId", userId); // ✅ forward the logged-in user
            startActivity(intent);
        });
    }
}
