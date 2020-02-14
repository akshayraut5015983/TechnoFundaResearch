package com.app.technofunda.utility;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.List;

import com.app.technofunda.R;
import com.app.technofunda.ethiqs.ServiceHandler;
import com.app.technofunda.ethiqs.Validate;
import com.app.technofunda.util.Config;

public class DTHRecharge extends Fragment {

    EditText subscribid,rechamt;
    Spinner dthopcode;
    Button subdthrech;
    SharedPreferences pref;
    String loginid,mobiles,url,dthchkcd,dth,suid,dtcode,drs,dthop,subid,amount,Dthrch,urls,s;
    List<String> dthcodelist;
    private static final String TAG_CONTACTS = "Codes";
    private static final String TAG_PINNAME = "Code";
    JSONArray contacts = null;
    private ProgressDialog pDialog;
    ImageView homef,updatef,logoutf,profilef;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View views = inflater.inflate(R.layout.dth_fragment, container, false);
        subscribid=(EditText)views.findViewById(R.id.edit_subscrib_id);
        dthopcode = (Spinner)views. findViewById(R.id.subscribid_grps);
        rechamt=(EditText)views.findViewById(R.id.edit_rchsubcrib);
        subdthrech=(Button)views.findViewById(R.id.btn_submit_subscib);

        pref = getContext().getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            loginid = pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            mobiles = pref.getString(Config.KEY_MOBILE, "");
        }

        dthcodelist=new ArrayList<String>();

        ConnectivityManager cn = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {
            dthcodelist.clear();
            dthcodelist.add("Select Service Provider");
            dthchkcd = "ChkCode";
            dth="DTH";
            url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(mobiles) + "&msg=" + String.valueOf(dthchkcd) + "%20" + String.valueOf(loginid)+ "%20" + String.valueOf(dth);
            new GetContacts().execute();

            ArrayAdapter<String> codeAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item,dthcodelist);
            codeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dthopcode.setAdapter(codeAdapter);

            subdthrech.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    suid = subscribid.getText().toString().trim();
                    dtcode = dthopcode.getSelectedItem().toString().trim();
                    drs = rechamt.getText().toString().trim();
                    dthop=dtcode.replaceAll(" ", "-");

                    boolean check = true;
                    Validate validate = new Validate();

                    if (dtcode.equals("Select Service Provider")) {
                        check = false;
                        Toast.makeText(getContext(), "Please Select Service Provider", Toast.LENGTH_SHORT).show();
                    }
                    if (validate.isNotEmpty(suid)) {
                        suid = subscribid.getText().toString();
                        subid = suid.replaceAll(" ", "");
                    }
                    else {
                        suid = "";
                        check = false;
                        subscribid.setError("Please enter subscriber id");
                    }
                    if (validate.isNotEmpty(drs)) {
                        drs = rechamt.getText().toString();
                        amount = drs.replaceAll(" ", "");
                    } else {
                        drs = "";
                        check = false;
                        rechamt.setError("");
                    }
                    if(check) {
                        Dthrch = "Rch";
                        urls = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(mobiles) + "&msg=" + String.valueOf(Dthrch) + "%20" + String.valueOf(loginid) + "%20" + String.valueOf(subid) + "%20" + String.valueOf(amount) + "%20" + String.valueOf(dthop);
                        DownloadWebPageTask task1 = new DownloadWebPageTask();
                        task1.execute(new String[]{String.valueOf(urls)});
                        pDialog = new ProgressDialog(getContext());
                        pDialog.setMessage("Please wait recharge processing...");
                        pDialog.setCancelable(false);
                        pDialog.show();
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
        }
        return views;
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
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
                        dthcodelist.add(c.getString(TAG_PINNAME));
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
                    while ((s = buffer.readLine()) != null)
                    {
                        response += s;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {

            s = Html.fromHtml(result).toString();
            if (pDialog.isShowing())
                pDialog.dismiss();

            AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
            builder2.setTitle("DTH Recharge");
            builder2.setMessage(String.valueOf(s).toString().trim());
            builder2.setCancelable(false);
            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    subscribid.setText("");
                    rechamt.setText("");
                }
            });
            builder2.show();
        }
    }
}