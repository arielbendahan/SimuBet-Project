package com.example.simubetproject.ui.basket;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.simubetproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BasketFragment extends Fragment {

    private RequestQueue requestQueue;
    private final String API_KEY = "6bca7b7411f917a70830a99999d80dec";
    private TextView textViewResult;

    public BasketFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_basket, container, false);
        requestQueue = Volley.newRequestQueue(requireContext());
        textViewResult = view.findViewById(R.id.textViewResult);
        fetchBasketballData();
        return view;
    }

    // Fetch basketball data from the API, not final version as it will be displayed in a RecyclerView
    // Note: Odds will change frequently, so numbers may differ when re-running the app
    private void fetchBasketballData() {
        String url = "https://api.the-odds-api.com/v4/sports/basketball_nba/odds/?regions=us&markets=h2h&bookmakers=draftkings&apiKey=" + API_KEY;

        // Create a JSON array request
        // Note: I used a JsonArrayRequest instead of a JsonObjectRequest because the response is an array of games; JSON file starts as an array
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            StringBuilder result = new StringBuilder();

                            // Loop through the response array; each element is a game
                            // Each game has home team, away team, and odds
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject game = response.getJSONObject(i);

                                // Extract key data
                                String homeTeam = game.getString("home_team");
                                String awayTeam = game.getString("away_team");
                                String commenceTime = game.getString("commence_time");

                                // Get the bookmaker data from h2h market
                                JSONArray bookmakers = game.getJSONArray("bookmakers");
                                JSONObject draftKings = bookmakers.getJSONObject(0); // DraftKings bookmaker, no need to loop through bookmakers since the URL specifies DraftKings
                                JSONArray outcomes = draftKings.getJSONArray("markets")
                                        .getJSONObject(0) // H2H market, no need to loop through markets since the URL specifies h2h
                                        .getJSONArray("outcomes");

                                // Initialize odds
                                String homeOdds = "";
                                String awayOdds = "";

                                // Extract odds for home and away teams
                                // Loop through the two outcomes; home and away
                                // If the outcome name matches the home or away team, extract the odds of that team
                                for (int j = 0; j < outcomes.length(); j++) {
                                    JSONObject outcome = outcomes.getJSONObject(j);
                                    if (outcome.getString("name").equals(homeTeam)) {
                                        homeOdds = outcome.getString("price");
                                    } else if (outcome.getString("name").equals(awayTeam)) {
                                        awayOdds = outcome.getString("price");
                                    }
                                }

                                // Build the result string for each game
                                // Note: This is a simple example for testing purposes; the final version will use a RecyclerView
                                result.append("Game: ").append(homeTeam).append(" vs ").append(awayTeam)
                                        .append("\nTime: ").append(commenceTime)
                                        .append("\nOdds: ")
                                        .append(homeTeam).append(" (").append(homeOdds).append("), ")
                                        .append(awayTeam).append(" (").append(awayOdds).append(")\n\n");
                            }

                            // Set the text view with the result
                            textViewResult.setText(result.toString());
                        }
                        // Error handling
                        catch (JSONException e) {
                            e.printStackTrace();
                            textViewResult.setText("Error parsing data: " + e.getMessage());
                        }
                    }
                    // More error handling
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textViewResult.setText("Error fetching data: " + error.getMessage());
            }
        });

        // Add the request to the request queue
        requestQueue.add(request);
    }
}