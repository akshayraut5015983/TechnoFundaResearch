package com.app.technofunda.newsupdate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

import com.app.technofunda.R;
import com.app.technofunda.addvertise.ImageLoader;
import com.app.technofunda.addvertise.TouchImageView;
import com.app.technofunda.addvertise.XMLParser;
import com.app.technofunda.util.Config;

public class NewsUpdateDetialsActivity extends AppCompatActivity {

    private DownloadData downloadData = new DownloadData();
    ImageLoader imageLoader;
    Context context;
    SharedPreferences pref;
    String url, newsupdatelogid, newsupdatelogmobile, newsupdatedetials,addno,xml;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    static final String KEY_NEWSTITLE = "newstitle";
    static final String KEY_NEWSDESC = "description";
    static final String FLAG = "photos";
    String FLAG1, FLAG2;
    TextView newsupdatetitle, newsupdatedesc;
    ImageView Newsupdateimage;
    TouchImageView img;
    private ProgressDialog pDialog;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_update_detials_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageLoader = new ImageLoader(context);
        img = new TouchImageView(this);
        newsupdatetitle = (TextView) findViewById(R.id.news_detials_title);
        newsupdatedesc = (TextView) findViewById(R.id.news_update_desc);
        Newsupdateimage = (ImageView) findViewById(R.id.news_detials_image);
        Newsupdateimage.setClickable(true);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                addno = null;
            } else {
                addno = extras.getString("STRING_I_NEED");
            }
        } else {
            addno = (String) savedInstanceState.getSerializable("STRING_I_NEED");
        }
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

    private class DownloadData extends AsyncTask<String, Void, Document> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewsUpdateDetialsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        Document doc = null;
        protected Document doInBackground(String... args) {

            pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
            if (pref.contains(Config.KEY_NAME)) {
                newsupdatelogid = pref.getString(Config.KEY_NAME, "");
            }
            if (pref.contains(Config.KEY_MOBILE)) {
                newsupdatelogmobile = pref.getString(Config.KEY_MOBILE, "");
            }
            newsupdatedetials = "nws";
            url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(newsupdatelogmobile) + "&msg=" + String.valueOf(newsupdatedetials) + "%20" + String.valueOf(newsupdatelogid) + "%20" + String.valueOf(addno);
            try {
                XMLParser parser = new XMLParser();
                xml = parser.getXmlFromUrl(url);
                doc = parser.getDomElement(xml);
            } catch (Exception e) {
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
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(NewsUpdateDetialsActivity.this);
                    builder2.setTitle("News");
                    builder2.setMessage(Html.fromHtml(xml).toString().trim());
                    builder2.setPositiveButton("OK",new DialogInterface.OnClickListener() {
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
                        newsupdatetitle.setText(parser.getValue(e, KEY_NEWSTITLE));
                        newsupdatedesc.setText(parser.getValue(e, KEY_NEWSDESC));
                        FLAG1 = parser.getValue(e, FLAG);
                        FLAG2 = FLAG1.replaceAll("~", Config.URL);
                        imageLoader.DisplayImage(FLAG2, Newsupdateimage);
                    }
                }
            }catch (Exception e) {
                Log.d("Exception", e.getMessage());
            }
        }
    }
}
