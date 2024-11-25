package com.example.simubetproject.ui.basket;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.simubetproject.Model;
import com.example.simubetproject.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class BasketFragment extends Fragment {

    private RequestQueue requestQueue;
    private final String API_KEY = "6bca7b7411f917a70830a99999d80dec";
    private RecyclerView recyclerView;
    private BasketballAdapter adapter;
    private List<Model> basketballGames;

    public BasketFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_basket, container, false);
        requestQueue = Volley.newRequestQueue(requireContext());
        recyclerView = view.findViewById(R.id.basketRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        basketballGames = new ArrayList<>();
        adapter = new BasketballAdapter(basketballGames);
        recyclerView.setAdapter(adapter);
        fetchBasketballData();
        return view;
    }

    private void fetchBasketballData() {
        String url = "https://api.the-odds-api.com/v4/sports/basketball_nba/odds/?regions=us&markets=h2h&bookmakers=draftkings&apiKey=" + API_KEY;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            basketballGames.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject game = response.getJSONObject(i);
                                String homeTeam = game.getString("home_team");
                                String awayTeam = game.getString("away_team");
                                String commenceTime = game.getString("commence_time");

                                JSONArray bookmakers = game.getJSONArray("bookmakers");
                                if (bookmakers.length() > 0) {
                                    JSONObject draftKings = bookmakers.getJSONObject(0);
                                    JSONArray markets = draftKings.getJSONArray("markets");
                                    if (markets.length() > 0) {
                                        JSONArray outcomes = markets.getJSONObject(0).getJSONArray("outcomes");

                                        String homeOdds = "";
                                        String awayOdds = "";

                                        for (int j = 0; j < outcomes.length(); j++) {
                                            JSONObject outcome = outcomes.getJSONObject(j);
                                            if (outcome.getString("name").equals(homeTeam)) {
                                                homeOdds = outcome.getString("price");
                                            } else if (outcome.getString("name").equals(awayTeam)) {
                                                awayOdds = outcome.getString("price");
                                            }
                                        }

                                        basketballGames.add(new Model(homeTeam, awayTeam, commenceTime, homeOdds, awayOdds));
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
            }
        });

        requestQueue.add(request);
    }
}