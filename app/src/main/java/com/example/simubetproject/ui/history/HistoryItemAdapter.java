package com.example.simubetproject.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simubetproject.Bet;
import com.example.simubetproject.R;

import java.util.ArrayList;

public class HistoryItemAdapter extends RecyclerView.Adapter<HistoryItemAdapter.ViewHolder> {

    private ArrayList<Bet> betHistoryList;

    public HistoryItemAdapter(ArrayList<Bet> betHistoryList) {
        this.betHistoryList = betHistoryList;
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

        // Set bet details
        holder.betDetailsTextView.setText("Bets: " + getBetDetails(bet.getBets()));  // List of bet names
        holder.anteAmountTextView.setText("Ante Amount: $" + bet.getAnteAmount());
        holder.possibleWinningTextView.setText("Possible Winning: $" + bet.getPossibleWinning());
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

    private String getBetDetails(ArrayList<String> bets) {
        StringBuilder betDetails = new StringBuilder();
        for (String bet : bets) {
            betDetails.append(bet).append(", ");
        }
        return betDetails.length() > 0 ? betDetails.substring(0, betDetails.length() - 2) : "";
    }
}
