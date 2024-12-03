package com.example.simubetproject.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simubetproject.Bet;
import com.example.simubetproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private RecyclerView historyRecyclerView;
    private HistoryItemAdapter historyItemAdapter;
    private ArrayList<Bet> betHistoryList;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // Initialize components
        betHistoryList = new ArrayList<>();
        historyRecyclerView = view.findViewById(R.id.history_recycler_view);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Setup adapter
        historyItemAdapter = new HistoryItemAdapter(betHistoryList);
        historyRecyclerView.setAdapter(historyItemAdapter);

        // Fetch data from Firebase
        fetchBetHistory();

        return view;
    }

    private void fetchBetHistory() {
        // Ensure user is logged in
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference betRef = FirebaseDatabase.getInstance().getReference("bets").child(userId);

        betRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                betHistoryList.clear(); // Clear the list before adding new data
                if (snapshot.exists()) {
                    for (DataSnapshot betSnapshot : snapshot.getChildren()) {
                        Bet bet = betSnapshot.getValue(Bet.class);
                        if (bet != null) {
                            betHistoryList.add(bet);
                        }
                    }
                }
                if (betHistoryList.isEmpty()) {
                    Toast.makeText(getContext(), "No bet history found", Toast.LENGTH_SHORT).show();
                }
                historyItemAdapter.notifyDataSetChanged(); // Refresh the RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error loading data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
