package com.example.simubetproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private OnUserClickListener listener;

    // For user clicks
    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    public UserAdapter(List<User> userList, OnUserClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        User user = userList.get(position);

        String fullName = user.getFirstName() + " " + user.getLastName();

        holder.fullNameTextView.setText(fullName);
        holder.usernameTextView.setText("Username: " + user.getUsername());
        holder.emailTextView.setText("Email: " + user.getEmail());
        holder.typeTextView.setText("Account Type: " + user.getType());
        holder.balanceTextView.setText("Balance: $" + String.format("%.2f", user.getBalance()));

        holder.viewDetailsButton.setOnClickListener(v -> listener.onUserClick(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView fullNameTextView;
        TextView usernameTextView;
        TextView emailTextView;
        TextView typeTextView;
        TextView balanceTextView;
        Button viewDetailsButton;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            balanceTextView = itemView.findViewById(R.id.balanceTextView);
            viewDetailsButton = itemView.findViewById(R.id.viewDetailsButton);
        }
    }
}
