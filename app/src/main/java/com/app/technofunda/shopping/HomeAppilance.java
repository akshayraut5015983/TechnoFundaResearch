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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.app.technofunda.*;
import com.app.technofunda.ethiqs.SessionManager;
import com.app.technofunda.newsupdate.NewsUpdateActivity;
import com.app.technofunda.profile.ProfileActivity;
import com.app.technofunda.ethiqs.frontview;
import com.app.technofunda.util.Config;

public class HomeAppilance extends AppCompatActivity {
    private GridView gridView;
    private ArrayList<String> imagesArrayname = new ArrayList<>();
    private ArrayList<Integer> imagesArray = new ArrayList<>();

    ImageView imgcart;
    ImageView homef,updatef,logoutf,profilef;
    ProgressDialog progressBar;
    SessionManager session;
    SharedPreferences pref;
    Toolbar toolbar;
    String tokens,loginid,mobilenumber,passwords,token,s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_appilance);

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

        imgcart=(ImageView)findViewById(R.id.cart);
        imgcart.setVisibility(View.VISIBLE);
        imagesArray = new ArrayList<>();
        imagesArray.add(R.drawable.coffiemaker);
        imagesArray.add(R.drawable.air);
        imagesArray.add(R.drawable.iron);
        imagesArray.add(R.drawable.cooker);
        imagesArray.add(R.drawable.fryer);
        imagesArray.add(R.drawable.heater);
        imagesArray.add(R.drawable.juicer);

        imagesArrayname = new ArrayList<>();
        imagesArrayname.add("Kitchen Appliance");
        imagesArrayname.add("Shentong Multi Purpose");
        imagesArrayname.add("Press");
        imagesArrayname.add("Pigeon Kitchen Appliance");
        imagesArrayname.add("Philips Household Appliances");
        imagesArrayname.add("Heater");
        imagesArrayname.add("Juicer");

        ImagesAdapter imagesAdapter = new ImagesAdapter();
        gridView = (GridView) findViewById(R.id.grid_list_category);
        gridView.setAdapter(imagesAdapter);

        // To update the GridView Dynamically or in Runtime
        // imagesAdapter.notifyDataSetChanged();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selectedImage = (int) parent.getItemAtPosition(position);

                Intent fullScreenIntent = new Intent(HomeAppilance.this, FullGridView.class);
                fullScreenIntent.putExtra("IMAGE_PATH", selectedImage);
                startActivity(fullScreenIntent);
            }
        });

        //footer
        homef=(ImageView)findViewById(R.id.home_footer);
        homef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeAppilance.this,frontview.class);
                startActivity(intent);
            }
        });
        updatef=(ImageView)findViewById(R.id.update_footer);
        updatef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeAppilance.this, NewsUpdateActivity.class);
                startActivity(intent);

            }
        });
        logoutf=(ImageView)findViewById(R.id.logout_footer);
        logoutf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nf = cn.getActiveNetworkInfo();
                if (nf != null && nf.isConnected() == true) {
                    progressBar = ProgressDialog.show(HomeAppilance.this, "Please wait ...", "Logout processing ...", false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                            } catch (Exception e) {
                            }
                            session.logoutUser();
                            progressBar.dismiss();
                            HomeAppilance.this.finish();
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
                Intent intent=new Intent(HomeAppilance.this,ProfileActivity.class);
                startActivity(intent);

            }
        });


    }

    private class ImagesAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return imagesArray.size();
        }

        @Override
        public Object getItem(int position) {
            return imagesArray.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            View rootView = inflater.inflate(R.layout.grid_category_list, null);

            ImageView imageView = (ImageView) rootView.findViewById(R.id.imggrid);
            imageView.setImageResource(imagesArray.get(position));

            TextView textView = (TextView) rootView.findViewById(R.id.txtname);
            textView.setText(imagesArrayname.get(position));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(HomeAppilance.this, imagesArrayname.get(position), Toast.LENGTH_SHORT).show();
                }
            });

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