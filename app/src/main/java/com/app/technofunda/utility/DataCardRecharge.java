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

public class DataCardRecharge extends Fragment {

    EditText datacardno,rchamnt;
    Spinner dtcdcode;
    Button subdatacrd;
    SharedPreferences pref;
    List<String> datacodelist;
    String loginid,mobiles,dthchkcd,datacrd,url,crdno,dtcrdcd,dtcrdrs,dtcrop,cardno,amnt,Datarch,urls,s;
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

        View viewc = inflater.inflate(R.layout.datacardrecharge_fragment, container, false);
        datacardno=(EditText)viewc.findViewById(R.id.edit_datacard_number);
        dtcdcode = (Spinner)viewc. findViewById(R.id.dtccode_groups);
        rchamnt=(EditText)viewc.findViewById(R.id.edit_rchamount);
        subdatacrd=(Button)viewc.findViewById(R.id.btn_submit_datacard);

        pref = getContext().getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            loginid = pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            mobiles = pref.getString(Config.KEY_MOBILE, "");
        }

        datacodelist=new ArrayList<String>();

        ConnectivityManager cn = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {
            datacodelist.clear();
            datacodelist.add("Select Service Provider");
            dthchkcd = "ChkCode";
            datacrd="Data-Card";
            url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(mobiles) + "&msg=" + String.valueOf(dthchkcd) + "%20" + String.valueOf(loginid)+ "%20" + String.valueOf(datacrd);
            new GetContacts().execute();

            ArrayAdapter<String> codeAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item,datacodelist);
            codeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dtcdcode.setAdapter(codeAdapter);

            subdatacrd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    crdno = datacardno.getText().toString().trim();
                    dtcrdcd = dtcdcode.getSelectedItem().toString().trim();
                    dtcrdrs = rchamnt.getText().toString().trim();
                    dtcrop=dtcrdcd.replaceAll(" ", "-");

                    boolean check = true;
                    Validate validate = new Validate();

                    if (dtcrdcd.equals("Select Service Provider")) {
                        check = false;
                        Toast.makeText(getContext(), "Please Select Service Provider", Toast.LENGTH_SHORT).show();
                    }
                    if (validate.isNotEmpty(crdno)) {
                        crdno = datacardno.getText().toString();
                        cardno = crdno.replaceAll(" ", "");
                    }
                    else {
                        crdno = "";
                        check = false;
                        datacardno.setError("Please enter datacard number");
                    }
                    if (validate.isNotEmpty(dtcrdrs)) {
                        dtcrdrs = rchamnt.getText().toString();
                        amnt = dtcrdrs.replaceAll(" ", "");
                    } else {
                        dtcrdrs = "";
                        check = false;
                        rchamnt.setError("");
                    }
                    if(check) {
                        Datarch = "Rch";
                        urls = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(mobiles) + "&msg=" + String.valueOf(Datarch) + "%20" + String.valueOf(loginid) + "%20" + String.valueOf(cardno) + "%20" + String.valueOf(amnt) + "%20" + String.valueOf(dtcrop);
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
        return viewc;
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
                        datacodelist.add(c.getString(TAG_PINNAME));
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
            builder2.setTitle("Data Card Recharge");
            builder2.setMessage(String.valueOf(s).toString().trim());
            builder2.setCancelable(false);
            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    datacardno.setText("");
                    rchamnt.setText("");
                }
            });
            builder2.show();
        }
    }
}
