package com.example.simubetproject.ui.basket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.simubetproject.CheckoutActivity;
import com.example.simubetproject.Model;
import com.example.simubetproject.R;
import com.example.simubetproject.betValidationListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BasketFragment extends Fragment {

    private RequestQueue requestQueue;
    private final String API_KEY = "6bca7b7411f917a70830a99999d80dec";
    private RecyclerView recyclerView;
    private BasketballAdapter adapter;
    private List<Model> basketballGames;
    private ArrayList<Model> selectedBets;
    Button goToCheckoutButton;


    public BasketFragment() {
        // Required empty public constructor
    }

    // onCreateView method that inflates the layout for this fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_basket, container, false);
        requestQueue = Volley.newRequestQueue(requireContext());
        recyclerView = view.findViewById(R.id.basketRecyclerView);
        goToCheckoutButton = view.findViewById(R.id.basket_item_checkout_button);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        basketballGames = new ArrayList<>();
        selectedBets = new ArrayList<>();
        adapter = new BasketballAdapter(basketballGames, selectedBets, new betValidationListener() {
            @Override
            public void onBetStateChange(Model bet) {
                //bets validation. Communication between an element from the fragment and the bets from the adapter.
                if (bet.getAmountOddsButtonsPressed() != 1) {
                    //bet is not valid
                    goToCheckoutButton.setEnabled(false);
                    return;
                }

                goToCheckoutButton.setEnabled(true);
            }
        });

        recyclerView.setAdapter(adapter);

        goToCheckoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), CheckoutActivity.class);

                intent.putParcelableArrayListExtra("selectedBets", selectedBets);

                view.getContext().startActivity(intent);
            }
        });

        fetchBasketballData();

        return view;
    }

    // Method to fetch basketball data from the API
    public void fetchBasketballData() {
        String url = "https://api.the-odds-api.com/v4/sports/basketball_nba/odds/?regions=us&markets=h2h&bookmakers=draftkings&apiKey=" + API_KEY;

        // Uses JsonArrayRequest instead of JsonObjectRequest since the JSON begins as an array
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            basketballGames.clear();
                            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                            SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault());

                            // Loop through the JSON array to get the home team, away team, and commence time
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject game = response.getJSONObject(i);
                                String homeTeam = game.getString("home_team");
                                String awayTeam = game.getString("away_team");
                                String commenceTime = game.getString("commence_time");

                                // Format the date to a more readable format; it was previously in the format "2024-11-27T00:00:00Z"
                                Date date = inputFormat.parse(commenceTime);
                                String formattedDate = outputFormat.format(date);

                                // Get the bookmakers array and check if it is not empty
                                JSONArray bookmakers = game.getJSONArray("bookmakers");
                                if (bookmakers.length() > 0) {
                                    JSONObject draftKings = bookmakers.getJSONObject(0); // Assume DraftKings is the first bookmaker, which it is due to the API link
                                    JSONArray markets = draftKings.getJSONArray("markets");
                                    if (markets.length() > 0) {
                                        JSONArray outcomes = markets.getJSONObject(0).getJSONArray("outcomes");

                                        String homeOdds = "";
                                        String awayOdds = "";

                                        // Inner loop to get the odds for the home and away teams
                                        for (int j = 0; j < outcomes.length(); j++) {
                                            JSONObject outcome = outcomes.getJSONObject(j);
                                            if (outcome.getString("name").equals(homeTeam)) {
                                                homeOdds = outcome.getString("price");
                                            } else if (outcome.getString("name").equals(awayTeam)) {
                                                awayOdds = outcome.getString("price");
                                            }
                                        }
                                        // Add the game to the list of basketball games
                                        basketballGames.add(new Model(homeTeam, awayTeam, formattedDate, homeOdds, awayOdds));
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException  | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            // Error listener to log any errors that occur
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    int statusCode = error.networkResponse.statusCode;
                    String errorMessage = new String(error.networkResponse.data);
                    Log.e("Volley Error", "Status Code: " + statusCode);
                    Log.e("Volley Error", "Error Message: " + errorMessage);
                } else {
                    Log.e("Volley Error", "No network response, error: " + error);
                }
            }
        });

        requestQueue.add(request);
    }
}