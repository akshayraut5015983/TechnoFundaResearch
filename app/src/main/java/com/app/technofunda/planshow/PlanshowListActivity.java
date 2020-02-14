package com.app.technofunda.planshow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import com.app.technofunda.R;
import com.app.technofunda.ethiqs.ServiceHandler;
import com.app.technofunda.util.Config;

public class PlanshowListActivity extends AppCompatActivity {

    String duid1,dmbbno,planlist,jsonStr;
    ListView planshowlistview;
    TextView status;
    SharedPreferences pref;
    private ProgressDialog pDialog;
    private static String url;
    private static final String TAG_CONTACTS = "Planshow";
    private static final String TAG_ID = "PPId";
    private static final String TAG_NEWID = "New Id";
    private static final String TAG_PSID = "PS Id";
    private static final String TAG_PSNAME = "PS Name";
    private static final String TAG_PSMOBILE = "PS Mbl";
    private static final String TAG_PSEMAIL = "PS Email";
    private static final String TAG_PSCITY = "PS City";
    private static final String TAG_PSDATE = "PS RegdDate";
    private static final String TAG_STATUS = "Status";

    JSONArray contacts = null;
    ArrayList<HashMap<String, String>> addplanshowlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planshowlist_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        planshowlistview = (ListView)findViewById(R.id.planshow_listview);
        addplanshowlist = new ArrayList<HashMap<String, String>>();

        pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            duid1=pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            dmbbno=pref.getString(Config.KEY_MOBILE, "");
        }
        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {
            planlist = "planshowlist";
            url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(dmbbno) + "&msg=" + String.valueOf(planlist) + "%20" + String.valueOf(duid1);
            new GetContacts().execute();
        }else {
            Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
        }
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PlanshowListActivity.this);
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
                        contact.put(TAG_NEWID, c.getString(TAG_NEWID));
                        contact.put(TAG_PSID, c.getString(TAG_PSID));
                        contact.put(TAG_PSNAME, c.getString(TAG_PSNAME));
                        contact.put(TAG_PSMOBILE, c.getString(TAG_PSMOBILE));
                        contact.put(TAG_PSEMAIL, c.getString(TAG_PSEMAIL));
                        contact.put(TAG_PSCITY, c.getString(TAG_PSCITY));
                        contact.put(TAG_PSDATE, c.getString(TAG_PSDATE));
                        contact.put(TAG_STATUS, c.getString(TAG_STATUS));
                        addplanshowlist.add(contact);
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
            if(addplanshowlist.size()!=0) {
                ListAdapter adapter = new SimpleAdapter(PlanshowListActivity.this, addplanshowlist,
                        R.layout.planshow_list_row, new String[]{TAG_ID, TAG_NEWID, TAG_PSID, TAG_PSNAME, TAG_PSMOBILE, TAG_PSEMAIL, TAG_PSCITY,
                        TAG_PSDATE, TAG_STATUS}, new int[]
                        {R.id.text_ppid, R.id.text_newid, R.id.text_psid, R.id.text_psname, R.id.text_psmobile, R.id.text_psemail, R.id.text_pscity,
                        R.id.text_psregdate, R.id.text_psstatus});
                planshowlistview.setAdapter(adapter);
            }else {
                alertPkgEmpty();
            }
        }
    }
    private void alertPkgEmpty(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Planshow List");
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
