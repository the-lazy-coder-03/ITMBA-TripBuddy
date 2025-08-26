package com.example.itmba_tripbuddy;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class PlanTrip extends AppCompatActivity {

    private Database dbHelper;
    private int userId;

    // Views (IDs taken from your XML)
    private EditText edtDestination, edtNotes, edtCustomExpenses, edtFoodExpenses;
    private TextView tvDisplayCost, tvDiscount, tvFinalCost;
    private Spinner spinnerTripType;
    private Button btnSaveTrip, btnConfirm;

    private static final double DISCOUNT_RATE = 0.10; // 10%

    private final DecimalFormat moneyFmt = new DecimalFormat("#,##0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_save_trip);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        // --- DB + user ---
        dbHelper = new Database(this);
        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "No user logged in. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // --- Bind views ---
        edtDestination    = findViewById(R.id.EdtDestination);
        edtNotes          = findViewById(R.id.EdtNotes);
        edtCustomExpenses = findViewById(R.id.EdtCustomExpenses);
        edtFoodExpenses   = findViewById(R.id.EdtFoodExpenses);
        tvDisplayCost     = findViewById(R.id.DisplayCost);
        tvDiscount        = findViewById(R.id.Discount);
        tvFinalCost       = findViewById(R.id.FinalCost);
        spinnerTripType   = findViewById(R.id.spinnerTripType);
        btnSaveTrip       = findViewById(R.id.SaveTripBtn);
        btnConfirm        = findViewById(R.id.ConfBtn);

        // --- Spinner data ---
        String[] tripTypes = {"Sightseeing", "Hiking", "BeachDay", "Museum Day"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, tripTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTripType.setAdapter(adapter);

        // Calculate preview
        btnSaveTrip.setOnClickListener(v -> calculateAndShowCosts());

        // Confirm save
        btnConfirm.setOnClickListener(v -> saveTripToDb());

        // Back button
        Button backToMain = findViewById(R.id.btnBackToMain);
        backToMain.setOnClickListener(v -> {
            Intent intent = new Intent(PlanTrip.this, MainActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
            finish();
        });
    }

    private void calculateAndShowCosts() {
        double custom   = parseDoubleOrZero(edtCustomExpenses.getText().toString().trim());
        double food     = parseDoubleOrZero(edtFoodExpenses.getText().toString().trim());
        double subtotal = custom + food;

        // Count trips already saved (before this one)
        int existingTrips = dbHelper.getTripCountForUser(userId);
        boolean eligible  = existingTrips >= 3; // discount from 4th trip onward
        double discount   = eligible ? subtotal * DISCOUNT_RATE : 0.0;
        double finalCost  = subtotal - discount;

        tvDisplayCost.setText("Total cost: " + moneyFmt.format(subtotal));
        tvDiscount.setText("Discount: " + moneyFmt.format(discount) + (eligible ? " (loyalty)" : ""));
        tvFinalCost.setText("Final cost: " + moneyFmt.format(finalCost));

        btnConfirm.setVisibility(Button.VISIBLE);
    }

    private void saveTripToDb() {
        String destination = edtDestination.getText().toString().trim();
        String notes       = edtNotes.getText().toString().trim();
        String type        = spinnerTripType.getSelectedItem() != null
                ? spinnerTripType.getSelectedItem().toString()
                : "";

        if (destination.isEmpty() || type.isEmpty()) {
            Toast.makeText(this, "Please enter Destination and select Trip Type.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Recompute using the same logic at save time
        double custom   = parseDoubleOrZero(edtCustomExpenses.getText().toString().trim());
        double food     = parseDoubleOrZero(edtFoodExpenses.getText().toString().trim());
        double subtotal = custom + food;

        int existingTrips = dbHelper.getTripCountForUser(userId); // BEFORE inserting
        boolean eligible  = existingTrips >= 3;
        double discount   = eligible ? subtotal * DISCOUNT_RATE : 0.0;
        double finalCost  = subtotal - discount;

        // Map to DB schema
        String tripName = destination;
        String tripDate = DateGen();
        String tripType = type;
        String dest     = destination;
        String note     = buildNotes(notes, custom, food, discount);
        double cost     = finalCost;
        int counter     = existingTrips + 1; // optional running number

        boolean ok = dbHelper.insertTrip(userId, tripName, tripDate, tripType, dest, note, cost, counter);

        if (ok) {
            Toast.makeText(this, "Trip saved ", Toast.LENGTH_SHORT).show();
            clearForm();
            btnConfirm.setVisibility(Button.INVISIBLE);
        } else {
            Toast.makeText(this, "Failed to save trip.", Toast.LENGTH_SHORT).show();
        }


    }

    private String buildNotes(String userNotes, double custom, double food, double discount) {
        StringBuilder sb = new StringBuilder();
        if (!userNotes.isEmpty()) sb.append(userNotes).append("\n");
        sb.append("Food: ").append(moneyFmt.format(food))
                .append(" | Custom: ").append(moneyFmt.format(custom));
        if (discount > 0) {
            sb.append(" | Discount: ").append(moneyFmt.format(discount));
        }
        return sb.toString();
    }

    private String DateGen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return LocalDate.now().toString();
        } else {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        }
    }

    private double parseDoubleOrZero(String s) {
        if (s == null || s.isEmpty()) return 0.0;
        try { return Double.parseDouble(s); } catch (NumberFormatException e) { return 0.0; }
    }

    private void clearForm() {
        edtDestination.setText("");
        edtNotes.setText("");
        edtCustomExpenses.setText("");
        edtFoodExpenses.setText("");
        tvDisplayCost.setText("");
        tvDiscount.setText("");
        tvFinalCost.setText("");
        if (spinnerTripType.getAdapter() != null && spinnerTripType.getAdapter().getCount() > 0) {
            spinnerTripType.setSelection(0);
        }
    }
}
