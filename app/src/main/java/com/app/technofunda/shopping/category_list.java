package com.app.technofunda.shopping;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.app.technofunda.*;
import com.app.technofunda.ethiqs.SessionManager;
import com.app.technofunda.profile.ProfileActivity;
import com.app.technofunda.ethiqs.frontview;
import com.app.technofunda.util.Config;

public class category_list extends AppCompatActivity {
    private ListView listView;
    ImageView imageView;
    private ArrayList<String> imagesArraynamelist;
   private ArrayList<Integer> imagesArray;
    public static final String TAG_ID = "Id";
    public static final String TAG_NAME = "Name";
    EditText edit_search;
    Category_search adapter;
    ImageView homef,updatef,logoutf,profilef;
    ProgressDialog progressBar;
    SessionManager session;
    SharedPreferences pref;
    Toolbar toolbar;
    String tokens,loginid,mobilenumber,passwords,token,s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        edit_search=(EditText)findViewById(R.id.search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionManager(getApplicationContext());
        pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            loginid=pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            mobilenumber=pref.getString(Config.KEY_MOBILE, "");
        }
        if (pref.contains(Config.KEY_PASSWORD)) {
            passwords=pref.getString(Config.KEY_PASSWORD, "");
        }
        imagesArraynamelist = new ArrayList<>();
        imagesArraynamelist.add("Apparesal");
        imagesArraynamelist.add("Home Appilances");
        imagesArraynamelist.add("Eletronics");
        imagesArraynamelist.add("Home and Kitchen");
        imagesArraynamelist.add("IT Product");
        imagesArraynamelist.add("Life style");
        imagesArraynamelist.add("Flower gift & cakes");
        imagesArraynamelist.add("Books & Magazines");
        imagesArraynamelist.add("Memory Cards, Pen Drives & HDD");
        imagesArraynamelist.add("Fitness & Sports");
        imagesArraynamelist.add("Jewellery");
        imagesArraynamelist.add("Perfumes");
        imagesArraynamelist.add("Baby Products & kids toys");
        imagesArraynamelist.add("Groceries");


     //   imagesArray=new ArrayList<>();
      //  imagesArray.add(R.drawable.ic_play_arrow_black_24dp);

        ListViewAdapter listViewAdapter = new ListViewAdapter();    // Object of ListViewAdapter

        listView = (ListView) findViewById(R.id.category_list);
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    Intent myIntent = new Intent(view.getContext(), grid_categery.class);
                    startActivityForResult(myIntent, 0);
                }

                if (position == 1) {
                    Intent myIntent = new Intent(view.getContext(), HomeAppilance.class);
                    startActivityForResult(myIntent, 0);
                }

                if (position == 2) {
                    //Intent myIntent = new Intent(view.getContext(), ListItemActivity1.class);
                   // startActivityForResult(myIntent, 0);
                }

                if (position == 3) {
                   // Intent myIntent = new Intent(view.getContext(), ListItemActivity2.class);
                   // startActivityForResult(myIntent, 0);
                }

                if (position == 4) {
                   // Intent myIntent = new Intent(view.getContext(), ListItemActivity1.class);
                   // startActivityForResult(myIntent, 0);
                }

                if (position == 5) {
                   // Intent myIntent = new Intent(view.getContext(), ListItemActivity2.class);
                   // startActivityForResult(myIntent, 0);
                }

                if (position == 6) {
                    //Intent myIntent = new Intent(view.getContext(), ListItemActivity1.class);
                    //startActivityForResult(myIntent, 0);
                }

                if (position == 7) {
                    //Intent myIntent = new Intent(view.getContext(), ListItemActivity2.class);
                    //startActivityForResult(myIntent, 0);
                }
            }
        });

        //footer
        homef=(ImageView)findViewById(R.id.home_footer);
        homef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(category_list.this,frontview.class);
                startActivity(intent);
            }
        });
//        updatef=(ImageView)findViewById(R.id.update_footer);
//        updatef.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(category_list.this,NewsUpdateActivity.class);
//                startActivity(intent);
//
//            }
//        });
        logoutf=(ImageView)findViewById(R.id.logout_footer);
        logoutf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nf = cn.getActiveNetworkInfo();
                if (nf != null && nf.isConnected() == true) {
                    progressBar = ProgressDialog.show(category_list.this, "Please wait ...", "Logout processing ...", false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                            } catch (Exception e) {
                            }
                            session.logoutUser();
                            progressBar.dismiss();
                            category_list.this.finish();
                        }
                    }).start();

                } else {
                    Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
                }
            }
        });
        profilef=(ImageView)findViewById(R.id.profile_footer);
        profilef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(category_list.this,ProfileActivity.class);
                startActivity(intent);

            }
        });



    }

    public class ListViewAdapter extends BaseAdapter {
        String[] planetNames = {
                "Mercury",
                "Venus",
                "Earth",
                "Mars",
                "Jupiter",
                "Saturn",
                "Neptune",
                "Uranus",
                "Pluto"
        };

        @Override
        public int getCount() {
            return  imagesArraynamelist.size();
        }

        @Override
        public Object getItem(int position) {
            return  imagesArraynamelist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            View rootView = inflater.inflate(R.layout.category_row_list, null);

            TextView txtPlanetName = (TextView) rootView.findViewById(R.id.list_element);
            txtPlanetName.setText( imagesArraynamelist.get(position));
            Log.d("TAG", "Position: " + position);

          //  ImageView imgProfilePic = (ImageView) rootView.findViewById(R.id.imgcategory_list);
          //  imgProfilePic.setImageResource(imagesArray.get(position));

            return rootView;
        }
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
}
