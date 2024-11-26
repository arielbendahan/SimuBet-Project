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

public class CheckoutActivity extends AppCompatActivity {

    TextView betTimeTextView, betHomeTeamTextView, betAwayTeamTextView, chosenTeamTextView, chosenOddsTextView, totalAmountTextView;
    Button confirmBetButton, cancelBetButton;
    EditText betAmountPlacedEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        Intent intent = getIntent();

        String betTime = intent.getStringExtra("commenceTime");
        String betHomeTeam = intent.getStringExtra("homeTeam");
        String betAwayTeam = intent.getStringExtra("awayTeam");
        String chosenTeam = intent.getStringExtra("chosenTeam");
        String chosenOdds = intent.getStringExtra("chosenOdds");

        betTimeTextView = findViewById(R.id.bet_time_checkout_textView);
        betHomeTeamTextView = findViewById(R.id.bet_home_team_checkout_textView);
        betAwayTeamTextView = findViewById(R.id.bet_away_team_checkout_textView);
        chosenTeamTextView = findViewById(R.id.bet_chosen_team_checkout_textView);
        chosenOddsTextView = findViewById(R.id.bet_chosen_team_odds_checkout_textView);
        confirmBetButton = findViewById(R.id.confirm_bet_checkout_button);
        cancelBetButton = findViewById(R.id.cancel_bet_checkout_button);
        totalAmountTextView = findViewById(R.id.bet_total_checkout_textView);
        betAmountPlacedEditText = findViewById(R.id.bet_amount_checkout_editText);

        betTimeTextView.setText(betTime);
        betHomeTeamTextView.setText(betHomeTeam);
        betAwayTeamTextView.setText(betAwayTeam);
        chosenTeamTextView.setText(chosenTeam);
        chosenOddsTextView.setText(chosenOdds);

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
                    double chosenOdd = Double.parseDouble(chosenOdds);

                    double total = amountBet * chosenOdd;

                    totalAmountTextView.setText(String.format("Total: $%.2f", total));
                }
                catch (NumberFormatException e) {
                    totalAmountTextView.setText("Total: $0.00");
                }
            }
        });
    }
}