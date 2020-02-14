package com.app.technofunda.myincome;

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

public class IncomeStartActivity extends AppCompatActivity {
    TextView binary,direct,referral,dgeneration,history;
    ImageView homef,updatef,logoutf,profilef;
    ProgressDialog progressBar;
    SessionManager session;
    SharedPreferences pref;
    String loginid, mobilenumber, passwords, s;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_start_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Logout Session
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

        Typeface pacificoFont = Typeface.createFromAsset(getAssets(), "arialnarrow.ttf");

        binary=(TextView)findViewById(R.id.text_view_f);
        binary.setText("Binary Income");
        binary.setTypeface(pacificoFont);

//        direct=(TextView)findViewById(R.id.text_view_s);
//        direct.setText("Direct Income");
//        direct.setTypeface(pacificoFont);

        referral=(TextView)findViewById(R.id.text_view_t);
        referral.setText("Referral Income");
        referral.setTypeface(pacificoFont);

        dgeneration=(TextView)findViewById(R.id.text_view_fr);
        dgeneration.setText("SS Income");
        dgeneration.setTypeface(pacificoFont);

        history=(TextView)findViewById(R.id.text_view_fith);
        history.setText("Income History");
        history.setTypeface(pacificoFont);

        binary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BinaryIncomeActivity.class);
                startActivity(i);
            }
        });

        direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DirectIncomeActivity.class);
                startActivity(i);
            }
        });

        referral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ReferralIncomeActivity.class);
                startActivity(i);
            }
        });

        dgeneration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SSIncomeActivity.class);
                startActivity(i);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), IncomeHistoryActivity.class);
                startActivity(i);
            }
        });

        //footer
        homef=(ImageView)findViewById(R.id.home_footer);
        homef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(IncomeStartActivity.this,frontview.class);
                startActivity(intent);
            }
        });
//        updatef=(ImageView)findViewById(R.id.update_footer);
//        updatef.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(IncomeStartActivity.this,NewsUpdateActivity.class);
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
                    progressBar = ProgressDialog.show(IncomeStartActivity.this, "Please wait ...", "Logout processing ...", false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                            } catch (Exception e) {
                            }
                            session.logoutUser();
                            progressBar.dismiss();
                            IncomeStartActivity.this.finish();
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
                Intent intent=new Intent(IncomeStartActivity.this,ProfileActivity.class);
                startActivity(intent);

            }
        });


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
