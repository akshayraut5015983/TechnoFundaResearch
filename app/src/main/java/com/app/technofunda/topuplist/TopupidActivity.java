package com.app.technofunda.topuplist;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.app.technofunda.R;
import com.app.technofunda.ethiqs.SessionManager;
import com.app.technofunda.ewallet.XMLParser;
import com.app.technofunda.newsupdate.NewsUpdateActivity;
import com.app.technofunda.profile.ProfileActivity;
import com.app.technofunda.ethiqs.ServiceHandler;
import com.app.technofunda.ethiqs.Validate;
import com.app.technofunda.ethiqs.frontview;
import com.app.technofunda.util.Config;

public class TopupidActivity extends AppCompatActivity {
    SharedPreferences pref;
    String duid1,dmbbno,topupids,topupidname,topupid,packg,packgname,packagenam,selctpack,id,pack,ids,piamount,pival,submittopup;
    static final String KEY_MEMNAME = "membername";
    static final String KEY_AMOUNT = "amt";
    private static final String KEY_PINVALUE = "pinvalue";
    private static final String KEY_PINAMOUNT = "pinamount";
    private DownloadData downloadData = new DownloadData();
    private DownloadPackData downloadPackg = new DownloadPackData();
    private ProgressDialog pDialog;
    private static String url,urls,urlsp,suburl;
    private static final String TAG_CONTACTS = "Pkg";
    private static final String TAG_PINNAME = "Pinname";
    JSONArray contacts = null;
    List<String> select_package;
    EditText toplogid,topidname,topidstatus;
    Spinner selectpackage;
    TextView pinamount,pinvalue,alreadyid;
    Button submit;
    ImageView homef,updatef,logoutf,profilef;
    ProgressDialog progressBar;
    SessionManager session;
    String loginid,mobilenumber,passwords,s;



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.topup_id_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        alreadyid=(TextView)findViewById(R.id.text_login_id);
        toplogid=(EditText)findViewById(R.id.edit_login_id);
        topidname=(EditText)findViewById(R.id.edit_topup_name);
        topidstatus=(EditText)findViewById(R.id.edit_current_status);
        selectpackage = (Spinner) findViewById(R.id.package_groups);
        pinamount=(TextView)findViewById(R.id.tex_pin_amount);
        pinvalue=(TextView)findViewById(R.id.text_pin_value);
        submit=(Button)findViewById(R.id.btn_submit_amount);

        pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            duid1=pref.getString(Config.KEY_NAME, "");
        }if (pref.contains(Config.KEY_MOBILE)) {
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

        select_package = new ArrayList<String>();

        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {

            topupids = "pkg";
            url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(dmbbno) + "&msg=" + String.valueOf(topupids) + "%20" + String.valueOf(duid1);
            new GetContacts().execute();
            select_package.add("Select Package");
            ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(TopupidActivity.this, android.R.layout.simple_spinner_item,select_package);
            stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selectpackage.setAdapter(stateAdapter);

            toplogid.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {}
                public void beforeTextChanged(CharSequence s, int start, int count, int after){}
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                topupid = toplogid.getText().toString().trim();
                topupidname = "Name";
                urls = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(dmbbno) + "&msg=" + String.valueOf(topupidname) + "%20" + String.valueOf(topupid);

                if(topupid.equals("sai")||topupid.length()>=6) {
                    downloadData = new DownloadData();
                    downloadData.execute();
                }
                else if(topupid.length()<6){
                    topidname.setText("");
                    topidstatus.setText("");
                    alreadyid.setText("Fill Your All Details");
                    alreadyid.setTextColor(Color.BLACK);
                }
            }
            });

            selectpackage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
                    packgname = selectpackage.getSelectedItem().toString().trim();
                    packagenam=packgname.replaceAll(" ","-");
                    packg="Pkgdetails";
                    if(packgname.equals("Select Package")){
                    }else {
                        urlsp = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(dmbbno) + "&msg=" + String.valueOf(packg) + "%20" + String.valueOf(packagenam);
                        downloadPackg = new DownloadPackData();
                        downloadPackg.execute();
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {}
            });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id = toplogid.getText().toString().trim();
                    piamount = pinamount.getText().toString().trim();
                    pival = pinvalue.getText().toString().trim();
                    pack = selectpackage.getSelectedItem().toString().trim();
                    boolean check = true;
                    Validate validate = new Validate();

                    if (pack.equals("Select Package")) {
                        check = false;
                        Toast.makeText(getApplicationContext(), "Please Select Package", Toast.LENGTH_SHORT).show();
                    }if (validate.isNotEmpty(id)) {
                        id = toplogid.getText().toString();
                        ids = id.replaceAll(" ", "");
                    } else {
                        id = "";
                        check = false;
                        toplogid.setError("Please fill login id");
                    }

                    if (check) {
                        selctpack=pack.replaceAll(" ","-");
                            AlertDialog.Builder builder = new AlertDialog.Builder(TopupidActivity.this);
                            builder.setTitle("Are you sure want to submit");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            submittopup = "Topup";
                            suburl = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(dmbbno) + "&msg=" + String.valueOf(submittopup) + "%20" + String.valueOf(duid1) + "%20" + String.valueOf(ids) + "%20" + String.valueOf(selctpack) + "%20" + String.valueOf(piamount) + "%20" + String.valueOf(pival);
                            DownloadWebPageTask task1 = new DownloadWebPageTask();
                            task1.execute(new String[]{String.valueOf(suburl)});
                                pDialog = new ProgressDialog(TopupidActivity.this);
                                pDialog.setMessage("Please wait...");
                                pDialog.setCancelable(false);
                                pDialog.show();
                            dialog.dismiss();
                            }});
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                            });
                            builder.setCancelable(false);
                            builder.show();
                    }
                }
            });
        }else {
            Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
        }

        //footer
        homef=(ImageView)findViewById(R.id.home_footer);
        homef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TopupidActivity.this,frontview.class);
                startActivity(intent);
            }
        });
        updatef=(ImageView)findViewById(R.id.update_footer);
        updatef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TopupidActivity.this, NewsUpdateActivity.class);
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
                    progressBar = ProgressDialog.show(TopupidActivity.this, "Please wait ...", "Logout processing ...", false);
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
                            TopupidActivity.this.finish();
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
                Intent intent=new Intent(TopupidActivity.this,ProfileActivity.class);
                startActivity(intent);

            }
        });

        String urls = Config.URL+"/api/apilogin.aspx?uid=" + String.valueOf(loginid) + "&pass=" + String.valueOf(passwords) + "&mbl=" + String.valueOf(mobilenumber);
        DownloadWebPageTask task2 = new DownloadWebPageTask();
        task2.execute(new String[]{String.valueOf(urls)});


    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TopupidActivity.this);
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
                        select_package.add(c.getString(TAG_PINNAME));
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
                    while ((s = buffer.readLine()) != null)  {
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
            if (pDialog.isShowing())
                pDialog.dismiss();

                AlertDialog.Builder builder2 = new AlertDialog.Builder(TopupidActivity.this);
                builder2.setMessage(String.valueOf(s).toString().trim());
                builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
                }});
                builder2.show();
            s = Html.fromHtml(result).toString();
            if (s.contains("Login Failed")){
                AlertDialog.Builder builder3 = new AlertDialog.Builder(TopupidActivity.this);
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
                builder3.show();
            }
        }
    }

    private class DownloadPackData extends AsyncTask<String, Void, Document> {
        Document doc = null;
        protected Document doInBackground(String... args) {
            try {
                XMLParser parser = new XMLParser();
                String xml = parser.getXmlFromUrl(urlsp);
                doc = parser.getDomElement(xml);
            } catch (Exception e) {
                Log.d("Exception", e.getMessage());}
            return doc;
        }
        @Override
        protected void onPostExecute(Document doc) {
            try {
                XMLParser parser = new XMLParser();
                NodeList nl = doc.getElementsByTagName(Config.KEY_SONG);
                for (int i = 0; i < nl.getLength(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                Element e = (Element) nl.item(i);
                pinvalue.setText(parser.getValue(e, KEY_PINVALUE));
                pinamount.setText(parser.getValue(e,KEY_PINAMOUNT));
                }
            } catch (Exception e) {
                Log.d("Exception", e.getMessage());}
        }
    }

    private class DownloadData extends AsyncTask<String, Void, Document> {

        Document doc = null;
        String xml;
        protected Document doInBackground(String... args) {
            try {
                XMLParser parser = new XMLParser();
                xml = parser.getXmlFromUrl(urls);
                doc = parser.getDomElement(xml);
            } catch (Exception e) {
                Log.d("Exception", e.getMessage());
            }
            return doc;
        }
        @Override
        protected void onPostExecute(Document doc) {
            try {
                XMLParser parser = new XMLParser();
                if(doc==null) {
                    alreadyid.setText(Html.fromHtml(xml).toString().trim());
                    alreadyid.setTextColor(Color.RED);
                } else {
                    alreadyid.setText("Fill Your All Details");
                    alreadyid.setTextColor(Color.BLACK);
                    NodeList nl = doc.getElementsByTagName(Config.KEY_SONG);
                    for (int i = 0; i < nl.getLength(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        Element e = (Element) nl.item(i);
                        topidname.setText(parser.getValue(e, KEY_MEMNAME));
                        topidstatus.setText(parser.getValue(e, KEY_AMOUNT));
                    }
                }
            }catch (Exception e) {
                Log.d("Exception", e.getMessage());}
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
}