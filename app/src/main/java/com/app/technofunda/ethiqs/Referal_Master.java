package com.app.technofunda.ethiqs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.technofunda.R;
import com.app.technofunda.direct.DirectActivitys;
import com.app.technofunda.ewallet.EwalletPaymentActivitys;
import com.app.technofunda.ewallet.Income_wallet;
import com.app.technofunda.ewallet.PointEwallet;
import com.app.technofunda.myincome.BinaryIncomeActivity;
import com.app.technofunda.myincome.DirectIncomeActivity;
import com.app.technofunda.myincome.IncomeHistoryActivity;
import com.app.technofunda.myincome.Income_report;
import com.app.technofunda.myincome.SSIncomeActivity;
import com.app.technofunda.newsupdate.NewsUpdateActivity;
import com.app.technofunda.pinmaster.Pinmaster;
import com.app.technofunda.profile.ProfileActivity;
import com.app.technofunda.recharge.MultiRechargeActivity;
import com.app.technofunda.refer.ReferActivity;
import com.app.technofunda.team.Team_main;
import com.app.technofunda.topuplist.TopupStartActivity;
import com.app.technofunda.util.Config;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Referal_Master extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    ImageView registration, topup, workingwallet, incomeewallet, newsupdate, teams, direct, incomereport, refer, pinmaster, home;
    ProgressDialog progressBar;
    SessionManager session;
    SharedPreferences pref;
    String tokens, loginid, mobilenumber, passwords, token, s;
    ImageView homef, updatef, logoutf, profilef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referal__master);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        session = new SessionManager(getApplicationContext());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

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

        //Logout Session
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            registration = (ImageView) findViewById(R.id.imgview_regi);
            topup = (ImageView) findViewById(R.id.imgview_topup);
            workingwallet = (ImageView) findViewById(R.id.imgview_workingwallet);
            incomereport = (ImageView) findViewById(R.id.img_incomereport);
            incomeewallet = (ImageView) findViewById(R.id.imgview_wallet);
            newsupdate = (ImageView) findViewById(R.id.imgview_newsandupdates);
            direct = (ImageView) findViewById(R.id.imgview_direct);
            teams = (ImageView) findViewById(R.id.imgview_userteam);
            refer = (ImageView) findViewById(R.id.imgview_refer);
            pinmaster = (ImageView) findViewById(R.id.imgview_pinmaster);

//            home = (ImageView) findViewById(R.id.homeicon);
//            home.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(Referal_Master.this, frontview.class);
//                    startActivity(intent);
//                }
//            });
            findViewById(R.id.cardRegi).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), NewRegistration.class);
                    startActivity(i);
                }
            });
            findViewById(R.id.cardRefLink).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), ReferActivity.class);
                    startActivity(i);
                }
            });
            findViewById(R.id.cardTeam).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), Team_main.class);
                    startActivity(i);
                }
            });
            findViewById(R.id.cardDairect).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), DirectActivitys.class);
                    startActivity(i);
                }
            });
            findViewById(R.id.cardIncomWalllet).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Referal_Master.this, Income_wallet.class);
                    startActivity(intent);
                }
            });
            findViewById(R.id.cardReperWallet).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Referal_Master.this, PointEwallet.class);
                    startActivity(intent);
                }
            });
            findViewById(R.id.cardIncomeReport).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Referal_Master.this, Income_report.class);
                    startActivity(intent);
                }
            });
            findViewById(R.id.cardTopUp).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), TopupStartActivity.class);
                    startActivity(i);
                }
            });
            findViewById(R.id.cardPinMaster).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), Pinmaster.class);
                    startActivity(i);
                }
            });
            findViewById(R.id.cardNewUpdate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), NewsUpdateActivity.class);
                    startActivity(i);
                }
            });
           /* topup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            direct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), DirectActivitys.class);
                    startActivity(i);
                }
            });
            workingwallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), PointEwallet.class);
                    startActivity(i);
                }
            });
            refer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            incomeewallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Referal_Master.this, Income_wallet.class);
                    startActivity(intent);
                }
            });
            newsupdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), NewsUpdateActivity.class);
                    startActivity(i);
                }
            });
            teams.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), Team_main.class);
                    startActivity(i);
                }
            });
            incomereport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Referal_Master.this, Income_report.class);
                    startActivity(intent);
                }
            });
            pinmaster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Referal_Master.this, Pinmaster.class);
                    startActivity(intent);
                }
            });*/

            //Footer
            homef = (ImageView) findViewById(R.id.home_footer);
            homef.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Referal_Master.this, frontview.class);
                    startActivity(intent);
                }
            });
            updatef = (ImageView) findViewById(R.id.update_footer);
            updatef.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Referal_Master.this, NewsUpdateActivity.class);
                    startActivity(intent);

                }
            });
            logoutf = (ImageView) findViewById(R.id.logout_footer);
            logoutf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo nf = cn.getActiveNetworkInfo();
                    if (nf != null && nf.isConnected() == true) {
                        progressBar = ProgressDialog.show(Referal_Master.this, "Please wait ...", "Logout processing ...", false);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                } catch (Exception e) {
                                }
                                session.logoutUser();
                                progressBar.dismiss();
                                Referal_Master.this.finish();
                            }
                        }).start();

                    } else {
                        Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
                    }
                }
            });
            profilef = (ImageView) findViewById(R.id.profile_footer);
            profilef.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Referal_Master.this, ProfileActivity.class);
                    startActivity(intent);

                }
            });


        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        Intent intent = new Intent(Referal_Master.this, frontview.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.referal__master, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
            case R.id.menu_item:
                Intent in = new Intent(Referal_Master.this, frontview.class);
                startActivity(in);
        }

        return super.onOptionsItemSelected(item);
    }

    private String getCurrentVersion() {
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;
        try {
            pInfo = pm.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        String currentVersion = pInfo.versionName;
        return currentVersion;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_news) {
            // Handle the camera action
            Intent inten = new Intent(Referal_Master.this, NewsUpdateActivity.class);
            startActivity(inten);
        } else if (id == R.id.nav_profile) {

            Intent inten = new Intent(Referal_Master.this, ProfileActivity.class);
            startActivity(inten);
        } else if (id == R.id.nav_incomewallet) {
            Intent intent = new Intent(Referal_Master.this, Income_wallet.class);
            startActivity(intent);
        } else if (id == R.id.nav_pwallet) {
            Intent inten = new Intent(Referal_Master.this, PointEwallet.class);
            startActivity(inten);

        } else if (id == R.id.nav_ewallet_payment) {
            Intent inten = new Intent(Referal_Master.this, MultiRechargeActivity.class);
            startActivity(inten);

        } else if (id == R.id.nav_epay) {
            Intent inten = new Intent(Referal_Master.this, EwalletPaymentActivitys.class);
            startActivity(inten);

        } else if (id == R.id.nav_direct) {
            Intent inten = new Intent(Referal_Master.this, DirectActivitys.class);
            startActivity(inten);

        } else if (id == R.id.nav_team) {
            Intent inten = new Intent(Referal_Master.this, Team_main.class);
            startActivity(inten);

        } else if (id == R.id.nav_binaryincome) {
            Intent inten = new Intent(Referal_Master.this, BinaryIncomeActivity.class);
            startActivity(inten);

        } else if (id == R.id.nav_directincome) {
            Intent inten = new Intent(Referal_Master.this, DirectIncomeActivity.class);
            startActivity(inten);

        } else if (id == R.id.nav_ssincome) {
            Intent inten = new Intent(Referal_Master.this, SSIncomeActivity.class);
            startActivity(inten);

        } else if (id == R.id.nav_referalincome) {
            Intent inten = new Intent(Referal_Master.this, ReferActivity.class);
            startActivity(inten);

        } else if (id == R.id.nav_incomehistory) {
            Intent inten = new Intent(Referal_Master.this, IncomeHistoryActivity.class);
            startActivity(inten);

        } else if (id == R.id.nav_join) {
            Intent inten = new Intent(Referal_Master.this, NewRegistration.class);
            startActivity(inten);

        } else if (id == R.id.nav_refer) {
            Intent inten = new Intent(Referal_Master.this, ReferActivity.class);
            startActivity(inten);
        } else if (id == R.id.nav_topup) {
            Intent inten = new Intent(Referal_Master.this, TopupStartActivity.class);
            startActivity(inten);

        } else if (id == R.id.nav_logout) {
            ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nf = cn.getActiveNetworkInfo();
            if (nf != null && nf.isConnected() == true) {
                progressBar = ProgressDialog.show(Referal_Master.this, "Please wait ...", "Logout processing ...", false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (Exception e) {
                        }
                        session.logoutUser();
                        progressBar.dismiss();
                        Referal_Master.this.finish();
                    }
                }).start();

            } else {
                Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                AlertDialog.Builder builder2 = new AlertDialog.Builder(Referal_Master.this);
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
