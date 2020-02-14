package com.app.technofunda.recharge;

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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import com.app.technofunda.R;
import com.app.technofunda.profile.ProfileActivity;
import com.app.technofunda.ethiqs.ServiceHandler;
import com.app.technofunda.ethiqs.frontview;
import com.app.technofunda.util.Config;

public class RechargeListActivity extends AppCompatActivity {
    ListView rechargetlist;
    SharedPreferences pref;
    String duid1,dmbbno,recharge,rupee,jsonStr;
    private ProgressDialog pDialog;
    private static String url;
    private static final String TAG_CONTACTS = "Recharge";
    private static final String TAG_ID = "Id";
    private static final String TAG_RECHNO = "RchNo";
    private static final String TAG_OPERATOR = "Operator";
    private static final String TAG_AMOUNT = "Amount";
    private static final String TAG_STATUS = "Status";
    JSONArray contacts = null;
    ArrayList<HashMap<String, String>> addrechargelist;
    ImageView homef,updatef,logoutf,profilef;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge_list_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rechargetlist = (ListView)findViewById(R.id.recharge_listview);
        addrechargelist = new ArrayList<HashMap<String, String>>();

        pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            duid1=pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            dmbbno=pref.getString(Config.KEY_MOBILE, "");
        }
        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {
            recharge = "rechargelist";
            url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(dmbbno) + "&msg=" + String.valueOf(recharge) + "%20" + String.valueOf(duid1);
            new GetContacts().execute();
        }else {
            Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
        }
        //FOOTER
        homef=(ImageView)findViewById(R.id.home_footer);
        homef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RechargeListActivity.this,frontview.class);
                startActivity(intent);
            }
        });
//        updatef=(ImageView)findViewById(R.id.update_footer);
//        updatef.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(RechargeListActivity.this,frontview.class);
//                startActivity(intent);
//
//            }
//        });
        logoutf=(ImageView)findViewById(R.id.logout_footer);
        logoutf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(RechargeListActivity.this);
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

                android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        profilef=(ImageView)findViewById(R.id.profile_footer);
        profilef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RechargeListActivity.this,ProfileActivity.class);
                startActivity(intent);

            }
        });


    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RechargeListActivity.this);
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
                        contact.put(TAG_ID, c.getString(TAG_ID));
                        contact.put(TAG_RECHNO, c.getString(TAG_RECHNO));
                        contact.put(TAG_OPERATOR, c.getString(TAG_OPERATOR));
                        contact.put(TAG_AMOUNT, rupee+" "+c.getString(TAG_AMOUNT));
                        contact.put(TAG_STATUS,c.getString(TAG_STATUS));
                        addrechargelist.add(contact);
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
            if(addrechargelist.size()!=0) {
                ListAdapter adapter = new SimpleAdapter(RechargeListActivity.this, addrechargelist,
                R.layout.recharge_list_row, new String[] { TAG_ID,TAG_RECHNO, TAG_OPERATOR,TAG_AMOUNT,TAG_STATUS}, new int[]
                {R.id.id_number, R.id.recharge_mobile, R.id.recharge_operator,R.id.recharge_amount, R.id.rcharge_status});
                rechargetlist.setAdapter(adapter);
            }else{
                alertPkgEmpty();
            }
        }
    }

    private void alertPkgEmpty(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Recharge List");
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
