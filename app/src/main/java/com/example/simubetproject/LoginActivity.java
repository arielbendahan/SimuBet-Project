// This activity is responsible for the login functionality of the app. It allows users to log in with their email and password.
// It also checks if the user is an admin or a regular user and redirects them to the appropriate page.
package com.example.simubetproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.simubetproject.ui.admin.AdminPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText emailText, passwordText;
    Button loginButton, registerLinkButton;
    FirebaseAuth mAuth;
    DatabaseReference databaseUsers;
    ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        emailText = findViewById(R.id.emailLoginText);
        passwordText = findViewById(R.id.passwordLoginText);
        loginButton = findViewById(R.id.loginButton);
        registerLinkButton = findViewById(R.id.signupLinkButton);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        mAuth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        registerLinkButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
            finish();
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sign in the user with the provided email and password and check if the user is an admin or a regular user
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        loadingIndicator.setVisibility(View.VISIBLE);
                        String userId = user.getUid();
                        databaseUsers.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                loadingIndicator.setVisibility(View.GONE);
                                User user = snapshot.getValue(User.class);
                                if (user != null) {
                                    String userRole = user.getType();
                                    if ("admin".equals(userRole)) {
                                        Intent intent = new Intent(LoginActivity.this, AdminPage.class);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(LoginActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed. Please make sure your credentials are correct!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
// If the user is already logged in, the app will redirect them to the appropriate page based on their role
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            loadingIndicator.setVisibility(View.VISIBLE);
            String userId = currentUser.getUid();
            databaseUsers.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    loadingIndicator.setVisibility(View.GONE);
                    // Get the user object from the database based on the user ID
                    User user = snapshot.getValue(User.class);
                    // Check if the user exists
                    if (user != null) {
                        // Get the user role and redirect to the appropriate page
                        String userRole = user.getType();
                        if ("admin".equals(userRole)) {
                            Intent intent = new Intent(LoginActivity.this, AdminPage.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                // Error handling in case the user data cannot be retrieved
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    loadingIndicator.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}