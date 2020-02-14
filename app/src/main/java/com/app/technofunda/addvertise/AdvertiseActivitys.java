package com.app.technofunda.addvertise;

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
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import com.app.technofunda.R;
import com.app.technofunda.ethiqs.ServiceHandler;
import com.app.technofunda.util.Config;

public class AdvertiseActivitys extends AppCompatActivity {
    EditText edit_search;
    ListView listViewadd;
    SharedPreferences pref;
    String aduid1,admbbno,advertisement,jsonStr;
    private ProgressDialog pDialog;
    private static String url;
    private static final String TAG_CONTACTS = "Advertise";
    public static final String TAG_ID = "Id";
    public static final String TAG_NAME = "Name";
    public static final String TAG_TYPE = "Type";
    public static final String TAG_DATE = "Date";
    JSONArray contacts = null;
    private CustemAdvertiseList adapter;
    ArrayList<HashMap<String, String>> usersList = new ArrayList<HashMap<String, String>>();
    ImageView homef,updatef,logoutf,profilef;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adverise_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listViewadd = (ListView)findViewById(R.id.lvstory_list);
        edit_search = (EditText)findViewById(R.id.inputSearch);

        pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            aduid1=pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            admbbno=pref.getString(Config.KEY_MOBILE, "");
        }

        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {
            advertisement="advertise";
            url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(admbbno) + "&msg=" +String.valueOf(advertisement) + "%20" + String.valueOf(aduid1);

            listViewadd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String TEST = ((TextView) view.findViewById(R.id.text_idno)).getText().toString();
                    Intent in = new Intent(getApplicationContext(), AdvertiseDetialsActivity.class);
                    in.putExtra("TAG_NAME", TEST);
                    startActivity(in);
                }
            });
            new GetContacts().execute();

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                adapter.getFilter().filter(cs);
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
            @Override
            public void afterTextChanged(Editable arg0) {}
        });
        }
        else {
            Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
        }
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AdvertiseActivitys.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    contacts = jsonObj.getJSONArray(TAG_CONTACTS);
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        HashMap<String, String> contact = new HashMap<String, String>();
                        contact.put(TAG_ID, c.getString(TAG_ID));
                        contact.put(TAG_NAME, c.getString(TAG_NAME));
                        contact.put(TAG_TYPE, c.getString(TAG_TYPE));
                        contact.put(TAG_DATE, c.getString(TAG_DATE));
                        usersList.add(contact);
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

            if (usersList.size() != 0) {
                adapter = new CustemAdvertiseList(getApplicationContext(), usersList);
                listViewadd.setAdapter(adapter);
            } else {
                listViewadd.setAdapter(null);
                alertPkgEmpty();
            }
        }
    }

    private void alertPkgEmpty() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Advertise");
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