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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.app.technofunda.R;
import com.app.technofunda.adapter.CommodityAdapter;
import com.app.technofunda.ethiqs.SessionManager;
import com.app.technofunda.model.TradingMainResponce;
import com.app.technofunda.util.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Futures extends Fragment {

    RecyclerView recyclerView;
    ProgressDialog loading;
    private List<TradingMainResponce> listSuperHeroes;
    private List<TradingMainResponce> listSuperHeroesMain;
    private RecyclerView.Adapter adapter;
    //    String MY_URL = "http://technofundaresearch.com/API/APIURL.aspx?msg=Resell&uid=TFR983898";
    SessionManager session;
    SharedPreferences pref;
    String loginid = "", mobilenumber = "", passwords = "";
    TextView all;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.tab_two_frag, container, false);
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
        getData();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        loading = ProgressDialog.show(getContext(), "Loading Data", "Please Wait...", false, false);


        adapter = new CommodityAdapter(listSuperHeroes, getContext());
        recyclerView.setAdapter(adapter);


        all = v.findViewById(R.id.all);
        v.findViewById(R.id.all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), v);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menuequity2, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        all.setText(item.getTitle());
                        if (item.getTitle().equals("ALL")) {
                            listSuperHeroes.clear();
                            getData();
                        } else if (item.getTitle().equals("Buy")) {
                            listSuperHeroes.clear();
                            getDataBuy();
                        } else if (item.getTitle().equals("Sell")) {
                            listSuperHeroes.clear();
                            getDataSell();
                        }
                        return false;
                    }

                });
                popup.show();
            }

        });

        return v;
    }

    private void getDataBuy() {
        String MY_URL = Config.URL + "API/APINEW.aspx?msg=Resell&uid=" + loginid;
        //  String MY_URL = "API/APIURL.aspx?msg=Resell&uid=TFR983898";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(MY_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("response", String.valueOf(response));
                        parseDataforBuy(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        error.getMessage();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    private void parseDataforBuy(JSONArray array) {
        try {

            //  JSONArray array = new JSONArray(aarray);
            for (int i = 0; i < array.length(); i++) {
                TradingMainResponce superHero = new TradingMainResponce();
                JSONObject json = null;
                json = array.getJSONObject(i);
                if (String.valueOf(json.get("segment")).equals("Futures")) {
                    if (String.valueOf(json.get("Recommendation")).equals("Buy")) {

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


                        listSuperHeroes.add(superHero);
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (adapter.getItemCount() == 0) {
            Toast.makeText(getContext(), "Data not avialable", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
        loading.dismiss();
    }

    private void getDataSell() {
        String MY_URL = Config.URL + "API/APINEW.aspx?msg=Resell&uid=" + loginid;
        //  String MY_URL = "API/APIURL.aspx?msg=Resell&uid=TFR983898";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(MY_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("response", String.valueOf(response));
                        parseDataforSell(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        error.getMessage();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    private void parseDataforSell(JSONArray array) {
        try {

            //  JSONArray array = new JSONArray(aarray);
            for (int i = 0; i < array.length(); i++) {
                TradingMainResponce superHero = new TradingMainResponce();
                JSONObject json = null;
                json = array.getJSONObject(i);
                if (String.valueOf(json.get("segment")).equals("Futures")) {
                    if (String.valueOf(json.get("Recommendation")).equals("Sell")) {

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


                        listSuperHeroes.add(superHero);
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (adapter.getItemCount() == 0) {
            Toast.makeText(getContext(), "Data not avialable", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
        loading.dismiss();
    }

    private void getData() {
        String MY_URL = Config.URL + "API/APINEW.aspx?msg=Resell&uid=" + loginid;
        //  String MY_URL = "API/APIURL.aspx?msg=Resell&uid=TFR983898";

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
                        error.getMessage();
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


                json = array.getJSONObject(i);
                if (String.valueOf(json.get("segment")).equals("Futures")) {
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


                    listSuperHeroes.add(superHero);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (adapter.getItemCount() == 0) {
            Toast.makeText(getContext(), "Data not avialable", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
        loading.dismiss();
    }
}