package com.app.technofunda.profile;

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
import android.widget.TextView;
import android.widget.Toast;

import com.app.technofunda.R;
import com.app.technofunda.ethiqs.SessionManager;
import com.app.technofunda.ethiqs.frontview;
import com.app.technofunda.ewallet.XMLParser;
import com.app.technofunda.newsupdate.NewsUpdateActivity;
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
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    static final String KEY_SONG = "Profile";
    static final String KEY_PNAME = "Name";
    static final String KEY_PMOBILE = "Mobile";
    static final String KEY_PEMAIL = "Email";
    static final String KEY_UPLINE = "uplines";
    static final String KEY_SPONCER = "Sponcer";
    static final String KEY_POSITION = "position";
    static final String KEY_DATE="doj";
    static final String KEY_ADDRESS="address";
    static final String KEY_BANKACC="bankacc";
    static final String KEY_ACCNAM="accname";
    static final String KEY_BANKNAM="bankname";
    static final String KEY_BRNAM="branchnm";
    static final String KEY_BANKCITY="bankcity";
    static final String KEY_IFSC="ifscode";
    private DownloadData downloadData = new DownloadData();
    private ProgressDialog pDialog;
    String url,puid1, profile1,pmbbno,xml;
    SharedPreferences pref;
    TextView name,mobile,email,upline, sponcerid,position,daterg,addrs,bankacc,accname,bankname,brname,bkcity,ifsc;
    ImageView homef,updatef,logoutf,profilef;
    ProgressDialog progressBar;
    SessionManager session;
    String loginid,mobilenumber,passwords,s;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name=(TextView)findViewById(R.id.txt_pro_fnam);
        email=(TextView)findViewById(R.id.txt_pro_email);
        mobile=(TextView)findViewById(R.id.txt_pro_mobile);
        upline=(TextView)findViewById(R.id.txt_upl_id);
        sponcerid=(TextView)findViewById(R.id.txt_pro_spo_id);
        position=(TextView)findViewById(R.id.txt_profile);
        daterg=(TextView)findViewById(R.id.txt_reg);
        addrs=(TextView)findViewById(R.id.txt_addrs);
        bankacc=(TextView)findViewById(R.id.txt_bankacc);
        accname=(TextView)findViewById(R.id.txt_accnam);
        bankname=(TextView)findViewById(R.id.txt_bknam);
        brname=(TextView)findViewById(R.id.txt_brchnam);
        bkcity=(TextView)findViewById(R.id.txt_brcity);
        ifsc=(TextView)findViewById(R.id.txt_ifscc);

        pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            puid1=pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            pmbbno=pref.getString(Config.KEY_MOBILE, "");
        }
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

        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {
            downloadData = new DownloadData();
            downloadData.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
        }
        //FOOTER
        homef=(ImageView)findViewById(R.id.home_footer);
        homef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,frontview.class);
                startActivity(intent);
            }
        });
        updatef=(ImageView)findViewById(R.id.update_footer);
        updatef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this, NewsUpdateActivity.class);
                startActivity(intent);

            }
        });
        logoutf=(ImageView)findViewById(R.id.logout_footer);
        logoutf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nf = cn.getActiveNetworkInfo();
                if (nf != null && nf.isConnected() == true) {
                    progressBar = ProgressDialog.show(ProfileActivity.this, "Please wait ...", "Logout processing ...", false);
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
                            ProfileActivity.this.finish();
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
                Intent intent=new Intent(ProfileActivity.this,ProfileActivity.class);
                startActivity(intent);

            }
        });



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
    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try {
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
            s = Html.fromHtml(result).toString();
            if (s.contains("Login Failed")){
                AlertDialog.Builder builder2 = new AlertDialog.Builder(ProfileActivity.this);
                builder2.setTitle("Login");
                builder2.setMessage("LoginId or Mobile No. Not Exist");
                builder2.setPositiveButton("OK",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                        session.logoutUser();
                        finish();
                    }
                });
                builder2.show();
            }
        }
    }

    private  class DownloadData extends AsyncTask<String, Void, Document> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProfileActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        Document doc = null;
        protected Document doInBackground(String... args) {
//            String url = Config.URL + "/api/apilogin.aspx?uid=" + String.valueOf(userid) + "&pass=" + String.valueOf(upass) + "&mbl=" + String.valueOf(mobile);
//            url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(pmbbno) + "&msg=" + String.valueOf(profile1) + "%20" + "&uid=" + String.valueOf(puid1);
          //  http://technofundaresearch.com//api/Apiurl.aspx?Mobile=7057255777&msg=profile%20tfr
            profile1="profile";
            url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(pmbbno) + "&msg=" + String.valueOf(profile1) + "%20" + String.valueOf(puid1);
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
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(ProfileActivity.this);
                    builder2.setTitle("Profile");
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
                    NodeList nl = doc.getElementsByTagName(KEY_SONG);
                    for (int i = 0; i < nl.getLength(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        Element e = (Element) nl.item(i);
                        name.setText(parser.getValue(e, KEY_PNAME));
                        mobile.setText(parser.getValue(e, KEY_PMOBILE));
                        email.setText(parser.getValue(e, KEY_PEMAIL));
                        upline.setText(parser.getValue(e, KEY_UPLINE));
                        sponcerid.setText(parser.getValue(e, KEY_SPONCER));
                        position.setText(parser.getValue(e, KEY_POSITION));
                        daterg.setText(parser.getValue(e,KEY_DATE));
                        addrs.setText(parser.getValue(e,KEY_ADDRESS));
                        bankacc.setText(parser.getValue(e,KEY_BANKACC));
                        accname.setText(parser.getValue(e,KEY_ACCNAM));
                        bankname.setText(parser.getValue(e,KEY_BANKNAM));
                        brname.setText(parser.getValue(e,KEY_BRNAM));
                        bkcity.setText(parser.getValue(e,KEY_BANKCITY));
                        ifsc.setText(parser.getValue(e,KEY_IFSC));
                    }
                }
            }catch (Exception e) {
                Log.d("Exception", e.getMessage());
            }
        }
    }
}


