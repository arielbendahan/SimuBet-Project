package com.example.simubetproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetailsActivity extends AppCompatActivity {

    TextView userFullNameTextView, userEmailTextView, userTypeTextView, userBalanceTextView;
    TextView betAmountTextView, totalOddsTextView, potentialWinTextView; // New TextViews
    Button activateAccountButton, deactivateAccountButton, backToUsersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_details);

        userFullNameTextView = findViewById(R.id.userFullNameTextView);
        userEmailTextView = findViewById(R.id.userEmailTextView);
        userTypeTextView = findViewById(R.id.userTypeTextView);
        userBalanceTextView = findViewById(R.id.userBalanceTextView);
        activateAccountButton = findViewById(R.id.activateAccountButton);
        deactivateAccountButton = findViewById(R.id.deactivateAccountButton);
        backToUsersButton = findViewById(R.id.backToUsersButton);

        betAmountTextView = findViewById(R.id.betAmountTextView);
        totalOddsTextView = findViewById(R.id.totalOddsTextView);
        potentialWinTextView = findViewById(R.id.potentialWinTextView);

        String userId, fullName, email, type;
        double balance;

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        fullName = intent.getStringExtra("fullName");
        email = intent.getStringExtra("email");
        type = intent.getStringExtra("type");
        balance = intent.getDoubleExtra("balance", 0.0);

        userFullNameTextView.setText("Full Name: " + fullName);
        userEmailTextView.setText("Email: " + email);
        userTypeTextView.setText("Account Type: " + type);
        userBalanceTextView.setText("Balance: $" + String.format("%.2f", balance));

        fetchUserBets(userId);

        backToUsersButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(UserDetailsActivity.this, AdminViewUsersActivity.class);
            backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(backIntent);
            finish();
        });
    }

    private void fetchUserBets(String userId) {
        DatabaseReference betsRef = FirebaseDatabase.getInstance().getReference("bets").child(userId);

        // Fetch bets for this user
        betsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    StringBuilder betStringBuilder = new StringBuilder("Bets:\n\n");
                    boolean hasValidBets = false;

                    for (DataSnapshot betSnapshot : snapshot.getChildren()) {
                        Bet bet = betSnapshot.getValue(Bet.class);

                        // Ensure that bet is properly loaded and has valid values
                        if (bet != null && bet.getAmountBet() > 0) {
                            hasValidBets = true;

                            // Set the values to individual TextViews
                            betAmountTextView.setText("Bet Amount: $" + String.format("%.2f", bet.getAmountBet()));
                            totalOddsTextView.setText("Total Odds: " + String.format("%.2f", bet.getTotalOddMultiplier()));
                            potentialWinTextView.setText("Potential Win: $" + String.format("%.2f", bet.getTotalAmountWon()));

                            // Optionally append to the bet string for showing all bets in a single view
                            betStringBuilder.append("Bet Amount: $")
                                    .append(String.format("%.2f", bet.getAmountBet())).append("\n");

                            betStringBuilder.append("Total Odds: ")
                                    .append(String.format("%.2f", bet.getTotalOddMultiplier())).append("\n");

                            betStringBuilder.append("Potential Win: $")
                                    .append(String.format("%.2f", bet.getTotalAmountWon())).append("\n");

                            betStringBuilder.append("------------\n"); // Separator between bets
                        }
                    }

                    if (hasValidBets) {
                        // If you want to show a summary of all bets in a separate area, you can also use another TextView.
                    } else {
                        betAmountTextView.setText("This user is not part of any bets yet.");
                        totalOddsTextView.setText("");
                        potentialWinTextView.setText("");
                    }
                } else {
                    betAmountTextView.setText("This user is not part of any bets yet.");
                    totalOddsTextView.setText("");
                    potentialWinTextView.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                betAmountTextView.setText("Failed to load bets: " + error.getMessage());
            }
        });
    }
}
