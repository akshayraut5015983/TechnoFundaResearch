package com.app.technofunda.addvertise;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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

import com.app.technofunda.R;
import com.app.technofunda.ethiqs.Validate;
import com.app.technofunda.util.Config;

public class AdvertiseDetialsActivity extends AppCompatActivity {

    private DownloadData downloadData = new DownloadData();
    ImageLoader imageLoader;
    Context context;
    SharedPreferences pref;
    String url,urls, addetialsuid, addetialmbno, adetials, addno, addcomment, addcomments, rateus, comments;
    String rupee,xml;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    static final String KEY_CPNAME = "compName";
    static final String KEY_CPTYPE = "compType";
    static final String KEY_CPDESC = "compdesc";
    static final String FLAG = "advImage";
    static final String KEY_CMRP = "mrp";
    String FLAG1, FLAG2;
    private ProgressDialog pDialog;
    TextView cname, ctype, cdesc,cmrp;
    ImageView addimage;
    Button subadvertise;
    EditText addcomm;
    RatingBar ratingBar;
    ImageView homef,updatef,logoutf,profilef;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_detials_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageLoader = new ImageLoader(context);
        subadvertise = (Button) findViewById(R.id.but_submit);
        addcomm = (EditText) findViewById(R.id.edit_add_comment);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        cname = (TextView) findViewById(R.id.tex_cname);
        ctype = (TextView) findViewById(R.id.tex_ctype);
        cdesc = (TextView) findViewById(R.id.tex_cdesc);
        cmrp = (TextView) findViewById(R.id.tex_cmrp);
        addimage = (ImageView) findViewById(R.id.image_add);
        addimage.setClickable(true);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                addno = null;
            } else {
                addno = extras.getString("TAG_NAME");
                Log.e("Adv No: ", addno);
            }
        } else {
            addno = (String) savedInstanceState.getSerializable("TAG_NAME");
        }
        pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            addetialsuid = pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            addetialmbno = pref.getString(Config.KEY_MOBILE, "");
        }

        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {
            adetials = "adv";
            url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(addetialmbno) + "&msg=" + String.valueOf(adetials) + "%20" + String.valueOf(addetialsuid) + "%20" + String.valueOf(addno);
            downloadData = new DownloadData();
            downloadData.execute();

            subadvertise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addcomment = addcomm.getText().toString().trim();
                    rateus = String.valueOf(ratingBar.getRating());

                    boolean check = true;
                    Validate validate = new Validate();

                    if (validate.isNotEmpty(addcomment)) {
                        addcomment = addcomm.getText().toString();
                        addcomments = addcomment.replaceAll(" ", "-");
                    } else {
                        addcomment = "";
                        check = false;
                        addcomm.setError("Please add your comment");
                    }
                    if (check) {
                        comments = "comment";
                        urls = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(addetialmbno) + "&msg=" + String.valueOf(comments) + "%20" + String.valueOf(addetialsuid) + "%20" + String.valueOf(addno) + "%20" + String.valueOf(rateus) + "%20" + String.valueOf(addcomments);
                        DownloadWebPageTask task1 = new DownloadWebPageTask();
                        task1.execute(new String[]{String.valueOf(urls)});
                        pDialog = new ProgressDialog(AdvertiseDetialsActivity.this);
                        pDialog.setMessage("Please wait...");
                        pDialog.setCancelable(false);
                        pDialog.show();
                    }
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
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

    private class DownloadData extends AsyncTask<String, Void, Document> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AdvertiseDetialsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        Document doc = null;

        protected Document doInBackground(String... args) {
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
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(AdvertiseDetialsActivity.this);
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
                        rupee = getResources().getString(R.string.Rs);
                        HashMap<String, String> map = new HashMap<String, String>();
                        Element e = (Element) nl.item(i);
                        cname.setText(parser.getValue(e, KEY_CPNAME));
                        ctype.setText(parser.getValue(e, KEY_CPTYPE));
                        cdesc.setText(parser.getValue(e, KEY_CPDESC));
                        FLAG1 = parser.getValue(e, FLAG);
                        FLAG2 = FLAG1.replaceAll("~", Config.URL);
                        imageLoader.DisplayImage(FLAG2, addimage);
                        cmrp.setText(rupee+" "+parser.getValue(e, KEY_CMRP));
                    }
                }
            } catch (Exception e) {
                Log.d("Exception", e.getMessage());
            }
        }
    }

    class DownloadWebPageTask extends AsyncTask<String, Void, String> {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response;
        }

        protected void onPostExecute(String result) {

            String s = Html.fromHtml(result).toString();
            if (pDialog.isShowing())
                pDialog.dismiss();

            AlertDialog.Builder builder2 = new AlertDialog.Builder(AdvertiseDetialsActivity.this);
            builder2.setTitle("Advertise");
            builder2.setMessage(String.valueOf(s).toString().trim());
            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });
            builder2.show();
        }
    }
}
