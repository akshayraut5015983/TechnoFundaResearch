package com.app.technofunda.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.app.technofunda.R;
import com.app.technofunda.adapter.TradingMainAdapter;
import com.app.technofunda.ethiqs.SessionManager;
import com.app.technofunda.model.TradingMainResponce;
import com.app.technofunda.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    ProgressDialog loading;
    private List<TradingMainResponce> listSuperHeroes;
    private RecyclerView.Adapter adapter;
    SessionManager session;
    SharedPreferences pref;
    String loginid = "", mobilenumber = "", passwords = "";

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        session = new SessionManager(getContext());
        pref = getActivity().getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            loginid = pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            mobilenumber = pref.getString(Config.KEY_MOBILE, "");
        }
        if (pref.contains(Config.KEY_PASSWORD)) {
            passwords = pref.getString(Config.KEY_PASSWORD, "");
        }
        listSuperHeroes = new ArrayList<>();
        recyclerView = v.findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        getData();
        return v;
    }

    private void getData() {
        String MY_URL = Config.URL + "API/APIURL.aspx?msg=Resell&uid=" + loginid;
        //  String MY_URL = "API/APIURL.aspx?msg=Resell&uid=TFR983898";
        loading = ProgressDialog.show(getContext(), "Loading Data", "Please Wait...", false, false);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(MY_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("response", String.valueOf(response));
                        parseData(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    private void parseData(JSONArray array) {
        try {

            //  JSONArray array = new JSONArray(aarray);
            for (int i = 0; i < array.length(); i++) {
                TradingMainResponce superHero = new TradingMainResponce();
                JSONObject json = null;
                try {

                    json = array.getJSONObject(i);
                    superHero.setSegment(json.getString("segment"));
                    superHero.setRecommendation(json.getString("Recommendation"));
                    superHero.setSymbol(json.getString("Symbol"));
                    superHero.setEntryPrice(json.getInt("EntryPrice"));
                    superHero.setSL(json.getInt("SL"));
                    superHero.setTG1(json.getInt("TG1"));
                    superHero.setTG2(json.getInt("TG2"));
                    superHero.setQuantity(json.getInt("Quantity"));
                    superHero.setExitValue(json.getInt("Exit_value"));
                    superHero.setProfitLoss(json.getInt("Profit_Loss"));
                    superHero.setTime(json.getString("Time"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listSuperHeroes.add(superHero);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter = new TradingMainAdapter(listSuperHeroes, getContext());
        Log.d("tag", String.valueOf(adapter.getItemCount()));
        recyclerView.setAdapter(adapter);
        loading.dismiss();
    }
}
