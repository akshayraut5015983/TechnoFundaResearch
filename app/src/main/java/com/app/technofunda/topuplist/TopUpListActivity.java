package com.app.technofunda.topuplist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.app.technofunda.ethiqs.SessionManager;
import com.app.technofunda.ethiqs.ServiceHandler;
import com.app.technofunda.util.Config;

public class TopUpListActivity extends AppCompatActivity {

    ListView topuplist;
    String duid1,dmbbno,topup,rupee,jsonStr;
    private ProgressDialog pDialog;
    private static String url;
    private static final String TAG_CONTACTS = "TopupList";
    private static final String TAG_ID = "Id";
    private static final String TAG_NAME = "Name";
    private static final String TAG_DATE = "Date";
    private static final String TAG_AMOUNT = "Amount";
    private static final String TAG_PV = "PV";
    JSONArray contacts = null;
    ArrayList<HashMap<String, String>> addtopuplist;
    ImageView homef,updatef,logoutf,profilef;
    ProgressDialog progressBar;
    SessionManager session;
    SharedPreferences pref;
    String loginid,mobilenumber,passwords;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topuplist_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        topuplist = (ListView)findViewById(R.id.topup_listview);
        addtopuplist = new ArrayList<HashMap<String, String>>();

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
            topup = "topuplist";
            url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(dmbbno) + "&msg=" + String.valueOf(topup) + "%20" + String.valueOf(duid1);
            new GetContacts().execute();
        }else {
            Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
        }
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TopUpListActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();
            jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            rupee = getResources().getString(R.string.Rs);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    contacts = jsonObj.getJSONArray(TAG_CONTACTS);
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        HashMap<String, String> contact = new HashMap<String, String>();
                        contact.put(TAG_ID, c.getString(TAG_ID));
                        contact.put(TAG_NAME, c.getString(TAG_NAME));
                        contact.put(TAG_DATE, c.getString(TAG_DATE));
                        contact.put(TAG_AMOUNT, rupee+" "+c.getString(TAG_AMOUNT));
                        contact.put(TAG_PV, c.getString(TAG_PV));
                        addtopuplist.add(contact);
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

            if(addtopuplist.size()!=0) {
                ListAdapter adapter = new SimpleAdapter(TopUpListActivity.this, addtopuplist,
                        R.layout.topup_list_row, new String[]{TAG_ID, TAG_NAME, TAG_DATE, TAG_AMOUNT, TAG_PV}, new int[]
                        {R.id.txt_topup_id, R.id.txt_topup_name, R.id.txt_topup_date, R.id.txt_topup_amount, R.id.txt_topup_pv});
                topuplist.setAdapter(adapter);
            }else{
                alertPkgEmpty();
            }
        }
    }
    private void alertPkgEmpty(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Topup List");
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
