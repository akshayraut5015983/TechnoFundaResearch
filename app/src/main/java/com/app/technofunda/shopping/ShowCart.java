package com.app.technofunda.shopping;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.app.technofunda.R;
import com.app.technofunda.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowCart extends AppCompatActivity {
    private List<PackageListPojo> listSuperHeroes;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    ProgressDialog loading;
    SharedPreferences pref;
    String getid,user_id_mobile,user_id,user_mobile,password,showcarturl;

    public static final String MY_URL="http://srsmarketing.biz/API/Apiurl.aspx?mobile=";
    public static final String TAG_Id = "id";
    public static final String TAG_IMAGE_URL = "dataImage";
    public static final String TAG_TITLE = "productname";
    public static final String TAG_PKG1 = "price";
    public static final String TAG_PKG2 = "finalamount";
    String showcart="ShowCart";
    TextView tv_price;
    String showtotal,showtotalurl;
    Button cuntinus_tocart2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView3);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ShowCart.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        listSuperHeroes = new ArrayList<>();
        // tv_price=(TextView)findViewById(R.id.show_finle_price);
        cuntinus_tocart2=(Button)findViewById(R.id.cuntinus_tocart2);
        pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            user_id = pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            user_mobile =  pref.getString(Config.KEY_MOBILE, "");
        }
        if (pref.contains(Config.KEY_PASSWORD)) {
            password = pref.getString(Config.KEY_PASSWORD, "");
        }

        cuntinus_tocart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ShowCart.this,CurrentLocation.class);
                startActivity(i);
            }
        });

        getData();
    }
    private void getData() {

        //Showing a progress dialog
        loading = ProgressDialog.show(ShowCart.this,"Loading Data", "Please Wait...",false,false);
//Creating a json array request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(MY_URL+user_mobile+"&pass="+password+"&msg="+showcart+"%20"+user_mobile,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing progress dialog
                        Log.d("response", String.valueOf(response));
                        //calling method to parse json array
                        parseData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                    }
                });
        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(ShowCart.this);
        //Adding request to the queue
        requestQueue.add(jsonArrayRequest);
        //Toast.makeText(ShowCart.this, ""+jsonArrayRequest, Toast.LENGTH_LONG).show();
    }

    //This method will parse json data
    private void parseData(JSONArray array){
        for(int i = 0; i<array.length(); i++) {
            PackageListPojo superHero = new PackageListPojo();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                superHero.setPackageid(json.getString(TAG_Id));
                // superHero.setImage(json.getString(TAG_IMAGE_URL));
                superHero.setPackagename(json.getString(TAG_TITLE));
                superHero.setFeature1(json.getString(TAG_PKG1));
                superHero.setFeature2(json.getString(TAG_PKG2));
              /*  superHero.setFeature3(json.getString(TAG_PKG3));
                superHero.setPackageAmount(json.getString(TAG_MRP));
                superHero.setDiscount(json.getString(TAG_DISCOUNT));*/
               /* superHero.setFinalAmount(json.getString(TAG_FAMOUNT));
                superHero.setOffer(json.getString(TAG_Offer));
                superHero.setMembershipBenifit(json.getString(TAG_Benifit));*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listSuperHeroes.add(superHero);
        }
        //Finally initializing our adapter
        adapter = new CardAdapterCompleteWork(listSuperHeroes,ShowCart.this);
        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
        loading.dismiss();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Are you sure want to Exit?");
        alertDialogBuilder.setMessage("Click yes to exit!").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

