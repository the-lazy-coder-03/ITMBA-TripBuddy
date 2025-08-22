package com.example.itmba_tripbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginScreen extends AppCompatActivity {

    private Database dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        dbHelper = new Database(this);

        EditText editEmail    = findViewById(R.id.editTextTextEmailAddress);
        // IMPORTANT: XML should define this as textPassword (NOT numberPassword)
        EditText editPassword = findViewById(R.id.editTextPassword);

        Button loginBtn  = findViewById(R.id.LoginBtn);
        Button signupBtn = findViewById(R.id.SignUpbtn);

        loginBtn.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (!validate(email, password)) return;

            if (!dbHelper.checkUser(email, password)) {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                return;
            }

            int userId = dbHelper.getUserId(email, password);
            if (userId <= 0) {
                Toast.makeText(this, "Login error: user not found", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save session + go
            SessionManager.saveUserId(this, userId);

            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginScreen.this, MainActivity.class);
            intent.putExtra("userId", userId); // optional, session already saved
            startActivity(intent);
            finish();
        });

        signupBtn.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (!validate(email, password)) return;

            boolean inserted = dbHelper.insertUser(email, password);
            if (!inserted) {
                Toast.makeText(this, "Account creation failed (email may already exist)", Toast.LENGTH_SHORT).show();
                return;
            }

            int userId = dbHelper.getUserId(email, password);
            if (userId <= 0) {
                Toast.makeText(this, "Account created, but couldn’t fetch user ID", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save session + go
            SessionManager.saveUserId(this, userId);

            Toast.makeText(this, "Account Created! Welcome 🎉", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginScreen.this, MainActivity.class);
            intent.putExtra("userId", userId); // optional
            startActivity(intent);
            finish();
        });
    }

    private boolean validate(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
