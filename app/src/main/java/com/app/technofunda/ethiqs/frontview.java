package com.app.technofunda.ethiqs;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.technofunda.R;
import com.app.technofunda.marchantPay.Front_marchant;
import com.app.technofunda.newsupdate.NewsUpdateActivity;
import com.app.technofunda.profile.ProfileActivity;
import com.app.technofunda.shopping.Nav_Shopping;
import com.app.technofunda.util.Config;
import com.app.technofunda.utility.Gallery;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class frontview extends AppCompatActivity {

    ImageView imageback, shopping_icon, wallet1, logout, marchant_pay, website, utility, referalmaster;
    LinearLayout li;
    Animation animation;
    View footer;
    TextView appname;
    TextView txtappnametool;
    ImageView icon_home;
    ImageView homef, updatef, logoutf, profilef;
    ProgressDialog progressBar;
    SessionManager session;
    SharedPreferences pref;
    String loginid="", mobilenumber="", passwords="", s;
    Context context;
    ImageView homefooter, trend;
    TextView hometext;
    EditText admin_id, admin_password;
    Button btnadmin_login;
    Dialog adminbox;
    String adminid, adminpassword;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontview);
//
        appname = (TextView) findViewById(R.id.txtappname);
        appname.setText(R.string.app_name);

        session = new SessionManager(getApplicationContext());
        pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            loginid = pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            mobilenumber = pref.getString(Config.KEY_MOBILE, "");
        }
        if (pref.contains(Config.KEY_PASSWORD)) {
            passwords = pref.getString(Config.KEY_PASSWORD, "");
        }
        Log.d("id", loginid);
        Log.d("mob", mobilenumber);
        Log.d("pass", passwords);
        homefooter = (ImageView) findViewById(R.id.home_footer);
        hometext = (TextView) findViewById(R.id.hometextfooter);
        hometext.setVisibility(View.VISIBLE);
        homefooter.setVisibility(View.VISIBLE);

        utility = (ImageView) findViewById(R.id.referalutility);
        utility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frontview.this, Gallery.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.cardTrding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frontview.this, TrendingActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.cardRefMaster).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.adminbox, null);
                adminbox = new Dialog(frontview.this);
                adminbox.setContentView(dialoglayout);

                admin_id = (EditText) adminbox.findViewById(R.id.admin_id);
                admin_password = (EditText) adminbox.findViewById(R.id.admin_password);
                btnadmin_login = (Button) adminbox.findViewById(R.id.btnadmin_login);

                btnadmin_login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adminid = admin_id.getText().toString();
                        adminpassword = admin_password.getText().toString();

                        adminid = adminid.replaceAll(" ", "%20");
                        adminpassword = adminpassword.replaceAll(" ", "%20");

                        if ((adminid.length() == 0) && (adminpassword.length() == 0)) {
                            Toast.makeText(getApplicationContext(), "Enter AdminId,Password and Mobile Number ", Toast.LENGTH_LONG).show();
                        } else if (adminid.length() == 0) {
                            Toast.makeText(getApplicationContext(), "Enter AdminId", Toast.LENGTH_LONG).show();
                        } else if (adminpassword.length() == 0) {
                            Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_LONG).show();
                        } else {
                            ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo nf = cn.getActiveNetworkInfo();
                            if (nf != null && nf.isConnected() == true) {
                              /*  // http://technofundaresearch.com/api/apilogin.aspx?uid=admin&pass=Omom@7799
                                // http://technofundaresearch.com/api/apilogin.aspx?uid=Admin&pass=Omom@7799&mbl=8888888820
                                String url = Config.URL + "/api/apilogin.aspx?uid=" + String.valueOf(adminid) + "&pass=" + String.valueOf(adminpassword);
                                frontview.DownloadWebPageTask2 task2 = new frontview.DownloadWebPageTask2();
                                task2.execute(new String[]{String.valueOf(url)});
                                pDialog = new ProgressDialog(frontview.this);
                                pDialog.setMessage("Please Wait Admin Processing...");
                                pDialog.setCancelable(false);
                                pDialog.show();*/
                                Intent intent = new Intent(frontview.this, Referal_Master.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
                            }
                        }

                    }

                });

//                Intent intent = new Intent(frontview.this, Referal_Master.class);
//                startActivity(intent);
                adminbox.show();
            }
        });
        findViewById(R.id.cardWebsite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frontview.this, WebsiteActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.cardLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nf = cn.getActiveNetworkInfo();
                if (nf != null && nf.isConnected() == true) {
                    onLogout();

                } else {
                    Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
                }
            }
        });


//

        shopping_icon = (ImageView) findViewById(R.id.shopping);
        shopping_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frontview.this, Nav_Shopping.class);
                startActivity(intent);
            }
        });


        marchant_pay = (ImageView) findViewById(R.id.wallet);
        marchant_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frontview.this, Front_marchant.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.ftHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.ftUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frontview.this, NewsUpdateActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.ftProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frontview.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.ftLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nf = cn.getActiveNetworkInfo();
                if (nf != null && nf.isConnected() == true) {


                    onLogout();
                } else {
                    Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
                }
            }
        });

        String urls = Config.URL + "/api/apilogin.aspx?uid=" + String.valueOf(loginid) + "&pass=" + String.valueOf(passwords) + "&mbl=" + String.valueOf(mobilenumber);
        DownloadWebPageTask task2 = new DownloadWebPageTask();
        task2.execute(new String[]{String.valueOf(urls)});

    }

    private void onLogout() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(frontview.this, android.R.style.Theme_Dialog);
        alertDialogBuilder.setTitle("Are you sure want to Logout?");
        alertDialogBuilder.setMessage("Click yes to logout!").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                progressBar = ProgressDialog.show(frontview.this, "Please wait ...", "Logout processing ...", false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (Exception e) {
                        }
                        session.logoutUser();
                        progressBar.dismiss();
                        frontview.this.finish();
                    }
                }).start();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class DownloadWebPageTask2 extends AsyncTask<String, Void, String> {
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

            AlertDialog.Builder builder2 = new AlertDialog.Builder(frontview.this);
            if (s.contains("Login Successfully")) {
//                session.createLoginSession(adminid, adminpassword );
                builder2.setTitle("Login");
                builder2.setMessage("Login Successfully");
                builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(frontview.this, Referal_Master.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder2.show();

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
                }
            }
            return response;
        }

        protected void onPostExecute(String result) {
            s = Html.fromHtml(result).toString();
            if (s.contains("Login Failed")) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(frontview.this);
                builder2.setTitle("Login");
                builder2.setMessage("LoginId or Mobile No. Not Exist");
                builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        session.logoutUser();
                        finish();
                    }
                });
                builder2.show();
            }
        }
    }

}
