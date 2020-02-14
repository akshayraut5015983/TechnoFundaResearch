package com.app.technofunda.shopping;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.app.technofunda.R;
import com.app.technofunda.ethiqs.SessionManager;
import com.app.technofunda.ethiqs.frontview;
import com.app.technofunda.newsupdate.NewsUpdateActivity;
import com.app.technofunda.profile.ProfileActivity;
import com.app.technofunda.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class grid_categery extends AppCompatActivity {
    private GridView gridView;
    private ArrayList<String> imagesArrayname = new ArrayList<>();
    private ArrayList<Integer> imagesArray = new ArrayList<>();
    TextView price_tag,offer_tag;
    ImageView imgcart;
    ImageView homef,updatef,logoutf,profilef;
    ProgressDialog progressBar;
    SessionManager session;
    SharedPreferences pref;
    private ProgressDialog pDialog;
    Toolbar toolbar;
    String tokens,loginid,mobilenumber,passwords,token,s,category,msg,xml,url;
    private List<CategoryList1> listSuperHeroes;
    ProgressDialog loading;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public static final String MY_URL="http://srsmarketing.biz/API/Apiurl.aspx?Mobile=";
    public static final String TAG_Id = "productid";
    public static final String TAG_IMAGE_URL = "imagedata";
    public static final String TAG_DESCRIPTION = "productname";
    public static final String TAG_PRICE = "mrp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_categery);
//        price_tag=(TextView)findViewById(R.id.price);
//        offer_tag=(TextView)findViewById(R.id.offer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        imgcart = (ImageView) findViewById(R.id.cart);
        imgcart.setVisibility(View.VISIBLE);

        imgcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(grid_categery.this,ShowCart.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView5);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        listSuperHeroes = new ArrayList<>();
        getData();


        //footer
        homef = (ImageView) findViewById(R.id.home_footer);
        homef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(grid_categery.this, frontview.class);
                startActivity(intent);
            }
        });

        updatef = (ImageView) findViewById(R.id.update_footer);
        updatef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(grid_categery.this, NewsUpdateActivity.class);
                startActivity(intent);

            }
        });
        logoutf = (ImageView) findViewById(R.id.logout_footer);
        logoutf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nf = cn.getActiveNetworkInfo();
                if (nf != null && nf.isConnected() == true) {
                    progressBar = ProgressDialog.show(grid_categery.this, "Please wait ...", "Logout processing ...", false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                            } catch (Exception e) {
                            }
                            session.logoutUser();
                            progressBar.dismiss();
                            grid_categery.this.finish();
                        }
                    }).start();

                } else {
                    Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
                }
            }
        });
        profilef = (ImageView) findViewById(R.id.profile_footer);
        profilef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(grid_categery.this, ProfileActivity.class);
                startActivity(intent);

            }
        });
    }


//need json dada
    private void getData() {
//Showing a progress dialog
        loading = ProgressDialog.show(grid_categery.this,"Loading Data", "Please Wait...",false,false);
//Creating a json array request
               category = "oil_and_masala";
                msg = "Product";
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(MY_URL + String.valueOf(mobilenumber) + "&msg=" + String.valueOf(msg) + "%20" + String.valueOf(category), new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                  //      Toast.makeText(grid_categery.this, "response:" + response, Toast.LENGTH_LONG).show();
                        //Dismissing progress dialog
                  //      Log.d("response", String.valueOf(response));
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
                RequestQueue requestQueue = Volley.newRequestQueue(grid_categery.this);
                //Adding request to the queue
                requestQueue.add(jsonArrayRequest);
        }


    //This method will parse json data
    private void parseData(JSONArray array){
        for(int i = 0; i<array.length(); i++) {
            CategoryList1 superHero = new CategoryList1();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                superHero.setId(json.getString(TAG_Id));
                superHero.setName(json.getString(TAG_DESCRIPTION));
                superHero.setImageUrl(json.getString(TAG_IMAGE_URL));
                superHero.setPrice(json.getString(TAG_PRICE));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            listSuperHeroes.add(superHero);
        }
      //Finally initializing our adapter
        adapter = new CardAdapterCategoryList1(listSuperHeroes,grid_categery.this);

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
