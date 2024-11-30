package com.example.simubetproject.ui.soccer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simubetproject.CheckoutActivity;
import com.example.simubetproject.Model;
import com.example.simubetproject.R;
import com.example.simubetproject.ui.basket.BasketballAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoccerAdapter extends RecyclerView.Adapter<SoccerAdapter.SoccerViewHolder> {

    private List<Model> soccerGames;
    //bets chosen by the user, later sent to the checkout
    private ArrayList<Model> selectedBets;
    private Context context;

    //Validating bets
    private betsStateChangeListener listener;
    interface betsStateChangeListener {
        void onBetStateChange(Model bet);
    }

    public SoccerAdapter(List<Model> soccerGames, ArrayList<Model> selectedBets, Context context, betsStateChangeListener listener) {
        this.soccerGames = soccerGames;
        this.selectedBets = selectedBets;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SoccerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.soccer_item, parent, false);
        return new SoccerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoccerViewHolder holder, int position) {
        //For adding to the selectedList
        Model homeWinBet, awayWinBet, tieBet;

        Model game = soccerGames.get(position);
        holder.timeTextView.setText(game.getCommenceTime());
        holder.homeTeamTextView.setText(game.getHomeTeam());
        holder.awayTeamTextView.setText(game.getAwayTeam());
        holder.homeTeamOddsTextView.setText(game.getHomeTeam());
        holder.awayTeamOddsTextView.setText(game.getAwayTeam());
        holder.homeTeamOddsButton.setText(game.getHomeOdds());
        holder.awayTeamOddsButton.setText(game.getAwayOdds());
        holder.tieOddsButton.setText(game.getTieOdds());

        homeWinBet = game;
        awayWinBet = game;
        tieBet = game;

        int originalOddButtonColor = holder.oddsButtonColor;

        holder.homeTeamOddsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game.isHomeIsPressed()) {
                    //remove the bet from the list
                    selectedBets.remove(homeWinBet);
                    //change color back to normal
                    holder.homeTeamOddsButton.setBackgroundColor(originalOddButtonColor);
                    //set homeIsPressed to false
                    game.setHomeIsPressed(false);
                    game.decreaseAmountOddsButtonsPressed();
                }
                else {
                    //add the bets to the list
                    //set the selected bet
                    homeWinBet.setSelectedBet(holder.homeTeamOddsButton.getText().toString());
                    homeWinBet.setSelectedTeam(holder.homeTeamTextView.getText().toString());
                    //add it to the arraylist
                    selectedBets.add(homeWinBet);
                    //change the color
                    holder.homeTeamOddsButton.setBackgroundColor(Color.GREEN);
                    //set homeIsPressed to true
                    game.setHomeIsPressed(true);
                    game.increaseAmountOddsButtonsPressed();
                }

                //notify the valid bet slip validation
                listener.onBetStateChange(game);
            }
        });

        holder.awayTeamOddsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game.isAwayIsPressed()) {
                    //remove the bet from the list
                    selectedBets.remove(awayWinBet);
                    //change color back to normal
                    holder.awayTeamOddsButton.setBackgroundColor(originalOddButtonColor);
                    //set homeIsPressed to false
                    game.setAwayIsPressed(false);
                    game.decreaseAmountOddsButtonsPressed();
                }
                else {
                    //add the bets to the list
                    //set the selected bet
                    awayWinBet.setSelectedBet(holder.awayTeamOddsButton.getText().toString());
                    awayWinBet.setSelectedTeam(holder.awayTeamTextView.getText().toString());
                    //add it to the arraylist
                    selectedBets.add(awayWinBet);
                    //change the color
                    holder.awayTeamOddsButton.setBackgroundColor(Color.GREEN);
                    //set homeIsPressed to true
                    game.setAwayIsPressed(true);
                    game.increaseAmountOddsButtonsPressed();
                }

                //notify the valid bet slip validation
                listener.onBetStateChange(game);
            }
        });

        holder.tieOddsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game.isTieIsPressed()) {
                    //remove the bet from the list
                    selectedBets.remove(tieBet);
                    //change color back to normal
                    holder.tieOddsButton.setBackgroundColor(originalOddButtonColor);
                    //set homeIsPressed to false
                    game.setTieIsPressed(false);
                    game.decreaseAmountOddsButtonsPressed();
                }
                else {
                    //add the bets to the list
                    //set the selected bet
                    tieBet.setSelectedBet(holder.tieOddsButton.getText().toString());
                    tieBet.setSelectedTeam("Tie");
                    //add it to the arraylist
                    selectedBets.add(tieBet);
                    //change the color
                    holder.tieOddsButton.setBackgroundColor(Color.GREEN);
                    //set homeIsPressed to true
                    game.setTieIsPressed(true);
                    game.increaseAmountOddsButtonsPressed();
                }

                //notify the valid bet slip validation
                listener.onBetStateChange(game);
            }
        });
    }

    @Override
    public int getItemCount() {
        return soccerGames.size();
    }

    public static class SoccerViewHolder extends RecyclerView.ViewHolder {

        TextView timeTextView, homeTeamTextView, awayTeamTextView, homeTeamOddsTextView, awayTeamOddsTextView;
        Button homeTeamOddsButton, awayTeamOddsButton, tieOddsButton;
        int oddsButtonColor;

        public SoccerViewHolder(@NonNull View itemView) {
            super(itemView);

            timeTextView = itemView.findViewById(R.id.soccer_item_time_textView);
            homeTeamTextView = itemView.findViewById(R.id.soccer_item_home_team_textView);
            awayTeamTextView = itemView.findViewById(R.id.soccer_item_away_team_textView);
            homeTeamOddsTextView = itemView.findViewById(R.id.soccer_item_home_team_bet_textView);
            awayTeamOddsTextView = itemView.findViewById(R.id.soccer_item_away_team_bet_textView);
            homeTeamOddsButton = itemView.findViewById(R.id.soccer_item_home_team_bet_button);
            awayTeamOddsButton = itemView.findViewById(R.id.soccer_item_away_team_bet_button);
            tieOddsButton = itemView.findViewById(R.id.soccer_item_tie_bet_button);
            oddsButtonColor = itemView.getResources().getColor(R.color.oddsButtonColor);
        }
    }
}
