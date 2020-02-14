package com.app.technofunda;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.app.technofunda.ethiqs.SessionManager;
import com.app.technofunda.util.Config;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AdminActivity extends AppCompatActivity {
    Button btnadminlogin;
    String s, uid1, pwd1, mno, userid, upass, mobile ;
    SessionManager session;
    EditText uid, pwd, mbno;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(getApplicationContext());

        uid = (EditText)findViewById(R.id.user_ids);
        pwd = (EditText)findViewById(R.id.userpswd);
        mbno = (EditText)findViewById(R.id.usr_mobile_no);
        btnadminlogin = (Button) findViewById(R.id.btnadminlogin);


            btnadminlogin.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    uid1 = uid.getText().toString().trim();
                    pwd1 = pwd.getText().toString().trim();
                    mno = mbno.getText().toString().trim();

                    userid = uid1.replaceAll(" ", "%20");
                    upass = pwd1.replaceAll(" ", "%20");
                    mobile = mno.replaceAll(" ", "%20");

                    if ((userid.length() == 0) && (upass.length() == 0) && (mobile.length() == 0)) {
                        Toast.makeText(getApplicationContext(), "Enter AdminId,Password and Mobile Number ", Toast.LENGTH_LONG).show();
                    } else if (userid.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Enter AdminId", Toast.LENGTH_LONG).show();
                    } else if (upass.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_LONG).show();
                    } else if (mobile.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Enter Registerd Mobile Number", Toast.LENGTH_LONG).show();
                    } else {
                        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo nf = cn.getActiveNetworkInfo();
                        if (nf != null && nf.isConnected() == true) {
                           // http://technofundaresearch.com/api/apilogin.aspx?uid=admin&pass=Omom@7799
                           // http://technofundaresearch.com/api/apilogin.aspx?uid=Admin&pass=Omom@7799&mbl=8888888820
                            String url = Config.URL + "/api/apilogin.aspx?uid=" + String.valueOf(userid) + "&pass=" + String.valueOf(upass) + "&mbl=" + String.valueOf(mobile);
                            AdminActivity.DownloadWebPageTask task1 = new AdminActivity.DownloadWebPageTask();
                            task1.execute(new String[]{String.valueOf(url)});
                            pDialog = new ProgressDialog(AdminActivity.this);
                            pDialog.setMessage("Please Wait Admin Processing...");
                            pDialog.setCancelable(false);
                            pDialog.show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
                        }
                    }
//                    Intent intent = new Intent(getApplicationContext(), Referal_Master.class);
//                    startActivity(intent);
                }

            });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

            AlertDialog.Builder builder2 = new AlertDialog.Builder(AdminActivity.this);
            if (s.contains("Admin Successfully")) {
                session.createLoginSession(userid, mobile, upass);
                builder2.setTitle("Admin");
                builder2.setMessage("Admin Successfully");
                builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
//                        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
//                        startActivity(intent);
                        finish();
                    }
                });
                builder2.show();

            } else {
                builder2.setTitle("Admin");
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

