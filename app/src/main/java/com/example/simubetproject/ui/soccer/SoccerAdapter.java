package com.example.simubetproject.ui.soccer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simubetproject.Model;
import com.example.simubetproject.R;
import com.example.simubetproject.ui.basket.BasketballAdapter;

import java.util.List;

public class SoccerAdapter extends RecyclerView.Adapter<SoccerAdapter.SoccerViewHolder> {

    private List<Model> soccerGames;

    public SoccerAdapter(List<Model> soccerGames) {
        this.soccerGames = soccerGames;
    }

    @NonNull
    @Override
    public SoccerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soccer_item, parent, false);
        return new SoccerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoccerViewHolder holder, int position) {
        /*
        * Model game = basketballGames.get(position);
        holder.homeTeamTextView.setText(game.getHomeTeam());
        holder.awayTeamTextView.setText(game.getAwayTeam());
        holder.commenceTimeTextView.setText(game.getCommenceTime());
        holder.homeOddsTextView.setText(game.getHomeOdds());
        holder.awayOddsTextView.setText(game.getAwayOdds());*/

        Model game = soccerGames.get(position);
        holder.timeTextView.setText(game.getCommenceTime());
        holder.homeTeamTextView.setText(game.getHomeTeam());
        holder.awayTeamTextView.setText(game.getAwayTeam());
        holder.homeTeamOddsTextView.setText(game.getHomeTeam());
        holder.awayTeamOddsTextView.setText(game.getAwayTeam());
        holder.homeTeamOddsButton.setText(game.getHomeOdds());
        holder.awayTeamOddsButton.setText(game.getAwayOdds());
        holder.tieOddsButton.setText(game.getTieOdds());
    }

    @Override
    public int getItemCount() {
        return soccerGames.size();
    }

    public static class SoccerViewHolder extends RecyclerView.ViewHolder {

        TextView timeTextView, homeTeamTextView, awayTeamTextView, homeTeamOddsTextView, awayTeamOddsTextView;
        Button homeTeamOddsButton, awayTeamOddsButton, tieOddsButton;

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

        }
    }
}
