package com.example.itmba_tripbuddy;

import android.content.Intent;
import android.os.Bundle;
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
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 🔹 Initialize database
        dbHelper = new Database(this);

        // 🔹 Get UI elements
        EditText editEmail = findViewById(R.id.editTextTextEmailAddress);
        EditText editPassword = findViewById(R.id.editTextNumberPassword);
        Button loginBtn = findViewById(R.id.LoginBtn);
        Button signupBtn = findViewById(R.id.SignUpbtn);

        // 🔹 Handle Login button
        loginBtn.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginScreen.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            } else {
                boolean validUser = dbHelper.checkUser(email, password);
                if (validUser) {
                    Toast.makeText(LoginScreen.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginScreen.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 🔹 Handle Sign Up button (create account + go to main)
        signupBtn.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginScreen.this, "Please enter email and password to sign up", Toast.LENGTH_SHORT).show();
            } else {
                boolean inserted = dbHelper.insertUser(email, password);
                if (inserted) {
                    Toast.makeText(LoginScreen.this, "Account Created! Welcome 🎉", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // 🔹 optional but recommended
                } else {
                    Toast.makeText(LoginScreen.this, "Account creation failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
