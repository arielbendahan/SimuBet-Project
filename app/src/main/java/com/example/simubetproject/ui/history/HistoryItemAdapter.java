package com.example.simubetproject.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simubetproject.Bet;
import com.example.simubetproject.Model;
import com.example.simubetproject.R;

import java.util.ArrayList;

public class HistoryItemAdapter extends RecyclerView.Adapter<HistoryItemAdapter.ViewHolder> {

    private ArrayList<Bet> betHistoryList;

    public HistoryItemAdapter(ArrayList<Bet> betHistoryList) {
        this.betHistoryList = betHistoryList != null ? betHistoryList : new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bet bet = betHistoryList.get(position);

        // Bind data to the views
        holder.betDetailsTextView.setText("Bets: " + getBetDetails(bet.getSelectedBets()));
        holder.selectedTeamTextView.setText("Selected Team(s): " + getSelectedTeams(bet.getSelectedBets()));
        holder.anteAmountTextView.setText("Ante Amount: $" + bet.getAmountBet());
        holder.possibleWinningTextView.setText("Possible Winnings: $" + String.format("%.2f",bet.getTotalAmountWon()));
        holder.betResultTextView.setText("Result: " + bet.getResult());
    }

    @Override
    public int getItemCount() {
        return betHistoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView betDetailsTextView, anteAmountTextView, possibleWinningTextView, betResultTextView, selectedTeamTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            betDetailsTextView = itemView.findViewById(R.id.bet_details);
            anteAmountTextView = itemView.findViewById(R.id.ante_amount);
            possibleWinningTextView = itemView.findViewById(R.id.possible_winning);
            betResultTextView = itemView.findViewById(R.id.bet_result);
            selectedTeamTextView = itemView.findViewById(R.id.selected_teams);

        }
    }

    // Helper method to format bet details
    private String getBetDetails(ArrayList<Model> selectedBets) {
        // Fall back to a default message if no bets are placed
        if (selectedBets == null || selectedBets.isEmpty()) {
            return "No bets placed";
        }
        StringBuilder betDetails = new StringBuilder();
        for (Model model : selectedBets) {
            betDetails.append(model.toString()).append(",\n"); // Assuming Model has a meaningful toString()
        }
        return betDetails.length() > 0 ? betDetails.substring(0, betDetails.length() - 2) : "";
    }

    // Helper method to get selected teams
    private String getSelectedTeams(ArrayList<Model> selectedBets) {
        // Fall back to a default message if no teams are selected
        if (selectedBets == null || selectedBets.isEmpty()) {
            return "No teams selected";
        }

        // Build a comma-separated list of selected teams
        StringBuilder selectedTeams = new StringBuilder();

        // Loop through each bet and append the selected team to the list
        for (Model model : selectedBets) {
            selectedTeams.append(model.getSelectedTeam()).append(", ");
        }
        return selectedTeams.length() > 0 ? selectedTeams.substring(0, selectedTeams.length() - 2) : "";
    }
}
