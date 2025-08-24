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

public class CreateMemory extends AppCompatActivity {

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_memory);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get userId from Intent or session (fallback)
        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) userId = SessionManager.getUserId(this);

        if (userId == -1) {
            Toast.makeText(this, "No user logged in. Please log in again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginScreen.class));
            finish();
            return;
        }

        Button backToMain = findViewById(R.id.btnBackToMain2);
        backToMain.setOnClickListener(v -> {
            Intent intent = new Intent(CreateMemory.this, MainActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
            finish();
        });
    }
}
