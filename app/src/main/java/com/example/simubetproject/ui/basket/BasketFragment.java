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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.simubetproject.R;
import org.json.JSONObject;

public class BasketFragment extends Fragment {

    private RequestQueue requestQueue;

    public BasketFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_basket, container, false);
        requestQueue = Volley.newRequestQueue(requireContext());
        fetchBasketballData();
        return view;
    }

    private void fetchBasketballData() {
        String url = "https://api.example.com/basketball?api_key=your_api_key"; // Replace with your API URL and key

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Update the UI with the fetched data
                        //textView.setText(response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //textView.setText("Error: " + error.getMessage());
                    }
                });

        // Add the request to the RequestQueue.
        requestQueue.add(jsonObjectRequest);
    }
}