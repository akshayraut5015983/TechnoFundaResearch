package com.app.technofunda.planshow;

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
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.app.technofunda.R;
import com.app.technofunda.ethiqs.Validate;
import com.app.technofunda.util.Config;

public class PlanShowActivity extends AppCompatActivity
{
    OTPSessionManager otpsession;
    SharedPreferences pref,pref1;
    EditText name, emailid, mobile, cityname,sponcer;
    Button submit;
    private ProgressDialog pDialog;
    String username, useremailid,usercity, nam, email, mob, city,Url,s,userloginid,userloginmobile,plan,sponcerid,sponcersid;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.planshow_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        otpsession = new OTPSessionManager(getApplicationContext());
        sponcer = (EditText) findViewById(R.id.your_sponcerid);
        name = (EditText) findViewById(R.id.your_name);
        emailid = (EditText) findViewById(R.id.your_email);
        mobile = (EditText) findViewById(R.id.your_mobile);
        cityname = (EditText) findViewById(R.id.your_city);
        submit = (Button) findViewById(R.id.btn_submit);

        pref1 = getSharedPreferences(Config.PREF_NAME1, Context.MODE_PRIVATE);
        if (pref1.contains(Config.KEY_OTPMOBILE)) {
            mob =pref1.getString(Config.KEY_OTPMOBILE, "");
        }
        Log.e("OTP MOBILE =", mob);
        mobile.setText(pref1.getString(Config.KEY_OTPMOBILE, ""));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sponcerid = sponcer.getText().toString().trim();
                username = name.getText().toString().trim();
                useremailid = emailid.getText().toString().trim();
                usercity = cityname.getText().toString().trim();

                boolean check = true;
                Validate validate = new Validate();

                if (validate.isNotEmpty(username)) {
                    sponcerid = sponcer.getText().toString();
                    sponcersid = sponcerid.replaceAll(" ", "%20");
                }
                else {
                    sponcerid = "";
                    check = false;
                    sponcer.setError("Please enter sponcer id");
                }
                if (validate.isNotEmpty(username)) {
                    username = name.getText().toString();
                    nam = username.replaceAll(" ", "-");
                }
                else {
                    username = "";
                    check = false;
                    name.setError("Please enter name");
                }
                if (validate.isValidEmail(useremailid)) {
                    useremailid = emailid.getText().toString();
                    email = useremailid.replaceAll(" ", "");
                }
                else {
                    useremailid = "";
                    check = false;
                    emailid.setError("Please enter valid E-mail Id");
                }

                if (validate.isNotEmpty(usercity)) {
                    usercity = cityname.getText().toString();
                    city = usercity.replaceAll(" ", "");
                }
                else {
                    usercity = "";
                    check = false;
                    cityname.setError("Please enter city name");
                }
                if (check) {
                    ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo nf = cn.getActiveNetworkInfo();
                    if (nf != null && nf.isConnected() == true) {
                        pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
                        if (pref.contains(Config.KEY_NAME)) {
                            userloginid=pref.getString(Config.KEY_NAME, "");
                        }
                        if (pref.contains(Config.KEY_MOBILE)) {
                            userloginmobile=pref.getString(Config.KEY_MOBILE, "");
                        }
                    plan = "planshow";
                    Url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(userloginmobile) + "&msg=" + String.valueOf(plan) + "%20" + String.valueOf(userloginid)+ "%20" + String.valueOf(sponcersid)+ "%20" + String.valueOf(nam)+ "%20" + String.valueOf(mob)+ "%20" + String.valueOf(email)+ "%20" + String.valueOf(city);
                    DownloadWebPageTask task1 = new DownloadWebPageTask();
                    task1.execute(new String[]{String.valueOf(Url)});
                        pDialog = new ProgressDialog(PlanShowActivity.this);
                        pDialog.setMessage("Please wait...");
                        pDialog.setCancelable(false);
                        pDialog.show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
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

    public class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            for (String url : urls)
            {
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

        protected void onPostExecute(String result) {
            s = Html.fromHtml(result).toString();
            if (pDialog.isShowing())
                pDialog.dismiss();

            AlertDialog.Builder builder2 = new AlertDialog.Builder(PlanShowActivity.this);
            if (s.contains("Registered Successfully")) {
                builder2.setTitle("PlanShow");
                builder2.setMessage("Registered Successfully");
                builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    otpsession.ClearOTP();
                    dialog.cancel();
                    finish();
                }
                });
                builder2.show();
            }
            else {
                builder2.setTitle("PlanShow");
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
}
