package com.app.technofunda.ewallet;


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
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.technofunda.R;
import com.app.technofunda.ethiqs.ServiceHandler;
import com.app.technofunda.ethiqs.Validate;
import com.app.technofunda.util.Config;

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

public class

PinGenerate extends Fragment {

    Spinner pinName;
    EditText pinPoint,pinAmount,pinQuantity;
    Button subpingen;
    String pinpoints,pinamounts,pinquantitys,quantity,pinnam;
    String userloginid,userloginmobile,s,pinnumber,urlpin,urlsp,packgname,packagenam,Urlpgen,generate;
    List<String> select_pinname;
    SharedPreferences pref;
    private static final String KEY_PINVALUE = "pinvalue";
    private static final String KEY_PINAMOUNT = "pinamount";
    private DownloadPackData downloadPackg = new DownloadPackData();
    private static final String TAG_CONTACTS = "Pkg";
    private static final String TAG_PINNAME = "Pinname";
    JSONArray contacts = null;
    private ProgressDialog pDialog;
    ImageView homef,updatef,logoutf,profilef;
    Context context;
    FragmentTransaction fragmentTransaction;
    EditText pinquantity_edt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewg = inflater.inflate(R.layout.pingeneration_fragment, container, false);

        pinName= (Spinner)viewg.findViewById(R.id.pinnam_groups);
        pinPoint = (EditText)viewg.findViewById(R.id.pinpoint_edit);
        pinAmount = (EditText)viewg.findViewById(R.id.pinamount_edit);
        pinQuantity = (EditText)viewg.findViewById(R.id.pinquantity_edit);
        subpingen = (Button)viewg.findViewById(R.id.btngen_pin);
       // pinquantity_edt=(EditText)viewg.findViewById(R.id.pinamount_edit);
       // getFragmentManager().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        pref = getContext().getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            userloginid=pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            userloginmobile=pref.getString(Config.KEY_MOBILE, "");
        }

        select_pinname = new ArrayList<String>();

        ConnectivityManager cn = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {
            select_pinname.clear();
            select_pinname.add("Select Pin Name");
            pinnumber="pkg";
            urlpin = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(userloginmobile) + "&msg=" + String.valueOf(pinnumber) + "%20" + String.valueOf(userloginid);
            new GetContacts().execute();
            ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, select_pinname);
            stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            pinName.setAdapter(stateAdapter);

            pinName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
                    packgname = pinName.getSelectedItem().toString().trim();
                    packagenam=packgname.replaceAll(" ","-");
                    String packg="Pkgdetails";
                if(packgname.equals("Select Pin Name")){
                    pinPoint.setText("");
                    pinAmount.setText("");;
                }else {
                    urlsp = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(userloginmobile) + "&msg=" + String.valueOf(packg) + "%20" + String.valueOf(packagenam);
                    downloadPackg = new DownloadPackData();
                    downloadPackg.execute();
                }
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {}
            });

        subpingen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinnam = pinName.getSelectedItem().toString().trim();
                packagenam=pinnam.replaceAll(" ","-");
                pinpoints = pinPoint.getText().toString().trim();
                pinamounts = pinAmount.getText().toString().trim();
                pinquantitys = pinQuantity.getText().toString().trim();

                boolean check = true;
                Validate validate = new Validate();
                if (pinnam.equals("Select Pin Name")) {
                    check = false;
                    Toast.makeText(getContext(), "Please select pin name", Toast.LENGTH_LONG).show();
                }
                if (validate.isNotEmpty(pinquantitys)) {
                    pinquantitys = pinQuantity.getText().toString();
                    quantity = pinquantitys.replaceAll(" ", "");
                } else {
                    pinquantitys = "";
                    check = false;
                    pinQuantity.setError("Please enter pin quantity");
                }
                if (check) {
                    generate="PinGenerate";
                    Urlpgen = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(userloginmobile) + "&msg=" + String.valueOf(generate) + "%20" + String.valueOf(userloginid)+ "%20" + String.valueOf(packagenam)+ "%20" + String.valueOf(quantity);
                    DownloadWebPageTask task1 = new DownloadWebPageTask();
                    task1.execute(new String[]{String.valueOf(Urlpgen)});
                    pDialog = new ProgressDialog(getContext());
                    pDialog.setMessage("Please wait...");
                    pDialog.setCancelable(false);
                    pDialog.show();
                }
            }
        });
        }else {
            Toast.makeText(getContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
        }
        return viewg;
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(urlpin, ServiceHandler.GET);
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
                    pinPoint.setText(parser.getValue(e, KEY_PINVALUE));
                    pinAmount.setText(parser.getValue(e,KEY_PINAMOUNT));
                }
            } catch (Exception e) {
                Log.d("Exception", e.getMessage());}
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
            s = Html.fromHtml(result).toString();
            if (pDialog.isShowing())
                pDialog.dismiss();

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(String.valueOf(s).toString().trim());
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    pinName.setSelection(0);
                    pinPoint.setText("");
                    pinAmount.setText("");
                    pinQuantity.setText("");
                }
            });
            builder.show();
        }
    }
}
