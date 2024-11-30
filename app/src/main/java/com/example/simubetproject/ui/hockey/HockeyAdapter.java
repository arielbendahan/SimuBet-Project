package com.example.simubetproject.ui.hockey;

import android.content.Context;
import android.content.Intent;
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
import com.example.simubetproject.ui.hockey.HockeyAdapter;

import java.util.List;


public class HockeyAdapter extends RecyclerView.Adapter<com.example.simubetproject.ui.hockey.HockeyAdapter.HockeyGameViewHolder> {

    private List<Model> hockeyGames;

    public HockeyAdapter(List<Model> hockeyGames) {
        this.hockeyGames = hockeyGames;
    }

    @NonNull
    @Override
    public com.example.simubetproject.ui.hockey.HockeyAdapter.HockeyGameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hockey_item, parent, false);
        return new com.example.simubetproject.ui.hockey.HockeyAdapter.HockeyGameViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HockeyGameViewHolder holder, int position) {
        Model game = hockeyGames.get(position);
        holder.homeTeamTextView.setText(game.getHomeTeam());
        holder.awayTeamTextView.setText(game.getAwayTeam());
        holder.commenceTimeTextView.setText(game.getCommenceTime());
        holder.homeOddsTextView.setText(game.getHomeOdds());
        holder.awayOddsTextView.setText(game.getAwayOdds());
        holder.homeTeamOddsButton.setText("Bet " + game.getHomeOdds());
        holder.awayTeamOddsButton.setText("Bet " + game.getAwayOdds());

        // Set click listeners for buttons
        holder.homeTeamOddsButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CheckoutActivity.class);
            intent.putExtra("team", game.getHomeTeam());
            intent.putExtra("odds", game.getHomeOdds());
            v.getContext().startActivity(intent);
        });

        holder.awayTeamOddsButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CheckoutActivity.class);
            intent.putExtra("team", game.getAwayTeam());
            intent.putExtra("odds", game.getAwayOdds());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return hockeyGames.size();
    }

    public static class HockeyGameViewHolder extends RecyclerView.ViewHolder {
        TextView homeTeamTextView;
        TextView awayTeamTextView;
        TextView commenceTimeTextView;
        TextView homeOddsTextView;
        TextView awayOddsTextView;
        Button homeTeamOddsButton, awayTeamOddsButton;

        public HockeyGameViewHolder(@NonNull View itemView) {
            super(itemView);
            homeTeamTextView = itemView.findViewById(R.id.homeTeamTextView);
            awayTeamTextView = itemView.findViewById(R.id.awayTeamTextView);
            commenceTimeTextView = itemView.findViewById(R.id.commenceTimeTextView);
            homeOddsTextView = itemView.findViewById(R.id.homeTeamOddsTextView);
            awayOddsTextView = itemView.findViewById(R.id.awayTeamOddsTextView);
            homeTeamOddsButton = itemView.findViewById(R.id.homeTeamBetButton);
            awayTeamOddsButton = itemView.findViewById(R.id.awayTeamBetButton);
        }
    }
}