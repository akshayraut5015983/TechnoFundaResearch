package com.app.technofunda.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.technofunda.R;
import com.app.technofunda.ethiqs.frontview;

import java.util.ArrayList;
import java.util.List;

public class Nav_Shopping extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView txtappnametool;
    TextView txtnewarrival, txtmen, txtwomen, txtkids, baby_care;
    EditText editText;
    ImageView icon_home;

    private LinearLayoutManager lLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav__shopping);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        List<ItemObject> rowListItem = getAllItemList();
        lLayout = new LinearLayoutManager(Nav_Shopping.this);

        RecyclerView rView = (RecyclerView) findViewById(R.id.recycler_view);
        rView.setLayoutManager(lLayout);

        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(Nav_Shopping.this, rowListItem);
        rView.setAdapter(rcAdapter);

        txtnewarrival = (TextView) findViewById(R.id.nav_oilmasala);
        txtnewarrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Nav_Shopping.this, grid_categery.class);
                startActivity(intent);
            }
        });
        editText = (EditText) findViewById(R.id.edtsearch);
        //editText=(EditText)findViewById(R.id.search);
        //edt_search.setFocusable(false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        txtappnametool = (TextView) findViewById(R.id.txtappname);
        txtappnametool.setText(R.string.app_name);
        icon_home = (ImageView) findViewById(R.id.home_toolbar);
        icon_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Nav_Shopping.this, frontview.class);
                startActivity(intent);
            }
        });
        txtmen = (TextView) findViewById(R.id.nav_foodgrain);
        txtmen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Nav_Shopping.this, FoodGrains.class);
                startActivity(intent);
            }
        });
        txtwomen = (TextView) findViewById(R.id.nav_beautyhygine);
        txtwomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Nav_Shopping.this, BeautyAndHygine.class);
                startActivity(intent);
            }
        });
        txtkids = (TextView) findViewById(R.id.nav_cleanhousehold);
        txtkids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Nav_Shopping.this, grid_categery.class);
                startActivity(intent);
            }
        });
        baby_care = (TextView) findViewById(R.id.nav_babycare);
        baby_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Nav_Shopping.this, BabyCare.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav__shopping, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_offer) {
            // Handle the camera action
        } else if (id == R.id.nav_oilmasala) {
            Intent intent = new Intent(Nav_Shopping.this, grid_categery.class);
            startActivity(intent);


        } else if (id == R.id.nav_foodgrain) {
            Intent intent = new Intent(Nav_Shopping.this, grid_categery.class);
            startActivity(intent);


        } else if (id == R.id.nav_beautyhygine) {
            Intent intent = new Intent(Nav_Shopping.this, grid_categery.class);
            startActivity(intent);


        } else if (id == R.id.nav_cleanhousehold) {
            Intent intent = new Intent(Nav_Shopping.this, HomeAppilance.class);
            startActivity(intent);


        } else if (id == R.id.nav_sport) {


        } else if (id == R.id.nav_gifts) {

        } else if (id == R.id.nav_deal) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private List<ItemObject> getAllItemList() {

        List<ItemObject> allItems = new ArrayList<ItemObject>();
        allItems.add(new ItemObject("Oil", R.drawable.oil));
        allItems.add(new ItemObject("Masala ", R.drawable.masala));
        allItems.add(new ItemObject("House Hold", R.drawable.household));
        allItems.add(new ItemObject("Baby Care", R.drawable.babycare));

        return allItems;
    }

}
