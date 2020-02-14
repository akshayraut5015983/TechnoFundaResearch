package com.app.technofunda.registration;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.app.technofunda.R;
import com.app.technofunda.ewallet.XMLParser;
import com.app.technofunda.util.Config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

public class PinRegistrationActivity extends AppCompatActivity
{
    String url,loginid,mobilenumber,pinnumber,xml;
    SharedPreferences pref;
    private DownloadData downloadData = new DownloadData();
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    ListView listpinreg;
    static final String KEY_NAMES = "pinname";
    static final String KEY_AMOUNT = "pinamount";
    static final String KEY_PINNUMBER = "pinno";

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_pin_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listpinreg = (ListView)findViewById(R.id.list_pin_registration);

        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {
            downloadData = new DownloadData();
            downloadData.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private  class DownloadData extends AsyncTask<String, Void, Document> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        Document doc = null;
        protected Document doInBackground(String... args) {
            pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
            if (pref.contains(Config.KEY_NAME)) {
                loginid=pref.getString(Config.KEY_NAME, "");
            }
            if (pref.contains(Config.KEY_MOBILE)) {
                mobilenumber=pref.getString(Config.KEY_MOBILE, "");
            }
            pinnumber="pin";
            url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(mobilenumber) + "&msg=" + String.valueOf(pinnumber) + "%20" + String.valueOf(loginid);
            try
            {
                XMLParser parser = new XMLParser();
                xml = parser.getXmlFromUrl(url);
                doc = parser.getDomElement(xml);
            }catch (Exception e) {
                Log.d("Exception", e.getMessage());
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {
            try {
                XMLParser parser = new XMLParser();
                if (doc == null) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(PinRegistrationActivity.this);
                    builder2.setTitle("PIN");
                    builder2.setMessage(Html.fromHtml(xml).toString().trim());
                    builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    });
                    builder2.show();
                }
                else {
                    NodeList nl = doc.getElementsByTagName(Config.KEY_SONG);
                    for (int i = 0; i < nl.getLength(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        Element e = (Element) nl.item(i);
                        map.put(KEY_NAMES, parser.getValue(e, KEY_NAMES));
                        map.put(KEY_AMOUNT, parser.getValue(e, KEY_AMOUNT));
                        map.put(KEY_PINNUMBER, parser.getValue(e, KEY_PINNUMBER));
                        dataList.add(map);
                    }
                    SingleMenuItemActivitys adapter = new SingleMenuItemActivitys(PinRegistrationActivity.this, dataList);
                    listpinreg.setAdapter(adapter);
                }
                }
                catch(Exception e) {
                    Log.d("Exception", e.getMessage());
                }
        }
    }
}