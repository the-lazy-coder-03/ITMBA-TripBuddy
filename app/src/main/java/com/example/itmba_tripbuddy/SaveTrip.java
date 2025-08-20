package com.example.itmba_tripbuddy;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.itmba_tripbuddy.R;

public class SaveTrip extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Edge-to-edge support
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_save_trip);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Reference the spinner
        Spinner spinner = findViewById(R.id.spinnerTripType);

        // Step 1: Create spinner data
        String[] tripTypes = {"Sightseeing", "Hiking", "BeachDay", "Museum Day"};

        // Step 2: Create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tripTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Step 3: Attach adapter to spinner
        spinner.setAdapter(adapter);

        // Step 4: Handle selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(SaveTrip.this, "Selected: " + item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: Handle no selection
            }
        });
    }
}
