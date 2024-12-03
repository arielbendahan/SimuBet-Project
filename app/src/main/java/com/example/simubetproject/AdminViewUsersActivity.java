package com.example.simubetproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminViewUsersActivity extends AppCompatActivity {

    RecyclerView usersRecyclerView;
    SearchView userSearchView;
    UserAdapter userAdapter;
    List<User> userList = new ArrayList<>();
    DatabaseReference usersRef;
    Button backToAdminButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_view_users);

        backToAdminButton = findViewById(R.id.backToAdminButton);

        userSearchView = findViewById(R.id.userSearchView);
        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        usersRef = FirebaseDatabase.getInstance().getReference("users");

        fetchUsers();

        userSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterUsers(newText);
                return false;
            }
        });

        backToAdminButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(AdminViewUsersActivity.this, AdminPage.class); // Correct the class reference
            backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(backIntent);
            finish();
        });
    }

    private void fetchUsers() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        user.setUserId(userSnapshot.getKey());
                        userList.add(user);
                    }
                }
                userAdapter = new UserAdapter(userList, user -> openUserDetails(user));
                usersRecyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminViewUsersActivity.this, "Failed to load users: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterUsers(String query) {
        List<User> filteredList = new ArrayList<>();
        for (User user : userList) {
            if (user.getFirstName().toLowerCase().contains(query.toLowerCase()) ||
                    user.getLastName().toLowerCase().contains(query.toLowerCase()) ||
                    user.getUsername().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(user);
            }
        }
        userAdapter = new UserAdapter(filteredList, user -> openUserDetails(user));
        usersRecyclerView.setAdapter(userAdapter);
    }

    private void openUserDetails(User user) {
        Intent intent = new Intent(AdminViewUsersActivity.this, UserDetailsActivity.class);
        intent.putExtra("userId", user.getUserId());
        intent.putExtra("fullName", user.getFirstName() + " " + user.getLastName());
        intent.putExtra("email", user.getEmail());
        intent.putExtra("type", user.getType());
        intent.putExtra("balance", user.getBalance());
        startActivity(intent);
    }
}
