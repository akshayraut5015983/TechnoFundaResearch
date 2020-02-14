package com.app.technofunda.planshow;

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


public class PlanshowMobileActivity extends AppCompatActivity {

    SharedPreferences pref;
    EditText mobileotp;
    Button submitmobile;
    String mobiles,mobs,userloginid,userloginmobile,otp,Url,s,s1;
    OTPSessionManager otpsession;
    private ProgressDialog pDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planshow_mobile_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        otpsession = new OTPSessionManager(getApplicationContext());
        mobileotp = (EditText) findViewById(R.id.your_mobile_for_otp);
        submitmobile = (Button) findViewById(R.id.btn_mobile_submit);

        if (otpsession.isOTPIn() == true) {
            Intent i = new Intent(getApplicationContext(), PlanshowOTPActivity.class);
            startActivity(i);
            finish();
        }
        else {
            submitmobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mobs = mobileotp.getText().toString().trim();
                    boolean check = true;
                    Validate validate = new Validate();
                    if (validate.isValidPhone(mobs)) {
                        mobs = mobileotp.getText().toString();
                        mobiles = mobs.replaceAll(" ", "%20");
                    }
                    else {
                        mobs = "";
                        check = false;
                        mobileotp.setError("Please enter mobile number");
                    }

                    if (check) {
                        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo nf = cn.getActiveNetworkInfo();
                        if (nf != null && nf.isConnected() == true) {
                            pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
                            if (pref.contains(Config.KEY_NAME)) {
                                userloginid = pref.getString(Config.KEY_NAME, "");
                            }
                            if (pref.contains(Config.KEY_MOBILE)) {
                                userloginmobile = pref.getString(Config.KEY_MOBILE, "");
                            }
                            otp = "otp";
                            Url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(userloginmobile) + "&msg=" + String.valueOf(otp) + "%20" + String.valueOf(userloginid) + "%20" + String.valueOf(mobiles);
                            DownloadWebPageTask task1 = new DownloadWebPageTask();
                            task1.execute(new String[]{String.valueOf(Url)});
                            pDialog = new ProgressDialog(PlanshowMobileActivity.this);
                            pDialog.setMessage("Please wait...");
                            pDialog.setCancelable(false);
                            pDialog.show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
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

    public class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            for (String url : urls)
            {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try
                {
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
            if (s.contains("Mobile Already Registered")) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(PlanshowMobileActivity.this);
                builder2.setTitle("Mobile Number");
                builder2.setMessage("Mobile Already Registered");
                builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                builder2.show();
            }
            else {
                s1=s.replaceAll(" ", "");
                otpsession.createOTPSession(s1, mobiles);
                Intent inten = new Intent(PlanshowMobileActivity.this, PlanshowOTPActivity.class);
                startActivity(inten);
                mobileotp.setText("");
                finish();
            }
        }
    }
}
