package com.app.technofunda.myincome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import com.app.technofunda.R;
import com.app.technofunda.ethiqs.SessionManager;
import com.app.technofunda.profile.ProfileActivity;
import com.app.technofunda.ethiqs.ServiceHandler;
import com.app.technofunda.ethiqs.frontview;
import com.app.technofunda.util.Config;

public class DirectIncomeActivity extends AppCompatActivity {
    public double d=0,h=0,n=0,a=0,b,g,f,m;
    TextView totamnt,tottds,totadmin,totnetpay;
    ListView regisinlist;
    String duid1,dmbbno,regisin,rupee,jsonStr;
    private ProgressDialog pDialog;
    private static String url;
    private static final String TAG_CONTACTS = "Direct Income";
    private static final String TAG_DIRECTID = "Direct Id";
    private static final String TAG_DIRECTCNT = "Direct Count";
    private static final String TAG_INCOME = "Income";
    private static final String TAG_TDS = "TDS";
    private static final String TAG_ADMIN = "Admin";
    private static final String TAG_NETPAY = "Netpay";
    JSONArray contacts = null;
    ArrayList<HashMap<String, String>> addregislist;
    ImageView homef,updatef,logoutf,profilef;
    ProgressDialog progressBar;
    SessionManager session;
    SharedPreferences pref;
    String loginid, mobilenumber, passwords, s;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.direct_income_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        totamnt=(TextView)findViewById(R.id.tex_totamnt);
        tottds=(TextView)findViewById(R.id.tex_tottds);
        totadmin=(TextView)findViewById(R.id.tex_totadmin);
        totnetpay=(TextView)findViewById(R.id.tex_totnet);

        regisinlist = (ListView)findViewById(R.id.regisincome_listview);
        addregislist = new ArrayList<HashMap<String, String>>();

        pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            duid1=pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            dmbbno=pref.getString(Config.KEY_MOBILE, "");
        }
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
        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {
            regisin = "directincome";
            url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(dmbbno) + "&msg=" + String.valueOf(regisin) + "%20" + String.valueOf(duid1);
            new GetContacts().execute();
        }else {
            Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
        }

        //footer
        homef=(ImageView)findViewById(R.id.home_footer);
        homef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DirectIncomeActivity.this,frontview.class);
                startActivity(intent);
            }
        });
//        updatef=(ImageView)findViewById(R.id.update_footer);
//        updatef.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(DirectIncomeActivity.this,NewsUpdateActivity.class);
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
                    progressBar = ProgressDialog.show(DirectIncomeActivity.this, "Please wait ...", "Logout processing ...", false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                            } catch (Exception e) {
                            }
                            session.logoutUser();
                            progressBar.dismiss();
                            DirectIncomeActivity.this.finish();
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
                Intent intent=new Intent(DirectIncomeActivity.this,ProfileActivity.class);
                startActivity(intent);

            }
        });


    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DirectIncomeActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            Log.d("Response: ", "> " + jsonStr);
            rupee = getResources().getString(R.string.Rs);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    contacts = jsonObj.getJSONArray(TAG_CONTACTS);
                    for (int i = 0; i < contacts.length(); i++) {

                        JSONObject c = contacts.getJSONObject(i);
                        HashMap<String, String> contact = new HashMap<String, String>();

                        contact.put(TAG_DIRECTID, c.getString(TAG_DIRECTID));
                        contact.put(TAG_DIRECTCNT, c.getString(TAG_DIRECTCNT));
                        contact.put(TAG_INCOME, c.getString(TAG_INCOME));
                        contact.put(TAG_TDS, c.getString(TAG_TDS));
                        contact.put(TAG_ADMIN,c.getString(TAG_ADMIN));
                        contact.put(TAG_NETPAY, c.getString(TAG_NETPAY));

                        addregislist.add(contact);

                        b = Double.parseDouble(c.getString(TAG_INCOME).toString());
                        d = d + b;

                        g= Double.parseDouble(c.getString(TAG_TDS).toString());
                        h = h + g;

                        f = Double.parseDouble(c.getString(TAG_ADMIN).toString());
                        a = a + f;

                        m = Double.parseDouble(c.getString(TAG_NETPAY).toString());
                        n = n + m;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if(addregislist.size()!=0) {
                ListAdapter adapter = new SimpleAdapter(DirectIncomeActivity.this, addregislist,
                        R.layout.direct_income_list_row, new String[]{TAG_DIRECTID, TAG_DIRECTCNT, TAG_INCOME,
                        TAG_TDS, TAG_ADMIN, TAG_NETPAY}, new int[]
                        { R.id.txt_level, R.id.txt_countsid, R.id.txt_pv,R.id.txt_tds, R.id.txt_admin,
                                R.id.txt_netpay});

                regisinlist.setAdapter(adapter);
                totamnt.setText(String.valueOf(d));
                tottds.setText(String.valueOf(h));
                totadmin.setText(String.valueOf(a));
                totnetpay.setText(String.valueOf(n));
            }else{
                alertPkgEmpty();
            }
        }
    }

    private void alertPkgEmpty(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Direct Income");
        builder1.setMessage(Html.fromHtml(jsonStr).toString().trim());
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