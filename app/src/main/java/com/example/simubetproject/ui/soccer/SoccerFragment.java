package com.example.simubetproject.ui.soccer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.simubetproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {link SoccerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoccerFragment extends Fragment {
    private final String API_KEY = "02959fcfad0e49335d7b46045bdde808";
    private TextView testTestView;
    private RequestQueue requestQueue;

    /*
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

     */

    public SoccerFragment() {
        // Required empty public constructor
    }
/*
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SoccerFragment.

    // TODO: Rename and change types and number of parameters
    public static SoccerFragment newInstance(String param1, String param2) {
        SoccerFragment fragment = new SoccerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_soccer, container, false);
        VolleyLog.DEBUG = true;
        testTestView = view.findViewById(R.id.football_test_textview);
        requestQueue = Volley.newRequestQueue(requireContext());
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

                            JSONObject game1 = response.getJSONObject(1);
                            String homeTeam = game1.getString("home_team");
                            String awayTeam = game1.getString("away_team");
                            String time = game1.getString("commence_time");
                            //for the odds

                            Map<String, Double> outcomes = new HashMap<>();

                            //for the arrays within the resposne
                            JSONArray bookmakersArray = game1.getJSONArray("bookmakers");

                            if(bookmakersArray.length() > 0) {
                                JSONObject firstBookmakers = bookmakersArray.getJSONObject(0);
                                JSONArray marketsArray = firstBookmakers.getJSONArray("markets");

                                if (marketsArray.length() > 0) {
                                    JSONObject firstMarket = marketsArray.getJSONObject(0);
                                    //where the odds are found
                                    JSONArray outcomesArray = firstMarket.getJSONArray("outcomes");

                                    for (int i = 0; i < outcomesArray.length(); i++) {
                                        JSONObject outcome = outcomesArray.getJSONObject(i);
                                        String name = outcome.getString("name");
                                        Double odds = outcome.getDouble("price");

                                        outcomes.put(name, odds);
                                    }
                                }
                            }

                            StringBuilder resultText = new StringBuilder();
                            resultText.append("Commence time: ").append(time).append("\n");
                            resultText.append("Home Team: ").append(homeTeam).append("\n");
                            resultText.append("Away Team: ").append(awayTeam).append("\n\n");

                            resultText.append("Outcomes:\n");

                            for (Map.Entry<String, Double> entry : outcomes.entrySet()) {
                                resultText.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                            }

                            testTestView.setText(resultText);


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
                    Log.e("Volley Error", "No network response, error: " + error.toString());
                }
            }
        }
        );

        requestQueue.add(jsonArrayRequest);
    }
}