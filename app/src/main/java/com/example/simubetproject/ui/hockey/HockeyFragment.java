package com.example.simubetproject.ui.hockey;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

public class HockeyFragment extends Fragment {

    private final String API_KEY = "9503f40c5935e671ce94f11f2800d9bd";
    private RequestQueue requestQueue;
    private List<Model> hockeyGames;
    private RecyclerView recyclerView;
    private HockeyAdapter adapter;
    private ArrayList<Model> selectedBets;
    Button goToCheckoutButton;

    public HockeyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hockey, container, false);

        requestQueue = Volley.newRequestQueue(requireContext());
        recyclerView = view.findViewById(R.id.hockeyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        goToCheckoutButton = view.findViewById(R.id.hockey_item_checkout_button);

        hockeyGames = new ArrayList<>();
        selectedBets = new ArrayList<>();

        adapter = new HockeyAdapter(hockeyGames, selectedBets, new betValidationListener() {
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

        fetchHockeyData();
        return view;
    }

    public void fetchHockeyData() {
        String url = "https://api.the-odds-api.com/v4/sports/icehockey/odds/?regions=us&markets=h2h&bookmakers=draftkings&apiKey=" + API_KEY;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            hockeyGames.clear();
                            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                            SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault());

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject game = response.getJSONObject(i);

                                String homeTeam = game.optString("home_team", "Unknown");
                                String awayTeam = game.optString("away_team", "Unknown");
                                String commenceTime = game.optString("commence_time", "");

                                // Parse and format the commence time
                                String formattedDate = commenceTime;
                                if (!commenceTime.isEmpty()) {
                                    try {
                                        Date date = inputFormat.parse(commenceTime);
                                        formattedDate = outputFormat.format(date);
                                    } catch (ParseException e) {
                                        Log.e("Date Parsing", "Error parsing date: " + commenceTime);
                                    }
                                }

                                // Handle bookmakers and odds
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

                                        hockeyGames.add(new Model(homeTeam, awayTeam, formattedDate, homeOdds, awayOdds));
                                    }
                                }
                            }

                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.e("JSON Parsing", "Error parsing JSON: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error fetching data: " + error.getMessage());
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }
}
