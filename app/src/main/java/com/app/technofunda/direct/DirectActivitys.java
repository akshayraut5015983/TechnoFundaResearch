package com.app.technofunda.direct;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.app.technofunda.R;
import com.app.technofunda.ethiqs.SessionManager;
import com.app.technofunda.newsupdate.NewsUpdateActivity;
import com.app.technofunda.profile.ProfileActivity;
import com.app.technofunda.ethiqs.ServiceHandler;
import com.app.technofunda.ethiqs.frontview;
import com.app.technofunda.util.Config;

public class DirectActivitys extends AppCompatActivity {

    ListView directlist;
    EditText edit_search;
    TextView totaldirectcount;
    SharedPreferences pref;
    String duid1,dmbbno,direct1,directs,jsonStr;
    private ProgressDialog pDialog;
    private static String url;
    public static final String TAG_CONTACTS = "Directs";
    public static final String TAG_ID = "Id";
    public static final String TAG_NAME = "Name";
    public static final String TAG_DATE = "Date";
    public static final String TAG_POSITION = "Pinvalue";
    JSONArray contacts = null;
    private CustemAdptDirectList adapter;
    ArrayList<HashMap<String, String>> usersList = new ArrayList<HashMap<String, String>>();
    ImageView homef,updatef,logoutf,profilef;
    ProgressDialog progressBar;
    SessionManager session;
    String loginid,mobilenumber,passwords,s;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_activityss);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        totaldirectcount=(TextView) findViewById(R.id.txt_cnt_total);
        directlist = (ListView)findViewById(R.id.team_list);
        edit_search = (EditText)findViewById(R.id.inputSearch);

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
            directs = "direct";
            String urls = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(dmbbno) + "&msg=" + String.valueOf(directs) + "%20" + String.valueOf(duid1);
            DownloadWebPageTask task1 = new DownloadWebPageTask();
            task1.execute(new String[]{String.valueOf(urls)});
            direct1="directlist";
            url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(dmbbno) + "&msg=" +String.valueOf(direct1) + "%20" + String.valueOf(duid1);
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

        //footer
        homef=(ImageView)findViewById(R.id.home_footer);
        homef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DirectActivitys.this,frontview.class);
                startActivity(intent);
            }
        });
        updatef=(ImageView)findViewById(R.id.update_footer);
        updatef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DirectActivitys.this, NewsUpdateActivity.class);
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
                    progressBar = ProgressDialog.show(DirectActivitys.this, "Please wait ...", "Logout processing ...", false);
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
                            DirectActivitys.this.finish();
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
                Intent intent=new Intent(DirectActivitys.this,ProfileActivity.class);
                startActivity(intent);

            }
        });
       /* String urls = Config.URL + "/api/apilogin.aspx?uid=" + String.valueOf(loginid) + "&pass=" + String.valueOf(passwords) + "&mbl=" + String.valueOf(mobilenumber);
        DirectActivitys.DownloadWebPageTask task2 = new DirectActivitys.DownloadWebPageTask();
        task2.execute(new String[]{String.valueOf(urls)});
*/



    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DirectActivitys.this);
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
                        contact.put(TAG_DATE, c.getString(TAG_DATE));
                        contact.put(TAG_POSITION, "Pin Value: "+c.getString(TAG_POSITION));
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
                adapter = new CustemAdptDirectList(getApplicationContext(), usersList);
                directlist.setAdapter(adapter);
            } else {
                directlist.setAdapter(null);
              //  alertPkgEmpty();
            }
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
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response;
        }
        protected void onPostExecute(String result) {
            String s = Html.fromHtml(result).toString();
            totaldirectcount.setText("Total Direct Count: "+String.valueOf(s).toString().trim());
            s = Html.fromHtml(result).toString();
            if (s.contains("Login Failed")){
                AlertDialog.Builder builder2 = new AlertDialog.Builder(DirectActivitys.this);
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

    private void alertPkgEmpty(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Direct List");
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