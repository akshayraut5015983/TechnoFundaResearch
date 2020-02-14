package com.app.technofunda.myincome;

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
import com.app.technofunda.ewallet.XMLParser;
import com.app.technofunda.util.Config;

public class IncomeHistoryActivity extends AppCompatActivity {
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    static final String KEY_PNAME = "payname";
    static final String KEY_PDATE = "PayDate";
    static final String KEY_NETPAY = "NetPay";
    SharedPreferences pref;
    String url,inuid1, inmbbno, income,xml;
    ListView listincome;
    public static TextView totalnetpay;
    private ProgressDialog pDialog;
    private DownloadData downloadData = new DownloadData();
    public String f,a;
    public int c=0,b;
    ImageView homef,updatef,logoutf,profilef;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myincome_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        totalnetpay=(TextView)findViewById(R.id.txt_total_netpay);
        listincome = (ListView) findViewById(R.id.income_list);

        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {
            downloadData = new DownloadData();
            downloadData.execute();
        }
        else{
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
                pDialog = new ProgressDialog(IncomeHistoryActivity.this);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();
        }
        Document doc = null;
        protected Document doInBackground(String... args) {

            pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
                if (pref.contains(Config.KEY_NAME)) {
                    inuid1 = pref.getString(Config.KEY_NAME, "");
                }
                if (pref.contains(Config.KEY_MOBILE)) {
                    inmbbno = pref.getString(Config.KEY_MOBILE, "");
                }
            income = "income";
            url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(inmbbno) + "&msg=" + String.valueOf(income) + "%20" + String.valueOf(inuid1);
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
            try
             {
                XMLParser parser = new XMLParser();
                if (doc==null) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(IncomeHistoryActivity.this);
                    builder2.setTitle("Income");
                    builder2.setMessage(Html.fromHtml(xml).toString().trim());
                    builder2.setPositiveButton("OK",new DialogInterface.OnClickListener() {
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
                        map.put(KEY_PNAME, parser.getValue(e, KEY_PNAME));
                        map.put(KEY_PDATE, parser.getValue(e, KEY_PDATE));
                        map.put(KEY_NETPAY, parser.getValue(e, KEY_NETPAY));
                        a = parser.getValue(e, KEY_NETPAY);
                        b = Integer.parseInt(a);
                        c = c + b;
                        f = String.valueOf(c);
                        dataList.add(map);
                    }
                }
                IncomeHistoryActivity.totalnetpay.setText(f);
                SingleMenuItemActivity2 adapter = new SingleMenuItemActivity2(IncomeHistoryActivity.this, dataList);
                listincome.setAdapter(adapter);
             } catch (Exception e) {
                Log.d("Exception", e.getMessage());
             }
        }
    }
}