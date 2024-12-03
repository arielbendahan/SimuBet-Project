package com.example.simubetproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    RecyclerView betSlipInfoRecyclerView;
    BetAdapter betAdapter;
    TextView totalAmountTextView, totalOddsMultiplierTextView;
    Button confirmBetButton, cancelBetButton;
    EditText betAmountPlacedEditText;
    ArrayList<Model> selectedBets;
    Double totalOddMultiplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        Intent intent = getIntent();

        selectedBets = intent.getParcelableArrayListExtra("selectedBets");

        confirmBetButton = findViewById(R.id.confirm_bet_checkout_button);
        cancelBetButton = findViewById(R.id.cancel_bet_checkout_button);
        totalAmountTextView = findViewById(R.id.bet_total_checkout_textView);
        betAmountPlacedEditText = findViewById(R.id.bet_amount_checkout_editText);
        totalOddsMultiplierTextView = findViewById(R.id.bet_total_odds_multiplier_textView);

        betSlipInfoRecyclerView = findViewById(R.id.bets_info_recyclerView);
        betSlipInfoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        betAdapter = new BetAdapter(selectedBets);

        betSlipInfoRecyclerView.setAdapter(betAdapter);

        totalOddMultiplier = 1.0;

        for (Model bet : selectedBets) {
            Double oddMultiplier = Double.parseDouble(bet.getSelectedBet());

            totalOddMultiplier *= oddMultiplier;
        }

        totalOddsMultiplierTextView.setText(String.format("Total multiplier: x$%.2f", totalOddMultiplier));
        //check if the user has enough money to place the bet
        //save the bet in his history
        //go back to the home screen
        confirmBetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                double amountBet = Double.parseDouble(betAmountPlacedEditText.getText().toString());

                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            double balance = user.getBalance();
                            if (balance >= amountBet) {
                                double newBalance = balance - amountBet;
                                userRef.child("balance").setValue(newBalance);

                                Bet bet = new Bet(userId, selectedBets, amountBet, totalOddMultiplier, amountBet * totalOddMultiplier);
                                DatabaseReference betRef = FirebaseDatabase.getInstance().getReference("bets").child(userId);
                                String betId = betRef.push().getKey();
                                betRef.child(betId).setValue(bet);

                                Toast.makeText(CheckoutActivity.this, "Bet placed successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(CheckoutActivity.this, "Insufficient balance", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(CheckoutActivity.this, "Failed to place bet or retrieve user balance", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        //destroy the intent
        //go back to home page
        cancelBetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the selected bets
                selectedBets.clear();

                // Cancel the bet and go back to the previous activity
                Intent intent1 = new Intent(CheckoutActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();
            }
        });

        betAmountPlacedEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();

                if (input.isEmpty()) {
                    totalAmountTextView.setText("Total: $0.00");
                    return;
                }

                try {
                    double amountBet = Double.parseDouble(input);
                    //double chosenOdd = Double.parseDouble(chosenOdds);

                    double total = amountBet * totalOddMultiplier;

                    totalAmountTextView.setText(String.format("Total: $%.2f", total));
                }
                catch (NumberFormatException e) {
                    totalAmountTextView.setText("Total: $0.00");
                }
            }
        });
    }
}