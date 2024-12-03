package com.example.simubetproject.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.simubetproject.Model;
import com.example.simubetproject.R;
import com.example.simubetproject.ui.hockey.HockeyAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AdminViewBetsActivity extends AppCompatActivity {

    private RecyclerView hockeyRecyclerView, soccerRecyclerView, basketballRecyclerView;
    private HockeyAdapter hockeyAdapter, soccerAdapter, basketballAdapter;
    private List<Model> hockeyGames, soccerGames, basketballGames;
    private RequestQueue requestQueue;
    Button backToAdminButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_bets);

        // Initialize RecyclerViews and Adapters
        hockeyRecyclerView = findViewById(R.id.hockeyRecyclerView);
        soccerRecyclerView = findViewById(R.id.soccerRecyclerView);
        basketballRecyclerView = findViewById(R.id.basketballRecyclerView);

        backToAdminButton = findViewById(R.id.backToAdminButton);

        hockeyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        soccerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        basketballRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        hockeyGames = new ArrayList<>();
        soccerGames = new ArrayList<>();
        basketballGames = new ArrayList<>();

        hockeyAdapter = new HockeyAdapter(hockeyGames, new ArrayList<>(), bet -> {});
        soccerAdapter = new HockeyAdapter(soccerGames, new ArrayList<>(), bet -> {});
        basketballAdapter = new HockeyAdapter(basketballGames, new ArrayList<>(), bet -> {});

        hockeyRecyclerView.setAdapter(hockeyAdapter);
        soccerRecyclerView.setAdapter(soccerAdapter);
        basketballRecyclerView.setAdapter(basketballAdapter);

        // Initialize Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Fetch data
        fetchHockeyData();
        fetchSoccerData();
        fetchBasketballData();

        backToAdminButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(AdminViewBetsActivity.this, AdminPage.class); // Correct the class reference
            backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(backIntent);
            finish();
        });
    }

    private void fetchHockeyData() {
        String url = "https://api.the-odds-api.com/v4/sports/icehockey/odds/?regions=us&markets=h2h&bookmakers=draftkings&apiKey=9503f40c5935e671ce94f11f2800d9bd";
        fetchData(url, hockeyGames, hockeyAdapter);
    }

    private void fetchSoccerData() {
        String url = "https://api.the-odds-api.com/v4/sports/soccer/odds/?regions=us&markets=h2h&bookmakers=draftkings&apiKey=9503f40c5935e671ce94f11f2800d9bd";
        fetchData(url, soccerGames, soccerAdapter);
    }

    private void fetchBasketballData() {
        String url = "https://api.the-odds-api.com/v4/sports/basketball/odds/?regions=us&markets=h2h&bookmakers=draftkings&apiKey=9503f40c5935e671ce94f11f2800d9bd";
        fetchData(url, basketballGames, basketballAdapter);
    }

    private void fetchData(String url, List<Model> gameList, HockeyAdapter adapter) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        gameList.clear();
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                        SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault());

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject game = response.getJSONObject(i);
                            String homeTeam = game.optString("home_team", "Unknown");
                            String awayTeam = game.optString("away_team", "Unknown");
                            String commenceTime = game.optString("commence_time", "");

                            String formattedDate = commenceTime;
                            if (!commenceTime.isEmpty()) {
                                try {
                                    Date date = inputFormat.parse(commenceTime);
                                    formattedDate = outputFormat.format(date);
                                } catch (ParseException e) {
                                    Log.e("Date Parsing", "Error parsing date: " + commenceTime);
                                }
                            }

                            JSONArray bookmakers = game.optJSONArray("bookmakers");
                            if (bookmakers != null && bookmakers.length() > 0) {
                                JSONObject draftKings = bookmakers.getJSONObject(0);
                                JSONArray markets = draftKings.optJSONArray("markets");

                                if (markets != null && markets.length() > 0) {
                                    JSONArray outcomes = markets.getJSONObject(0).optJSONArray("outcomes");

                                    String homeOdds = "N/A";
                                    String awayOdds = "N/A";

                                    for (int j = 0; j < outcomes.length(); j++) {
                                        JSONObject outcome = outcomes.getJSONObject(j);
                                        String name = outcome.optString("name");
                                        String price = outcome.optString("price", "N/A");

                                        if (name.equals(homeTeam)) {
                                            homeOdds = price;
                                        } else if (name.equals(awayTeam)) {
                                            awayOdds = price;
                                        }
                                    }

                                    gameList.add(new Model(homeTeam, awayTeam, formattedDate, homeOdds, awayOdds));
                                }
                            }
                        }

                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e("JSON Parsing", "Error parsing JSON: " + e.getMessage());
                    }
                },
                error -> Log.e("Volley Error", "Error fetching data: " + error.getMessage())
        );

        requestQueue.add(jsonArrayRequest);
    }
}
