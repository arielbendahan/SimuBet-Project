package com.example.simubetproject.ui.soccer;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.simubetproject.CheckoutActivity;
import com.example.simubetproject.Model;
import com.example.simubetproject.R;
import com.example.simubetproject.ui.basket.BasketballAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SoccerFragment extends Fragment {
    private final String API_KEY = "02959fcfad0e49335d7b46045bdde808";
    private RequestQueue requestQueue;
    //bets to be send to the checkout
    private ArrayList<Model> selectedBets;
    private List<Model> bundesligaGames;
    private RecyclerView recyclerView;
    private SoccerAdapter adapter;
    Button goToCheckoutButton;


    public SoccerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_soccer, container, false);
        VolleyLog.DEBUG = true;
        requestQueue = Volley.newRequestQueue(requireContext());
        recyclerView = view.findViewById(R.id.football_bundesliga_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        goToCheckoutButton = view.findViewById(R.id.soccer_item_checkout_button);

        selectedBets = new ArrayList<>();

        bundesligaGames = new ArrayList<>();

        adapter = new SoccerAdapter(bundesligaGames, selectedBets, this.getContext(), new SoccerAdapter.betsStateChangeListener() {
            @Override
            public void onBetStateChange(Model bet) {
                //bets validation. Communication between an element from the fragment and the bets from the adapter.
                if (bet.getAmountOddsButtonsPressed() > 2) {
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

        fetchFootballData();
        return view;
    }

    private void fetchFootballData() {
        String url = "https://api.the-odds-api.com/v4/sports/soccer_germany_bundesliga/odds/?regions=us&markets=h2h&bookmakers=unibet&apiKey=" + API_KEY;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            bundesligaGames.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject game1 = response.getJSONObject(i);
                                String homeTeam = game1.getString("home_team");
                                String awayTeam = game1.getString("away_team");
                                String time = game1.getString("commence_time");
                                //for the odds

                                Map<String, Double> outcomes = new HashMap<>();

                                //for the arrays within the resposne
                                JSONArray bookmakersArray = game1.getJSONArray("bookmakers");

                                if (bookmakersArray.length() > 0) {
                                    JSONObject firstBookmakers = bookmakersArray.getJSONObject(0);
                                    JSONArray marketsArray = firstBookmakers.getJSONArray("markets");

                                    if (marketsArray.length() > 0) {
                                        JSONObject firstMarket = marketsArray.getJSONObject(0);
                                        //where the odds are found
                                        JSONArray outcomesArray = firstMarket.getJSONArray("outcomes");

                                        for (int j = 0; j < outcomesArray.length(); j++) {
                                            JSONObject outcome = outcomesArray.getJSONObject(j);
                                            String name = outcome.getString("name");
                                            Double odds = outcome.getDouble("price");

                                            outcomes.put(name, odds);
                                        }
                                    }
                                }

                                bundesligaGames.add(new Model(homeTeam, awayTeam, time, String.valueOf(outcomes.get(homeTeam)), String.valueOf(outcomes.get(awayTeam)), String.valueOf(outcomes.get("Draw"))));
                            }

                            adapter.notifyDataSetChanged();


                        } catch (Exception ex) {
                            Log.d("Error fetching data:", ex.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
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
        }
        );

        requestQueue.add(jsonArrayRequest);
    }
}