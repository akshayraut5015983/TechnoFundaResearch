package com.app.technofunda.ewallet;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.technofunda.R;
import com.app.technofunda.ethiqs.frontview;
import com.app.technofunda.newsupdate.NewsUpdateActivity;
import com.app.technofunda.profile.ProfileActivity;
import com.app.technofunda.util.Config;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class PointEwallet extends AppCompatActivity {

    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    static final String KEY_TRANSID = "TxnId";
    static final String KEY_DATE = "date";
    static final String KEY_DEBIT = "Debit";
    static final String KEY_CREDIT = "Credit";
    static final String KEY_REMARK = "remark";
    ListView listView;
    private DownloadData downloadData = new DownloadData();
    String url,uid1, ewallet1,mbbno,xml;
    private ProgressDialog pDialog;
    TextView txtewamt;
    SharedPreferences pref;
    Animation animBlink;
    ImageView homef,updatef,logoutf,profilef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ewallets_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtewamt=(TextView)findViewById(R.id.txt_ewallet_amt);
        listView = (ListView)findViewById(R.id.listHome);

        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {
            pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
            if (pref.contains(Config.KEY_NAME)) {
                uid1=pref.getString(Config.KEY_NAME, "");
                Toast.makeText(PointEwallet.this,"uid1:"+uid1,Toast.LENGTH_LONG).show();
            }
            if (pref.contains(Config.KEY_MOBILE)) {
                mbbno=pref.getString(Config.KEY_MOBILE, "");
            }
            ewallet1="CpB";
            url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(mbbno) + "&msg=" +String.valueOf(ewallet1) + "%20" + String.valueOf(uid1);
            DownloadWebPageTask task1 = new DownloadWebPageTask();
            task1.execute(new String[]{String.valueOf(url)});
            transactionDel();
        } else {
            Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
        }
        homef=(ImageView)findViewById(R.id.home_footer);
        homef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PointEwallet.this,frontview.class);
                startActivity(intent);
            }
        });
        updatef=(ImageView)findViewById(R.id.update_footer);
        updatef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PointEwallet.this, NewsUpdateActivity.class);
                startActivity(intent);

            }
        });
        logoutf=(ImageView)findViewById(R.id.logout_footer);
        logoutf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PointEwallet.this);
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

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        profilef=(ImageView)findViewById(R.id.profile_footer);
        profilef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PointEwallet.this,ProfileActivity.class);
                startActivity(intent);

            }
        });


    }
    public void transactionDel() {
        downloadData = new DownloadData();
        downloadData.execute();

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

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
            try
            {
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

        @Override
        protected void onPostExecute(String result) {
            String s = Html.fromHtml(result).toString();
            if (s.contains("Current Point Ewallet")) {
                animBlink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                txtewamt.setText(s);
                txtewamt.setVisibility(View.VISIBLE);
                txtewamt.startAnimation(animBlink);
            }
            else {
            }
        }
    }

    private  class DownloadData extends AsyncTask<String, Void, Document> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PointEwallet.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        Document doc = null;
        protected Document doInBackground(String... args) {
            ewallet1="cpt";
            url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(mbbno) + "&msg=" +String.valueOf(ewallet1) + "%20" + String.valueOf(uid1);
            try {
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
            if (pDialog.isShowing())
                pDialog.dismiss();
            try {
                XMLParser parser = new XMLParser();
                if (doc==null) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(PointEwallet.this);
                    builder2.setTitle("CT");
                    builder2.setMessage(Html.fromHtml(xml).toString().trim());
                    builder2.setPositiveButton("OK",new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.cancel();
                        }
                    });
                    builder2.show();
                }
                else {
                    NodeList nl = doc.getElementsByTagName(Config.KEY_SONG);
                    for (int i = 0; i < nl.getLength(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        Element e = (Element) nl.item(i);
                        map.put(KEY_TRANSID, parser.getValue(e, KEY_TRANSID));
                        map.put(KEY_DATE, parser.getValue(e, KEY_DATE));
                        map.put(KEY_DEBIT, parser.getValue(e, KEY_DEBIT));
                        map.put(KEY_CREDIT, parser.getValue(e, KEY_CREDIT));
                        map.put(KEY_REMARK, parser.getValue(e, KEY_REMARK));
                        dataList.add(map);
                    }
                }
                SingleMenuItemActivity adapter = new SingleMenuItemActivity(PointEwallet.this, dataList);
                listView.setAdapter(adapter);
            }catch (Exception e) {
                Log.d("Exception", e.getMessage());
            }
        }
    }
}

