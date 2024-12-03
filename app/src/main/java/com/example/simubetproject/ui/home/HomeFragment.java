package com.example.simubetproject.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.simubetproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseUsers;
    private String username = "User"; // Default username
    private double userBalance = 0.0; // Default balance

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        // Retrieve the currently signed-in user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            fetchUserData(userId, rootView);
        } else {
            Toast.makeText(getContext(), "User not logged in!", Toast.LENGTH_SHORT).show();
        }

        // Set up other views
        Button btnLogout = rootView.findViewById(R.id.logout_button);

        // Handle Logout
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
            // Navigate back to LoginActivity (or any other action)
            requireActivity().finish();
        });

        // Add other button click listeners if needed

        return rootView;
    }

    private void fetchUserData(String userId, View rootView) {
        // Fetch username and balance from Firebase Database
        DatabaseReference userRef = databaseUsers.child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Fetch username and balance
                    username = snapshot.child("username").getValue(String.class);
                    userBalance = snapshot.child("balance").getValue(Double.class);

                    updateUI(rootView);
                } else {
                    Log.e("HomeFragment", "User data not found in database!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("HomeFragment", "Database error: " + error.getMessage());
            }
        });
    }

    private void updateUI(View rootView) {
        // Update the welcome message
        TextView welcomeText = rootView.findViewById(R.id.welcome);
        String welcomeMessage = "Welcome to SimuBet, " + username;
        welcomeText.setText(welcomeMessage);

        // Update the balance
        TextView balanceTextView = rootView.findViewById(R.id.balance_text_view);
        String balanceMessage = "Balance: $" + String.format("%.2f", userBalance);
        balanceTextView.setText(balanceMessage);
    }
}
