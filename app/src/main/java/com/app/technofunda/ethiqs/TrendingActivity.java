package com.app.technofunda.ethiqs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.app.technofunda.NewsAndUpdateActivity;
import com.app.technofunda.R;
import com.app.technofunda.adapter.TradingMainAdapter;
import com.app.technofunda.model.TradingMainResponce;
import com.app.technofunda.performance.PerformanceActivity;
import com.app.technofunda.util.Config;
import com.app.technofunda.utility.ChatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TrendingActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;

    private FragmentManager manager;
    private FragmentTransaction fragmentTransaction;
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
        setContentView(R.layout.activity_trending);

        Toolbar toolbar = (Toolbar) findViewById(com.app.technofunda.R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setHomeAsUpIndicator(com.app.srsmarketing.R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

      /*  manager = this.getSupportFragmentManager();
        fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.layout, new HomeFragment()).commit();*/

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
        loading = ProgressDialog.show(TrendingActivity.this, "Loading Data", "Please Wait...", false, false);
        getData();
        adapter = new TradingMainAdapter(listSuperHeroes, TrendingActivity.this);
        Log.d("tag", String.valueOf(adapter.getItemCount()));
        recyclerView.setAdapter(adapter);

        mDrawerLayout = (DrawerLayout) findViewById(com.app.technofunda.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, com.app.technofunda.R.string.navigation_drawer_open, com.app.technofunda.R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                mDrawerLayout.closeDrawers();
                if (menuItem.getTitle().equals("News And Update")) {
                    Intent i = new Intent(TrendingActivity.this, NewsAndUpdateActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.hold, android.R.anim.fade_out);

                }
                else if (menuItem.getTitle().equals("Trending")) {
                    Intent i = new Intent(TrendingActivity.this, TrendingActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.hold, android.R.anim.fade_out);
                }else if (menuItem.getTitle().equals("Home")) {
                    Intent i = new Intent(TrendingActivity.this, frontview.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.hold, android.R.anim.fade_out);
                } else if (menuItem.getTitle().equals("Research Calls")) {
                    Intent i = new Intent(TrendingActivity.this, ReferLinkActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.hold, android.R.anim.fade_out);
                    finish();

                } else if (menuItem.getTitle().equals("Performance")) {
                    Intent i = new Intent(TrendingActivity.this, PerformanceActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.hold, android.R.anim.fade_out);
                } else if (menuItem.getTitle().equals("Refer Link")) {
                    Intent shareintent = new Intent();
                    shareintent.setAction(Intent.ACTION_SEND);
                    shareintent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id= h1");
                    shareintent.setType("text/plain");
                    startActivity(Intent.createChooser(shareintent, "share via"));
                } else if (menuItem.getTitle().equals("LogOut")) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(TrendingActivity.this);
                    builder2.setTitle("Logout");
                    builder2.setMessage("Do you want logout");
                    builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            session.logoutUser();
                            Intent i = new Intent(TrendingActivity.this, LoginActivity.class);
                            startActivity(i);
                        }
                    });
                    builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });
                    builder2.show();
                }
                return true;
            }
        });


        findViewById(R.id.toolImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabImg);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TrendingActivity.this, ChatActivity.class);
                startActivity(i);
            }
        });


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
                listSuperHeroes.clear();
                getData();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
        RequestQueue requestQueue = Volley.newRequestQueue(TrendingActivity.this);
        requestQueue.add(jsonArrayRequest);

    }

    private void parseData(JSONArray array) {
        try {

            //  JSONArray array = new JSONArray(aarray);
            for (int i = 0; i < array.length(); i++) {
                TradingMainResponce superHero = new TradingMainResponce();
                JSONObject json = null;
                json = array.getJSONObject(i);
                try {

                    if (String.valueOf(json.get("Exit_value")).equals("0")) {

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

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (adapter.getItemCount() <= 0) {
            Toast.makeText(this, "Data not available", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
        loading.dismiss();
    }
}
