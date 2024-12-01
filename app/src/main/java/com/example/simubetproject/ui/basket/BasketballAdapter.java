package com.example.simubetproject.ui.basket;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simubetproject.Model;
import com.example.simubetproject.R;
import com.example.simubetproject.betValidationListener;
import com.example.simubetproject.ui.soccer.SoccerAdapter;

import java.util.ArrayList;
import java.util.List;

public class BasketballAdapter extends RecyclerView.Adapter<BasketballAdapter.BasketballGameViewHolder> {

    private List<Model> basketballGames;

    private ArrayList<Model> selectedBets;

    //Validating bets
    private betValidationListener listener;


    public BasketballAdapter(List<Model> basketballGames, ArrayList<Model> selectedBets, betValidationListener listener) {
        this.basketballGames = basketballGames;
        this.selectedBets = selectedBets;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BasketballGameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_item, parent, false);
        return new BasketballGameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketballGameViewHolder holder, int position) {
        //For adding to the selectedList
        Model homeWinBet, awayWinBet;

        Model game = basketballGames.get(position);
        holder.homeTeamTextView.setText(game.getHomeTeam());
        holder.awayTeamTextView.setText(game.getAwayTeam());
        holder.commenceTimeTextView.setText(game.getCommenceTime());
        holder.homeOddsTextView.setText(game.getHomeTeam());
        holder.awayOddsTextView.setText(game.getAwayTeam());
        holder.homeTeamOddsButton.setText(game.getHomeOdds());
        holder.awayTeamOddsButton.setText(game.getAwayOdds());

        homeWinBet = game;
        awayWinBet = game;

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
    }

    @Override
    public int getItemCount() {
        return basketballGames.size();
    }

    public static class BasketballGameViewHolder extends RecyclerView.ViewHolder {
        TextView homeTeamTextView;
        TextView awayTeamTextView;
        TextView commenceTimeTextView;
        TextView homeOddsTextView;
        TextView awayOddsTextView;
        Button homeTeamOddsButton, awayTeamOddsButton;
        int oddsButtonColor;

        public BasketballGameViewHolder(@NonNull View itemView) {
            super(itemView);
            homeTeamTextView = itemView.findViewById(R.id.homeTeamTextView);
            awayTeamTextView = itemView.findViewById(R.id.awayTeamTextView);
            commenceTimeTextView = itemView.findViewById(R.id.commenceTimeTextView);
            homeOddsTextView = itemView.findViewById(R.id.homeTeamOddsTextView);
            awayOddsTextView = itemView.findViewById(R.id.awayTeamOddsTextView);
            homeTeamOddsButton = itemView.findViewById(R.id.homeTeamBetButton);
            awayTeamOddsButton = itemView.findViewById(R.id.awayTeamBetButton);
            oddsButtonColor = itemView.getResources().getColor(R.color.oddsButtonColor);
        }
    }
}