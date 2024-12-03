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
        holder.anteAmountTextView.setText("Ante Amount: $" + bet.getAmountBet());
        holder.possibleWinningTextView.setText("Possible Winning: $" + bet.getTotalAmountWon());
        holder.betResultTextView.setText("Result: " + bet.getResult());
    }

    @Override
    public int getItemCount() {
        return betHistoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView betDetailsTextView, anteAmountTextView, possibleWinningTextView, betResultTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            betDetailsTextView = itemView.findViewById(R.id.bet_details);
            anteAmountTextView = itemView.findViewById(R.id.ante_amount);
            possibleWinningTextView = itemView.findViewById(R.id.possible_winning);
            betResultTextView = itemView.findViewById(R.id.bet_result);
        }
    }

    // Helper method to format bet details
    private String getBetDetails(ArrayList<Model> selectedBets) {
        if (selectedBets == null || selectedBets.isEmpty()) {
            return "No bets placed";
        }
        StringBuilder betDetails = new StringBuilder();
        for (Model model : selectedBets) {
            betDetails.append(model.toString()).append(",\n"); // Assuming Model has a meaningful toString()
        }
        return betDetails.length() > 0 ? betDetails.substring(0, betDetails.length() - 2) : "";
    }
}
