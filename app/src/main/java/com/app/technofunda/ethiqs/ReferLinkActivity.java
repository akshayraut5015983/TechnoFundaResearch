package com.app.technofunda.ethiqs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.app.technofunda.NewsAndUpdateActivity;
import com.app.technofunda.R;
import com.app.technofunda.adapter.TradingMainAdapter;
import com.app.technofunda.fragment.Commodity;
import com.app.technofunda.fragment.Equity;
import com.app.technofunda.fragment.Futures;
import com.app.technofunda.fragment.Optional;
import com.app.technofunda.model.TradingMainResponce;
import com.app.technofunda.performance.PerformanceActivity;
import com.app.technofunda.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReferLinkActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    SessionManager session;
    SharedPreferences pref;
    Menu menu;
    View one, two, theww, four;
    private FragmentManager manager;
    private FragmentTransaction fragmentTransaction;


    String loginid = "", mobilenumber = "", passwords = "";

    private List<TradingMainResponce> listSuperHeroes;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.app.technofunda.R.layout.activity_referlink);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        session = new SessionManager(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(com.app.technofunda.R.id.toolbar);
        //   toolbar.setTitle("StockName");
        toolbar.inflateMenu(R.menu.refresh);
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        theww = findViewById(R.id.three);
        four = findViewById(R.id.four);
        manager = this.getSupportFragmentManager();
        fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.container, new Equity()).commit();

        session = new SessionManager(this);
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        getData();
        listSuperHeroes = new ArrayList<TradingMainResponce>();
        adapter = new TradingMainAdapter(listSuperHeroes, this);

        findViewById(R.id.layoutOne).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                manager = getSupportFragmentManager();
                fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new Equity()).commit();


             /*   Bundle bundle = new Bundle();
                // intent.putStringArrayListExtra("test", (ArrayList<String>) dada);
                intent.putExtra("test", (Serializable) listSuperHeroes);
                bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) listSuperHeroes);
                Equity fragobj = new Equity();
                fragobj.setArguments(bundle);
*/
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.GONE);
                theww.setVisibility(View.GONE);
                four.setVisibility(View.GONE);

            }
        });
        findViewById(R.id.layoutTwo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager = getSupportFragmentManager();
                fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new Futures()).commit();
                one.setVisibility(View.GONE);
                two.setVisibility(View.VISIBLE);
                theww.setVisibility(View.GONE);
                four.setVisibility(View.GONE);
            }
        });
        findViewById(R.id.layoutThree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager = getSupportFragmentManager();
                fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new Commodity()).commit();
                one.setVisibility(View.GONE);
                two.setVisibility(View.GONE);
                theww.setVisibility(View.VISIBLE);
                four.setVisibility(View.GONE);
            }
        });
        findViewById(R.id.layoutFour).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager = getSupportFragmentManager();
                fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new Optional()).commit();
                one.setVisibility(View.GONE);
                two.setVisibility(View.GONE);
                theww.setVisibility(View.GONE);
                four.setVisibility(View.VISIBLE);
            }
        });


        /*TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("EQUITY"));
        tabLayout.addTab(tabLayout.newTab().setText("FUTURES"));
        tabLayout.addTab(tabLayout.newTab().setText("COMMODITY"));
        tabLayout.addTab(tabLayout.newTab().setText("OPTIONAL"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) findViewById(com.app.srsmarketing.R.id.pager);
        final ReferLinkAdapter adapter = new ReferLinkAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });*/

        findViewById(R.id.toolImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(com.app.technofunda.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, com.app.technofunda.R.string.navigation_drawer_open, com.app.technofunda.R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(com.app.technofunda.R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        menu = navigationView.getMenu();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                mDrawerLayout.closeDrawers();
                if (menuItem.getTitle().equals("News And Update")) {
                    Intent i = new Intent(ReferLinkActivity.this, NewsAndUpdateActivity.class);
                    startActivity(i);
                } else if (menuItem.getTitle().equals("Home")) {
                    Intent i = new Intent(ReferLinkActivity.this, frontview.class);
                    startActivity(i);
                }
                if (menuItem.getTitle().equals("Trending")) {
                    Intent i = new Intent(ReferLinkActivity.this, TrendingActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.hold, android.R.anim.fade_out);
                }else if (menuItem.getTitle().equals("Research Calls")) {

                } else if (menuItem.getTitle().equals("Performance")) {
                    Intent i = new Intent(ReferLinkActivity.this, PerformanceActivity.class);
                    startActivity(i);

                } else if (menuItem.getTitle().equals("Refer Link")) {
                    Intent shareintent = new Intent();
                    shareintent.setAction(Intent.ACTION_SEND);
                    shareintent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id= h1");
                    shareintent.setType("text/plain");
                    startActivity(Intent.createChooser(shareintent, "share via"));
                } else if (menuItem.getTitle().equals("LogOut")) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(ReferLinkActivity.this);
                    builder2.setTitle("Logout");
                    builder2.setMessage("Do you want logout");
                    builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            session.logoutUser();
                            Intent i = new Intent(ReferLinkActivity.this, LoginActivity.class);
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
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.refresh) {
                 onStart();
                }
                return false;
            }
        });

    }



    private void getData() {


        String MY_URL = Config.URL + "API/APINEW.aspx?msg=Resell&uid=" + loginid;

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

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(ReferLinkActivity.this);
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

        adapter.notifyDataSetChanged();

    }


}