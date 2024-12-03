package com.example.simubetproject.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.simubetproject.LoginActivity;
import com.example.simubetproject.R;
import com.google.firebase.auth.FirebaseAuth;

public class AdminPage extends AppCompatActivity {

    Button logoutButton, viewUsersButton, viewBetsButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_page);

        viewUsersButton = findViewById(R.id.viewUsersButton);
        viewUsersButton.setOnClickListener(view -> {
            Intent intent = new Intent(AdminPage.this, AdminViewUsersActivity.class);
            startActivity(intent);
        });

        viewBetsButton = findViewById(R.id.viewBetsButton);
        viewBetsButton.setOnClickListener(view -> {
            Intent intent = new Intent(AdminPage.this, AdminViewBetsActivity.class);
            startActivity(intent);
        });


        mAuth = FirebaseAuth.getInstance();
        logoutButton = findViewById(R.id.logoutAdminButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Toast.makeText(AdminPage.this, "Logged out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminPage.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}