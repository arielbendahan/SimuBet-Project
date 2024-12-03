package com.example.simubetproject.ui.history;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simubetproject.Bet;
import com.example.simubetproject.R;
import com.example.simubetproject.ui.history.HistoryItemAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView historyRecyclerView;
    HistoryItemAdapter historyItemAdapter;
    ArrayList<Bet> betHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_history);

        historyRecyclerView = findViewById(R.id.history_recycler_view);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        betHistoryList = new ArrayList<>();
        historyItemAdapter = new HistoryItemAdapter(betHistoryList);
        historyRecyclerView.setAdapter(historyItemAdapter);

        // Retrieve user ID and fetch bets from Firebase
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference betRef = FirebaseDatabase.getInstance().getReference("bets").child(userId);

        // Retrieve the user's past bets
        betRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                betHistoryList.clear();
                for (DataSnapshot betSnapshot : snapshot.getChildren()) {
                    Bet bet = betSnapshot.getValue(Bet.class);
                    if (bet != null) {
                        betHistoryList.add(bet);
                    }
                }
                historyItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HistoryActivity.this, "Failed to load bet history", Toast.LENGTH_SHORT).show();
            }
        });
    }
}