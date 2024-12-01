package com.example.simubetproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        confirmBetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if the user has enough money to place the bet
                //save the bet in his history
                //go back to the home screen
            }
        });

        cancelBetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //destroy the intent
                //go back to home page
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