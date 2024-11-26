package com.example.simubetproject.ui.basket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simubetproject.Model;
import com.example.simubetproject.R;

import java.util.List;

public class BasketballAdapter extends RecyclerView.Adapter<BasketballAdapter.BasketballGameViewHolder> {

    private List<Model> basketballGames;

    public BasketballAdapter(List<Model> basketballGames) {
        this.basketballGames = basketballGames;
    }

    @NonNull
    @Override
    public BasketballGameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_item, parent, false);
        return new BasketballGameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketballGameViewHolder holder, int position) {
        Model game = basketballGames.get(position);
        holder.homeTeamTextView.setText(game.getHomeTeam());
        holder.awayTeamTextView.setText(game.getAwayTeam());
        holder.commenceTimeTextView.setText(game.getCommenceTime());
        holder.homeOddsTextView.setText(game.getHomeTeam());
        holder.awayOddsTextView.setText(game.getAwayTeam());
        holder.homeTeamOddsButton.setText(game.getHomeOdds());
        holder.awayTeamOddsButton.setText(game.getAwayOdds());
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

        public BasketballGameViewHolder(@NonNull View itemView) {
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