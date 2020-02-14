package com.app.technofunda.topuplist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.technofunda.R;
import com.app.technofunda.ethiqs.SessionManager;
import com.app.technofunda.ethiqs.frontview;
import com.app.technofunda.newsupdate.NewsUpdateActivity;
import com.app.technofunda.profile.ProfileActivity;
import com.app.technofunda.util.Config;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TopupStartActivity extends AppCompatActivity {

    TextView textviewf,topupid,topuplist,textviewfrt,textviewfth;
    View first,forth,fifth;
    Typeface pacificoFont;
    String duid1,dmbbno,keysuper,s;
    private ProgressDialog pDialog;
    ImageView homef,updatef,logoutf,profilef;
    SessionManager session;
    SharedPreferences pref;
    ProgressDialog progressBar;
    String loginid,mobilenumber,passwords;



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_start_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pacificoFont = Typeface.createFromAsset(getAssets(), "arialnarrow.ttf");

        pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            duid1=pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            dmbbno=pref.getString(Config.KEY_MOBILE, "");
        }
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

        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {

            keysuper = "superseller";
            String urls = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(dmbbno) + "&msg=" + String.valueOf(keysuper) + "%20" + String.valueOf(duid1);
            DownloadWebPageTask task1 = new DownloadWebPageTask();
            task1.execute(new String[]{String.valueOf(urls)});
            pDialog = new ProgressDialog(TopupStartActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

            first=(View)findViewById(R.id.view_first);
            first.setVisibility(View.INVISIBLE);
            forth=(View)findViewById(R.id.view_frouth);
            forth.setVisibility(View.INVISIBLE);
            fifth=(View)findViewById(R.id.view_fifth);
            fifth.setVisibility(View.INVISIBLE);

            textviewf=(TextView)findViewById(R.id.text_view_f);
            textviewf.setVisibility(View.INVISIBLE);
            textviewfrt=(TextView)findViewById(R.id.text_view_fr);
            textviewfrt.setVisibility(View.INVISIBLE);
            textviewfth=(TextView)findViewById(R.id.text_view_fith);
            textviewfth.setVisibility(View.INVISIBLE);

//            topupid=(TextView)findViewById(R.id.text_view_s);
//            topupid.setText("Topup Id");
//            topupid.setTypeface(pacificoFont);

            topuplist=(TextView)findViewById(R.id.text_view_t);
             topuplist.setText("Topup Id List");
            topuplist.setTypeface(pacificoFont);

//            topupid.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(getApplicationContext(), TopupidActivity.class);
//                    startActivity(i);
//                }
//            });

            topuplist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), TopUpListActivity.class);
                    startActivity(i);
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
        }

        //footer
        homef=(ImageView)findViewById(R.id.home_footer);
        homef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TopupStartActivity.this,frontview.class);
                startActivity(intent);
            }
        });
        updatef=(ImageView)findViewById(R.id.update_footer);
        updatef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TopupStartActivity.this, NewsUpdateActivity.class);
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
                    progressBar = ProgressDialog.show(TopupStartActivity.this, "Please wait ...", "Logout processing ...", false);
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                            } catch (Exception e) {
                            }
                            session.logoutUser();
                            progressBar.dismiss();
                            TopupStartActivity.this.finish();
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
                Intent intent=new Intent(TopupStartActivity.this,ProfileActivity.class);
                startActivity(intent);

            }
        });
        String urls = Config.URL+"/api/apilogin.aspx?uid=" + String.valueOf(loginid) + "&pass=" + String.valueOf(passwords) + "&mbl=" + String.valueOf(mobilenumber);
        DownloadWebPageTask task2 = new DownloadWebPageTask();
        task2.execute(new String[]{String.valueOf(urls)});

    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response;
        }

        protected void onPostExecute(String result) {
            if (pDialog.isShowing())
                pDialog.dismiss();

            s = Html.fromHtml(result).toString();
            if (s.contains("You are not Super Seller")) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(TopupStartActivity.this);
                builder1.setTitle("Topup");
                builder1.setMessage(Html.fromHtml(s).toString().trim());
                builder1.setCancelable(false);
                builder1.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
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
