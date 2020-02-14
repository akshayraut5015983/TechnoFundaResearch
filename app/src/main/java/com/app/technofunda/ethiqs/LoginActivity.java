package com.app.technofunda.ethiqs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.technofunda.R;
import com.app.technofunda.util.Config;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoginActivity extends AppCompatActivity {

    Button login, registration_btn;
    String s, mobile, uid1, pwd1, userid, upass, mno;
    SessionManager session;
    EditText uid, pwd, mbno;
    private ProgressDialog pDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(getApplicationContext());

        uid = (EditText) findViewById(R.id.user_ids);
        pwd = (EditText) findViewById(R.id.userpswd);
        mbno = (EditText) findViewById(R.id.usr_mobile_no);

        login = (Button) findViewById(R.id.btnlogin);
        registration_btn = (Button) findViewById(R.id.registrationbtn);
        registration_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), NewRegistration.class);
                startActivity(i);
            }
        });
        if (session.isLoggedIn() == true) {
            Intent i = new Intent(getApplicationContext(), frontview.class);
            startActivity(i);
            finish();
        } else {

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 /*   Intent in = new Intent(LoginActivity.this, frontview.class);
                    startActivity(in);
                    session.createLoginSession("sai", "9876543210", "Aska123");*/

                    uid1 = uid.getText().toString().trim();
                    pwd1 = pwd.getText().toString().trim();
                    mno = mbno.getText().toString().trim();

                    userid = uid1.replaceAll(" ", "%20");
                    upass = pwd1.replaceAll(" ", "%20");
                    mobile = mno.replaceAll(" ", "%20");

                    if ((userid.length() == 0) && (upass.length() == 0) && (mobile.length() == 0)) {
                        Toast.makeText(getApplicationContext(), "Enter Loginid,Password and Mobile Number ", Toast.LENGTH_LONG).show();
                    } else if (userid.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Enter LoginId", Toast.LENGTH_LONG).show();
                    } else if (upass.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_LONG).show();
                    } else if (mobile.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Enter Registerd Mobile Number", Toast.LENGTH_LONG).show();
                    } else {
                        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo nf = cn.getActiveNetworkInfo();
                        if (nf != null && nf.isConnected() == true) {
                            //   http://technofundaresearch.com/api/apilogin.aspx?uid=tfr&pass=Omom7799&mbl=7057255777
                            //   http://technofundaresearch.com/API/APIlogin.aspx?uid=TFR&pass=Omom7799&mbl=7517504777
                            String url = Config.URL + "API/APIlogin.aspx?uid=" + String.valueOf(userid) + "&pass=" + String.valueOf(upass) + "&mbl=" + String.valueOf(mobile);
                            DownloadWebPageTask task1 = new DownloadWebPageTask();
                            task1.execute(new String[]{String.valueOf(url)});
                            pDialog = new ProgressDialog(LoginActivity.this);
                            pDialog.setMessage("Please Wait Login Processing...");
                            pDialog.setCancelable(false);
                            pDialog.show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
                        }
                    }
                }

            });

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Are you sure want to Exit?");
        alertDialogBuilder.setMessage("Click yes to exit!").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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

                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            s = Html.fromHtml(result).toString();

            if (pDialog.isShowing())
                pDialog.dismiss();

            AlertDialog.Builder builder2 = new AlertDialog.Builder(LoginActivity.this);
            if (s.contains("Login Successfully")) {
                session.createLoginSession(userid, mobile, upass);
               /* builder2.setTitle("Login");
                builder2.setMessage("Login Successfully");
                builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
                builder2.show();*/
                Intent in = new Intent(LoginActivity.this, frontview.class);
                startActivity(in);
                finish();
                Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
            } else {
                builder2.setTitle("Login");
                builder2.setMessage(String.valueOf(s).toString().trim());
                builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder2.show();
            }
        }
    }
}