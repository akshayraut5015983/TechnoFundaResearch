package com.app.technofunda.ewallet;

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
import com.app.technofunda.profile.ProfileActivity;
import com.app.technofunda.ethiqs.ServiceHandler;
import com.app.technofunda.ethiqs.Validate;
import com.app.technofunda.ethiqs.frontview;
import com.app.technofunda.util.Config;

public class PinTransferActivity extends AppCompatActivity {

    TextView alreadyid,totalpin;
    EditText transferid,transfername,pinpoint,pinamount,notranspin;
    Spinner piname;
    Button subpintrans;
    String id,tid,nopin,numpin,name,duid1,dmbbno,pinna,url,transname,transpid,urls,packgname,packagenam,urlsp,trans,Urlptrans,nam,topin,urlb;
    int total;
    List<String> select_pinname;
    SharedPreferences pref;
    private static final String TAG_CONTACTS = "Pkg";
    private static final String TAG_PINNAME = "Pinname";
    static final String KEY_MEMNAME = "membername";
    JSONArray contacts = null;
    private DownloadData downloadData = new DownloadData();
    private DownloadPackData downloadPackg = new DownloadPackData();
    private static final String KEY_PINVALUE = "pinvalue";
    private static final String KEY_PINAMOUNT = "pinamount";
    private static final String TAG_TOTALPIN = "TotalPin";
    private static final String TAG_PIN = "TtlPins";
    private ProgressDialog pDialog;
    ImageView homef,updatef,logoutf,profilef;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pintransfer_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        totalpin=(TextView)findViewById(R.id.total_apin);
        alreadyid=(TextView)findViewById(R.id.text_login_id);
        transferid=(EditText)findViewById(R.id.pintrans_id);
        transfername=(EditText)findViewById(R.id.pintrans_name);
        piname=(Spinner)findViewById(R.id.transpinnam_gps);
        pinpoint=(EditText)findViewById(R.id.transpinpoint_edit);
        pinamount=(EditText)findViewById(R.id.transpinamount_edit);
        notranspin=(EditText)findViewById(R.id.no_pin_trans);
        subpintrans=(Button)findViewById(R.id.btntran_pin);

        pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            duid1=pref.getString(Config.KEY_NAME, "");
        }if (pref.contains(Config.KEY_MOBILE)) {
            dmbbno=pref.getString(Config.KEY_MOBILE, "");
        }
        select_pinname = new ArrayList<String>();

        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {

            topin = "Totalpin";
            urlb = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(dmbbno) + "&msg=" + String.valueOf(topin) + "%20" + String.valueOf(duid1);
            new GetPin().execute();

            pinna = "pkg";
            url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(dmbbno) + "&msg=" + String.valueOf(pinna) + "%20" + String.valueOf(duid1);
            new GetContacts().execute();
            select_pinname.add("Select Pin Name");
            ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(PinTransferActivity.this, android.R.layout.simple_spinner_item, select_pinname);
            stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            piname.setAdapter(stateAdapter);

            transferid.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {}
                public void beforeTextChanged(CharSequence s, int start, int count, int after){}
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    transpid = transferid.getText().toString().trim();
                    transname = "Membername";
                    urls =Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(dmbbno) + "&msg=" + String.valueOf(transname) + "%20" + String.valueOf(transpid);
                    if(transpid.equals("sai")||transpid.length()>=6) {
                        downloadData = new DownloadData();
                        downloadData.execute();
                    }else if(transpid.length()<6){
                        transfername.setText("");
                        alreadyid.setText("Fill Your All Details");
                        alreadyid.setTextColor(Color.BLACK);
                    }}
            });

            piname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
                    packgname = piname.getSelectedItem().toString().trim();
                    packagenam=packgname.replaceAll(" ","-");
                    String packg="Pkgdetails";
                    if(packgname.equals("Select Pin Name")){
                        pinpoint.setText("");
                        pinamount.setText("");;
                    }else {
                        urlsp = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(dmbbno) + "&msg=" + String.valueOf(packg) + "%20" + String.valueOf(packagenam);
                        downloadPackg = new DownloadPackData();
                        downloadPackg.execute();
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {}
            });

            subpintrans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                 id = transferid.getText().toString().trim();
                 nam = transfername.getText().toString().trim();
                 nopin = notranspin.getText().toString().trim();
                 name = piname.getSelectedItem().toString().trim();

                 boolean check = true;
                 Validate validate = new Validate();

                 if (name.equals("Select Pin Name")) {
                     check = false;
                     Toast.makeText(getApplicationContext(), "Please select pin name", Toast.LENGTH_LONG).show();
                 }
                 if (validate.isNotEmpty(id)) {
                     id = transferid.getText().toString();
                     tid = id.replaceAll(" ", "");
                 } else {
                    id = "";
                    check = false;
                    transferid.setError("Please enter transfer id");
                 }
                 if (validate.isNotEmpty(nam)) {
                     nam = transfername.getText().toString();
                 } else {
                     nam = "";
                    check = false;
                    transfername.setError("");
                 }
                 if (validate.isNotEmpty(nopin)) {
                     nopin = notranspin.getText().toString();
                     numpin = nopin.replaceAll(" ", "");
                 } else {
                     nopin = "";
                     check = false;
                     notranspin.setError("Please enter no.s of pin");
                 }
                 if (check) {
                     packagenam=name.replaceAll(" ","-");
                     trans="PinTransfer";
                     Urlptrans = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(dmbbno) + "&msg=" + String.valueOf(trans) + "%20" + String.valueOf(duid1)+ "%20" + String.valueOf(tid)+ "%20" + String.valueOf(packagenam)+ "%20" + String.valueOf(numpin);
                     DownloadWebPageTask task1 = new DownloadWebPageTask();
                     task1.execute(new String[]{String.valueOf(Urlptrans)});
                     pDialog = new ProgressDialog(PinTransferActivity.this);
                     pDialog.setMessage("Please wait...");
                     pDialog.setCancelable(false);
                     pDialog.show();
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
                Intent intent=new Intent(PinTransferActivity.this,frontview.class);
                startActivity(intent);
            }
        });
//        updatef=(ImageView)findViewById(R.id.update_footer);
//        updatef.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(PinTransferActivity.this,NewsUpdateActivity.class);
//                startActivity(intent);
//
//            }
//        });
        logoutf=(ImageView)findViewById(R.id.logout_footer);
        logoutf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PinTransferActivity.this);
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
                Intent intent=new Intent(PinTransferActivity.this,ProfileActivity.class);
                startActivity(intent);

            }
        });


    }


    private class GetPin extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();}
        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(urlb, ServiceHandler.GET);
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    contacts = jsonObj.getJSONArray(TAG_TOTALPIN);
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        total=Integer.parseInt(c.getString(TAG_PIN).toString());
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
            totalpin.setText(String.valueOf(total).toString().trim());
        }
    }

    public class DownloadWebPageTask extends AsyncTask<String, Void, String> {
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
            String s = Html.fromHtml(result).toString();

            if (pDialog.isShowing())
                pDialog.dismiss();

            AlertDialog.Builder builder = new AlertDialog.Builder(PinTransferActivity.this);
            builder.setMessage(String.valueOf(s).toString().trim());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });
            builder.show();
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
                    pinpoint.setText(parser.getValue(e, KEY_PINVALUE));
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
                        transfername.setText(parser.getValue(e, KEY_MEMNAME));
                    }
                }
            }catch (Exception e) {
                Log.d("Exception", e.getMessage());}
        }
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PinTransferActivity.this);
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
                        select_pinname.add(c.getString(TAG_PINNAME));
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
                pDialog.dismiss();
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
