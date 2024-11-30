package com.example.simubetproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simubetproject.ui.soccer.SoccerAdapter;

import java.util.ArrayList;
import java.util.List;

//This class is for displaying the selected bets on the checkout. It is not an universal adapter
public class BetAdapter extends RecyclerView.Adapter<BetAdapter.BetViewHolder>{

    private List<Model> bets;

    BetAdapter(List<Model> bets) {
        this.bets = bets;
    }

    @NonNull
    @Override
    public BetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bet_info_item, parent, false);
        return new BetAdapter.BetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BetViewHolder holder, int position) {
        Model bet = bets.get(position);

        holder.betTimeTextView.setText(bet.getCommenceTime());
        holder.betHomeTeamTextView.setText(bet.getHomeTeam());
        holder.betAwayTeamTextView.setText(bet.getAwayTeam());
        holder.chosenTeamTextView.setText(bet.getSelectedTeam());
        holder.chosenOddsTextView.setText(bet.getSelectedBet());
    }

    @Override
    public int getItemCount() {
        return bets.size();
    }


    public static class BetViewHolder extends RecyclerView.ViewHolder {
        TextView betTimeTextView, betHomeTeamTextView, betAwayTeamTextView, chosenTeamTextView, chosenOddsTextView;
        public BetViewHolder(@NonNull View itemView) {
            super(itemView);

            betTimeTextView = itemView.findViewById(R.id.bet_time_checkout_textView);
            betHomeTeamTextView = itemView.findViewById(R.id.bet_home_team_checkout_textView);
            betAwayTeamTextView = itemView.findViewById(R.id.bet_away_team_checkout_textView);
            chosenTeamTextView = itemView.findViewById(R.id.bet_chosen_team_checkout_textView);
            chosenOddsTextView = itemView.findViewById(R.id.bet_chosen_team_odds_checkout_textView);
        }
    }
}
