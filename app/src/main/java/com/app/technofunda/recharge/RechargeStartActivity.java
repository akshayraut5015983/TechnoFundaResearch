package com.app.technofunda.recharge;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.technofunda.R;
import com.app.technofunda.ethiqs.SessionManager;
import com.app.technofunda.profile.ProfileActivity;
import com.app.technofunda.ethiqs.frontview;
import com.app.technofunda.util.Config;

public class RechargeStartActivity extends AppCompatActivity {

    TextView textfirst,rechargelist,makerecharge,textfrth,textfith;
    View first,forth,fifth;
    ImageView homef,updatef,logoutf,profilef;
    ProgressDialog progressBar;
    SessionManager session;
    SharedPreferences pref;
    String loginid, mobilenumber, passwords, s;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_start_activity);
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Typeface pacificoFont = Typeface.createFromAsset(getAssets(), "arialnarrow.ttf");

        first=(View)findViewById(R.id.view_first);
        first.setVisibility(View.INVISIBLE);
        forth=(View)findViewById(R.id.view_frouth);
        forth.setVisibility(View.INVISIBLE);
        fifth=(View)findViewById(R.id.view_fifth);
        fifth.setVisibility(View.INVISIBLE);

        textfirst=(TextView)findViewById(R.id.text_view_f);
        textfirst.setVisibility(View.INVISIBLE);

        textfrth=(TextView)findViewById(R.id.text_view_fr);
        textfrth.setVisibility(View.INVISIBLE);

        textfith=(TextView)findViewById(R.id.text_view_fith);
        textfith.setVisibility(View.INVISIBLE);

//        makerecharge=(TextView)findViewById(R.id.text_view_s);
//        makerecharge.setText("Make Recharge");
//        makerecharge.setTypeface(pacificoFont);

        rechargelist=(TextView)findViewById(R.id.text_view_t);
        rechargelist.setText("Recharge History");
        rechargelist.setTypeface(pacificoFont);

        makerecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MultiRechargeActivity.class);
                startActivity(i);
            }
        });
        rechargelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RechargeListActivity.class);
                startActivity(i);
            }
        });

        //footer
        homef=(ImageView)findViewById(R.id.home_footer);
        homef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RechargeStartActivity.this,frontview.class);
                startActivity(intent);
            }
        });
//        updatef=(ImageView)findViewById(R.id.update_footer);
//        updatef.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(RechargeStartActivity.this,NewsUpdateActivity.class);
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
                    progressBar = ProgressDialog.show(RechargeStartActivity.this, "Please wait ...", "Logout processing ...", false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                            } catch (Exception e) {
                            }
                            session.logoutUser();
                            progressBar.dismiss();
                            RechargeStartActivity.this.finish();
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
                Intent intent=new Intent(RechargeStartActivity.this,ProfileActivity.class);
                startActivity(intent);

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


