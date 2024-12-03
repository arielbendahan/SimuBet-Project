package com.example.simubetproject.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.simubetproject.Model;
import com.example.simubetproject.ui.hockey.HockeyAdapter;
import com.example.simubetproject.ui.hockey.HockeyFragment;
import com.example.simubetproject.ui.soccer.SoccerAdapter;
import com.example.simubetproject.ui.soccer.SoccerFragment;
import com.example.simubetproject.ui.basket.BasketballAdapter;
import com.example.simubetproject.ui.basket.BasketFragment;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simubetproject.R;

import java.util.ArrayList;
import java.util.List;

public class AdminViewBetsActivity extends AppCompatActivity {

    RecyclerView hockeyRecyclerView, soccerRecyclerView, basketballRecyclerView;
    HockeyAdapter hockeyAdapter;
    SoccerAdapter soccerAdapter;
    BasketballAdapter basketballAdapter;
    Button backToAdminButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_view_bets);

        backToAdminButton = findViewById(R.id.backToAdminButton);

        hockeyRecyclerView = findViewById(R.id.hockeyRecyclerView);
        soccerRecyclerView = findViewById(R.id.soccerRecyclerView);
        basketballRecyclerView = findViewById(R.id.basketballRecyclerView);

        hockeyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        soccerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        basketballRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        HockeyFragment.fetchHockeyData(this, hockeyGames, hockeyAdapter);
//        SoccerFragment.fetchFootballData(this, soccerGames, soccerAdapter);
//        BasketFragment.fetchBasketballData(this, basketballGames, basketballAdapter);

        backToAdminButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(AdminViewBetsActivity.this, AdminPage.class); // Correct the class reference
            backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(backIntent);
            finish();
        });
    }

}