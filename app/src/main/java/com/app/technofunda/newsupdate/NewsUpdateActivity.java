package com.app.technofunda.newsupdate;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

import com.app.technofunda.R;
import com.app.technofunda.ethiqs.SessionManager;
import com.app.technofunda.ewallet.XMLParser;
import com.app.technofunda.profile.ProfileActivity;
import com.app.technofunda.ethiqs.frontview;
import com.app.technofunda.util.Config;

public class NewsUpdateActivity extends AppCompatActivity {

    SharedPreferences pref;
    String url,nuid1,nmbbno,newsupdate,xml;
    private ProgressDialog pDialog;
    private DownloadData downloadData = new DownloadData();
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    static final String KEY_NEWSID = "Newsid";
    static final String KEY_NEWSDATE = "newsdate";
    static final String KEY_NEWSTITLE = "NewsTitle";
    ListView listView;
    ImageView homef,updatef,logoutf,profilef;

    ProgressDialog progressBar;
    SessionManager session;
    String loginid,mobilenumber,passwords,s;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_update_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Logout
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

        listView = (ListView)findViewById(R.id.list_home_news);
        TextView tv = (TextView) this.findViewById(R.id.TextView03);
        tv.setSelected(true);

        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {
            downloadData = new DownloadData();
            downloadData.execute();
        }
        else {
            Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
        }

        //FOOTER
        homef=(ImageView)findViewById(R.id.home_footer);
        homef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NewsUpdateActivity.this,frontview.class);
                startActivity(intent);
            }
        });
//        updatef=(ImageView)findViewById(R.id.update_footer);
//        updatef.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(NewsUpdateActivity.this,NewsUpdateActivity.class);
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
                    progressBar = ProgressDialog.show(NewsUpdateActivity.this, "Please wait ...", "Logout processing ...", false);
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
                            NewsUpdateActivity.this.finish();
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
                Intent intent=new Intent(NewsUpdateActivity.this,ProfileActivity.class);
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

    private  class DownloadData extends AsyncTask<String, Void, Document> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewsUpdateActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        Document doc = null;
        protected Document doInBackground(String... args) {
            pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
            if (pref.contains(Config.KEY_NAME)) {
                nuid1=pref.getString(Config.KEY_NAME, "");
            }
            if (pref.contains(Config.KEY_MOBILE)) {
                nmbbno=pref.getString(Config.KEY_MOBILE, "");
            }
            newsupdate="news";
            url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(nmbbno) + "&msg=" +String.valueOf(newsupdate) + "%20" + String.valueOf(nuid1);
            try{
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
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(NewsUpdateActivity.this);
                    builder2.setTitle("News");
                    builder2.setMessage(Html.fromHtml(xml).toString().trim());
                    builder2.setPositiveButton("OK",new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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
                        map.put(KEY_NEWSID, parser.getValue(e, KEY_NEWSID));
                        map.put(KEY_NEWSDATE, parser.getValue(e, KEY_NEWSDATE));
                        map.put(KEY_NEWSTITLE, parser.getValue(e, KEY_NEWSTITLE));
                        dataList.add(map);
                    }
                }
                SingleMenuItemActivity3 adapter = new SingleMenuItemActivity3(NewsUpdateActivity.this, dataList);
                listView.setAdapter(adapter);
            }catch (Exception e) {
                Log.d("Exception", e.getMessage());
            }
        }
    }
}
