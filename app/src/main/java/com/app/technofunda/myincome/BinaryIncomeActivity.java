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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.app.technofunda.R;
import com.app.technofunda.ethiqs.SessionManager;
import com.app.technofunda.profile.ProfileActivity;
import com.app.technofunda.ethiqs.ServiceHandler;
import com.app.technofunda.ethiqs.frontview;
import com.app.technofunda.util.Config;

public class BinaryIncomeActivity extends AppCompatActivity {
    Spinner selectdate;
    List<String> select_date;
    String url,urlbin,sedate,duid1,dmbbno,getdate,dates,jsonStr,rupee;
    private static final String TAG_BINARY = "Binary Income";
    private static final String TAG_LOGINID= "Loginid";
    private static final String TAG_MATCHP = "Match Pair";
    private static final String TAG_AMOUNT = "Amount";
    private static final String TAG_PAYBLEP = "Payble Pair";
    private static final String TAG_PAYBLEA = "Payble Amount";
    private static final String TAG_TDS= "Tds";
    private static final String TAG_ADMIN = "Admin";
    private static final String TAG_NETPAY = "Netpay";
    private static final String TAG_CONTACTS = "BinaryDate";
    private static final String TAG_DATE = "Column1";
    JSONArray contacts = null;
    private ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> addbinarylist;
    ListView binarylist;
    ImageView homef,updatef,logoutf,profilef;
    ProgressDialog progressBar;
    SessionManager session;
    SharedPreferences pref;
    String loginid, mobilenumber, passwords, s;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.binary_income_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binarylist = (ListView)findViewById(R.id.binary_listview);
        selectdate = (Spinner) findViewById(R.id.date_groups);
        select_date = new ArrayList<String>();
        addbinarylist = new ArrayList<HashMap<String, String>>();

        pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            duid1=pref.getString(Config.KEY_NAME, "");
        }if (pref.contains(Config.KEY_MOBILE)) {
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
            sedate = "binddate";
            url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(dmbbno) + "&msg=" + String.valueOf(sedate) + "%20" + String.valueOf(duid1);
            new GetContacts().execute();
            select_date.add("Select Date");
            ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(BinaryIncomeActivity.this, android.R.layout.simple_spinner_item,select_date);
            stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selectdate.setAdapter(stateAdapter);

            selectdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {

                    addbinarylist.clear();

                    getdate = selectdate.getSelectedItem().toString().trim();
                    dates=getdate.replaceAll(" ","-");
                    String bincome="binaryincome";
                    if(getdate.equals("Select Date")){
                        addbinarylist.clear();
                        Toast.makeText(BinaryIncomeActivity.this,"Please Select Date",Toast.LENGTH_LONG);
                    }else {
                        urlbin = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(dmbbno) + "&msg=" + String.valueOf(bincome) + "%20" + String.valueOf(duid1)+ "%20" + String.valueOf(dates);
                        new GetBinaryIncome().execute();
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {}
            });
        }else {
            Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
        }

        //footer
        homef=(ImageView)findViewById(R.id.home_footer);
        homef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BinaryIncomeActivity.this,frontview.class);
                startActivity(intent);
            }
        });
//        updatef=(ImageView)findViewById(R.id.update_footer);
//        updatef.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(BinaryIncomeActivity.this,NewsUpdateActivity.class);
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
                    progressBar = ProgressDialog.show(BinaryIncomeActivity.this, "Please wait ...", "Logout processing ...", false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                            } catch (Exception e) {
                            }
                            session.logoutUser();
                            progressBar.dismiss();
                            BinaryIncomeActivity.this.finish();
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
                Intent intent=new Intent(BinaryIncomeActivity.this,ProfileActivity.class);
                startActivity(intent);

            }
        });


    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BinaryIncomeActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            Log.d("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    contacts = jsonObj.getJSONArray(TAG_CONTACTS);
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        select_date.add(c.getString(TAG_DATE));
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
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }

    private class GetBinaryIncome extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BinaryIncomeActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            jsonStr = sh.makeServiceCall(urlbin, ServiceHandler.GET);
            Log.d("Response: ", "> " + jsonStr);
            rupee = getResources().getString(R.string.Rs);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    contacts = jsonObj.getJSONArray(TAG_BINARY);
                    for (int i = 0; i < contacts.length(); i++) {

                        JSONObject c = contacts.getJSONObject(i);
                        HashMap<String, String> contact = new HashMap<String, String>();

                        contact.put(TAG_LOGINID, c.getString(TAG_LOGINID));
                        contact.put(TAG_MATCHP, c.getString(TAG_MATCHP));
                        contact.put(TAG_AMOUNT, c.getString(TAG_AMOUNT));
                        contact.put(TAG_PAYBLEP, c.getString(TAG_PAYBLEP));
                        contact.put(TAG_PAYBLEA,c.getString(TAG_PAYBLEA));
                        contact.put(TAG_TDS, c.getString(TAG_TDS));
                        contact.put(TAG_ADMIN, c.getString(TAG_ADMIN));
                        contact.put(TAG_NETPAY, c.getString(TAG_NETPAY));
                        addbinarylist.add(contact);
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

            if(addbinarylist.size()!=0) {
                ListAdapter adapter = new SimpleAdapter(BinaryIncomeActivity.this, addbinarylist,
                 R.layout.binary_income_row_list, new String[]{TAG_LOGINID, TAG_MATCHP, TAG_AMOUNT,
                 TAG_PAYBLEP, TAG_PAYBLEA, TAG_TDS,TAG_ADMIN,TAG_NETPAY}, new int[]
                 {R.id.txt_biogid, R.id.txt_bmatch, R.id.txt_bamnt,R.id.txt_bpaybal, R.id.txt_bpayamt,
                  R.id.txt_btds,R.id.txt_badmin,R.id.txt_bnetpay});
                binarylist.setAdapter(adapter);
            }else{
                alertPkgEmpty();
            }
        }
    }

    private void alertPkgEmpty(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Binary Income");
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
