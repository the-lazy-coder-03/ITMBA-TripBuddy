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

        // Ensure DB exists (optional)
        new Database(this).getWritableDatabase().close();

        // Get userId from intent or session
        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) userId = SessionManager.getUserId(this);

        if (userId == -1) {
            Toast.makeText(this, "No user logged in. Please log in again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginScreen.class));
            finish();
            return;
        }

        // Plan Trip button -> PlanTrip
        Button planTripBtn = findViewById(R.id.PlanTripbtn);
        planTripBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlanTrip.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        // View Gallery button -> ViewGalleryClass (your saved trips list)
        Button viewGalleryBtn = findViewById(R.id.ViewGallerybtn);
        viewGalleryBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewGalleryClass.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

    }
}

