package com.app.technofunda.performance;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.app.technofunda.R;
import com.app.technofunda.adapter.PerformanceAdapter;
import com.app.technofunda.ethiqs.SessionManager;
import com.app.technofunda.model.TradingMainResponce;
import com.app.technofunda.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PerformanceActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog loading;
    private List<TradingMainResponce> listSuperHeroes;
    private RecyclerView.Adapter adapter;
    SessionManager session;
    SharedPreferences pref;
    String loginid = "", mobilenumber = "", passwords = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionManager(getApplicationContext());
        pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
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
        recyclerView = findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        loading = ProgressDialog.show(PerformanceActivity.this, "Loading Data", "Please Wait...", false, false);
        getData();

        adapter = new PerformanceAdapter(listSuperHeroes, PerformanceActivity.this);
        Log.d("tag", String.valueOf(adapter.getItemCount()));
        recyclerView.setAdapter(adapter);
       /* toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.refresh) {
                    Toast.makeText(PerformanceActivity.this, "short", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.refresh) {
                    Toast.makeText(PerformanceActivity.this, "mid", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });*/
    }

    private void getData() {
        String MY_URL = Config.URL + "API/APINEW.aspx?msg=Resell&uid=" + loginid;
        Log.d("Url", MY_URL);
        // String MY_URL = "http://technofundaresearch.com/API/APIURL.aspx?msg=Resell&uid=tfr699239";

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
        RequestQueue requestQueue = Volley.newRequestQueue(PerformanceActivity.this);
        requestQueue.add(jsonArrayRequest);
    }

    private void parseData(JSONArray array) {
        try {
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
        if (adapter.getItemCount() == 0) {
            Toast.makeText(this, "Data not available", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
        loading.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.refresh:
                getData();
                listSuperHeroes.clear();
                return true;


        }
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
