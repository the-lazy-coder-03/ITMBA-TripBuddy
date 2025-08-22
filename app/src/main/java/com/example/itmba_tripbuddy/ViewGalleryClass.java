package com.example.itmba_tripbuddy;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ViewGalleryClass extends AppCompatActivity {

    private int userId;
    private Database dbHelper;

    private RecyclerView recyclerTrips;
    private TextView emptyView;

    private final DecimalFormat moneyFmt = new DecimalFormat("#,##0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_saved_trips);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1) Resolve userId (Intent → Session fallback)
        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) userId = SessionManager.getUserId(this);
        if (userId == -1) {
            Toast.makeText(this, "No user logged in. Please log in again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginScreen.class));
            finish();
            return;
        }

        // 2) Setup DB + views
        dbHelper = new Database(this);
        recyclerTrips = findViewById(R.id.recyclerTrips);
        emptyView     = findViewById(R.id.emptyView);
        recyclerTrips.setLayoutManager(new LinearLayoutManager(this));

        // 3) Load & bind data
        List<TripRow> trips = loadTripsForUser(userId);
        emptyView.setVisibility(trips.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
        recyclerTrips.setAdapter(new com.example.itmba_tripbuddy.SavedTripsAdapter(trips));

        // 4) Back button → MainActivity (keep userId)
        Button backToMain = findViewById(R.id.btnBackToMain);
        backToMain.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class).putExtra("userId", userId));
            finish();
        });
    }

    private List<TripRow> loadTripsForUser(int userId) {
        List<TripRow> list = new ArrayList<>();
        Cursor c = null;
        try {
            c = dbHelper.getTripsForUser(userId);
            if (c != null && c.moveToFirst()) {
                int idxDestination = c.getColumnIndexOrThrow("Destination");
                int idxNotes       = c.getColumnIndexOrThrow("Notes");
                int idxCost        = c.getColumnIndexOrThrow("TravelCost");
                int idxType        = c.getColumnIndexOrThrow("tripType");

                do {
                    String destination = c.getString(idxDestination);
                    String notes       = c.getString(idxNotes);
                    double cost        = c.getDouble(idxCost);
                    String type        = c.getString(idxType);

                    list.add(new TripRow(destination, notes, "R " + moneyFmt.format(cost), type));
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load trips.", Toast.LENGTH_SHORT).show();
        } finally {
            if (c != null) c.close();
        }
        return list;
    }

    // Simple row model
    public static class TripRow {
        public final String destination;
        public final String notes;
        public final String costDisplay;
        public final String type;

        public TripRow(String destination, String notes, String costDisplay, String type) {
            this.destination = destination;
            this.notes = notes;
            this.costDisplay = costDisplay;
            this.type = type;
        }
    }
}
