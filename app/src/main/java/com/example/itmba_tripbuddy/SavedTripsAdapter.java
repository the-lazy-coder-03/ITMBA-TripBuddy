package com.example.itmba_tripbuddy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itmba_tripbuddy.ViewGalleryClass.TripRow;

import java.util.List;

public class SavedTripsAdapter extends RecyclerView.Adapter<SavedTripsAdapter.TripVH> {

    private final List<TripRow> items;

    public SavedTripsAdapter(List<TripRow> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public TripVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_saved_trip, parent, false);
        return new TripVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TripVH h, int position) {
        TripRow t = items.get(position);
        h.tvDestination.setText(t.destination);
        h.tvTripType.setText(t.type);
        h.tvNotes.setText(t.notes == null || t.notes.isEmpty() ? "—" : t.notes);
        h.tvExpenses.setText(t.costDisplay);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class TripVH extends RecyclerView.ViewHolder {
        TextView tvDestination, tvTripType, tvNotes, tvExpenses;

        public TripVH(@NonNull View itemView) {
            super(itemView);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvTripType    = itemView.findViewById(R.id.tvTripType); // <-- updated
            tvNotes       = itemView.findViewById(R.id.tvNotes);
            tvExpenses    = itemView.findViewById(R.id.tvExpenses);
        }
    }
}
