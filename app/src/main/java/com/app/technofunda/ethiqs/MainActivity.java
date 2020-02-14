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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.app.technofunda.R;
import com.app.technofunda.direct.DirectActivitys;
import com.app.technofunda.ewallet.EwalletPaymentActivitys;
import com.app.technofunda.ewallet.PinGenerate;
import com.app.technofunda.ewallet.PointEwallet;
import com.app.technofunda.myincome.BinaryIncomeActivity;
import com.app.technofunda.myincome.DirectIncomeActivity;
import com.app.technofunda.myincome.IncomeHistoryActivity;
import com.app.technofunda.myincome.Income_report;
import com.app.technofunda.myincome.ReferralIncomeActivity;
import com.app.technofunda.myincome.SSIncomeActivity;
import com.app.technofunda.newsupdate.NewsUpdateActivity;
import com.app.technofunda.pinmaster.*;
import com.app.technofunda.profile.ProfileActivity;
import com.app.technofunda.refer.ReferActivity;
import com.app.technofunda.team.TeamActivitys;
import com.app.technofunda.topuplist.TopupStartActivity;
import com.app.technofunda.util.Config;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    ImageView registration,topup,workingwallet,incomeewallet,newsupdate,teams,direct,incomereport,refer,pinmaster,home;
    ProgressDialog progressBar;
    SessionManager session;
    SharedPreferences pref;
    String tokens,loginid,mobilenumber,passwords,token,s;
    ImageView homef,updatef,logoutf,profilef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            loginid=pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            mobilenumber=pref.getString(Config.KEY_MOBILE, "");
        }
        if (pref.contains(Config.KEY_PASSWORD)) {
            passwords=pref.getString(Config.KEY_PASSWORD, "");
        }

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        toolbar.setTitle("Welcome " + loginid);

        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();

                   if (menuItem.getTitle().equals("News And Update")) {
                        Intent inten = new Intent(MainActivity.this, NewsUpdateActivity.class);
                        startActivity(inten);
                    } else if (menuItem.getTitle().equals("Binary Income")) {
                        Intent inten = new Intent(MainActivity.this, BinaryIncomeActivity.class);
                        startActivity(inten);
                    } else if (menuItem.getTitle().equals("Direct Income")) {
                        Intent inten = new Intent(MainActivity.this, DirectIncomeActivity.class);
                        startActivity(inten);
                    } else if (menuItem.getTitle().equals("Referral Income")) {
                        Intent inten = new Intent(MainActivity.this, ReferralIncomeActivity.class);
                        startActivity(inten);
                    } else if (menuItem.getTitle().equals("SS Income")) {
                        Intent inten = new Intent(MainActivity.this, SSIncomeActivity.class);
                        startActivity(inten);
                    } else if (menuItem.getTitle().equals("Income history")) {
                        Intent inten = new Intent(MainActivity.this, IncomeHistoryActivity.class);
                        startActivity(inten);
                    } else if (menuItem.getTitle().equals("Profile")) {
                        Intent inten = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(inten);
                    } else if (menuItem.getTitle().equals("Income Ewallet")) {
                        PinGenerate pinGenerate = new PinGenerate();
                       FragmentManager manager = getSupportFragmentManager();
                       FragmentTransaction transaction = manager.beginTransaction();
                       transaction.add(R.id.pingeneration,pinGenerate);
                       transaction.commit();

                   } else if (menuItem.getTitle().equals("Working Ewallet")) {
                        Intent inten = new Intent(MainActivity.this, PointEwallet.class);
                        startActivity(inten);
                    } else if (menuItem.getTitle().equals("E-Wallet Payment")) {
                        Intent inten = new Intent(MainActivity.this, EwalletPaymentActivitys.class);
                        startActivity(inten);
                    } else if (menuItem.getTitle().equals("My Direct")) {
                        Intent inten = new Intent(MainActivity.this, DirectActivitys.class);
                        startActivity(inten);
                    } else if (menuItem.getTitle().equals("Total Team")) {
                        Intent inten = new Intent(MainActivity.this, TeamActivitys.class);
                        startActivity(inten);
                    } else if (menuItem.getTitle().equals("Topup")) {
                        Intent inten = new Intent(MainActivity.this, TopupStartActivity.class);
                        startActivity(inten);
                    } else if (menuItem.getTitle().equals("Join Now")) {
                        Intent inten = new Intent(MainActivity.this, com.app.technofunda.ethiqs.NewRegistration.class);
                        startActivity(inten);
                    } else if (menuItem.getTitle().equals("Refer")) {
                        Intent inten = new Intent(MainActivity.this, ReferActivity.class);
                        startActivity(inten);
                        return true;
                    } else if (menuItem.getTitle().equals("LogOut")) {
                        progressBar = ProgressDialog.show(MainActivity.this, "Please wait ...", "Logout processing ...", false);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                } catch (Exception e) {
                                }
                                session.logoutUser();
                                progressBar.dismiss();
                                MainActivity.this.finish();
                            }
                        }).start();
                    }
                    return true;
                }
            });

            registration = (ImageView) findViewById(R.id.imgview_regi);
            topup = (ImageView) findViewById(R.id.imgview_topup);
            workingwallet = (ImageView) findViewById(R.id.imgview_workingwallet);
            incomereport = (ImageView) findViewById(R.id.img_incomereport);
            incomeewallet = (ImageView) findViewById(R.id.imgview_wallet);
            newsupdate = (ImageView) findViewById(R.id.imgview_newsandupdates);
            direct = (ImageView) findViewById(R.id.imgview_direct);
            teams = (ImageView) findViewById(R.id.imgview_userteam);
            refer=(ImageView)findViewById(R.id.imgview_refer);
            pinmaster=(ImageView)findViewById(R.id.imgview_pinmaster);

            home=(ImageView)findViewById(R.id.homeicon);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MainActivity.this,frontview.class);
                    startActivity(intent);
                }
            });

            registration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), com.app.technofunda.ethiqs.NewRegistration.class);
                    startActivity(i);
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
                    Intent i = new Intent(getApplicationContext(), ReferActivity.class);
                    startActivity(i);
                }
            });
            incomeewallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PinGenerate pinGenerate = new PinGenerate();
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.add(R.id.pingeneration,pinGenerate);
                    transaction.commit();

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
                    Intent i = new Intent(getApplicationContext(), TeamActivitys.class);
                    startActivity(i);
                }
            });
            incomereport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MainActivity.this,Income_report.class);
                    startActivity(intent);
                }
            });
            pinmaster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MainActivity.this,Pinmaster.class);
                    startActivity(intent);
                }
            });

            //Footer
            homef=(ImageView)findViewById(R.id.home_footer);
            homef.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MainActivity.this,frontview.class);
                    startActivity(intent);
                }
            });
//            updatef=(ImageView)findViewById(R.id.update_footer);
//            updatef.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent=new Intent(MainActivity.this,NewsUpdateActivity.class);
//                    startActivity(intent);
//
//                }
//            });
            logoutf=(ImageView)findViewById(R.id.logout_footer);
            logoutf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertDialogBuilder.setTitle("Are you sure want to Exit?");
                    alertDialogBuilder.setMessage("Click yes to exit!").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
            });
            profilef=(ImageView)findViewById(R.id.profile_footer);
            profilef.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MainActivity.this,ProfileActivity.class);
                    startActivity(intent);

                }
            });


            //  FirebaseMessaging.getInstance().subscribeToTopic("test");
            // token= FirebaseInstanceId.getInstance().getToken();

        /*
        tokens="token";
        String url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(mobilenumber) + "&msg=" + String.valueOf(tokens) + "%20" + String.valueOf(loginid)+ "%20" + String.valueOf(token);
        DownloadWebPageTask task1 = new DownloadWebPageTask();
        task1.execute(new String[]{String.valueOf(url)});
        */

         /*   String latestVersion = "";
            String currentVersion = getCurrentVersion();
            Log.d("", "Current version = " + currentVersion);
            try {
                latestVersion = new GetLatestVersion().execute().get();
                Log.d("", "Latest version = " + latestVersion);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (!currentVersion.equals(latestVersion)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("An Update is Available");
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.app.dreamswaliya.&hl=en")));
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }

            String urls = Config.URL+"/api/apilogin.aspx?uid=" + String.valueOf(loginid) + "&pass=" + String.valueOf(passwords) + "&mbl=" + String.valueOf(mobilenumber);
            DownloadWebPageTask task2 = new DownloadWebPageTask();
            task2.execute(new String[]{String.valueOf(urls)});

        } else {
            Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
        }*/
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
            case R.id.menu_item:
                Intent in=new Intent(MainActivity.this,frontview.class);
                startActivity(in);
        }
        return super.onOptionsItemSelected(item);
    }

    private String getCurrentVersion() {
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;
        try {
            pInfo =  pm.getPackageInfo(this.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        String currentVersion = pInfo.versionName;
        return currentVersion;
    }

    @Override
    public void onBackPressed() {
     /*   AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Are you sure want to Exit?");
        alertDialogBuilder.setMessage("Click yes to exit!").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
        alertDialog.show();*/
        Intent intent=new Intent(MainActivity.this,frontview.class);
        startActivity(intent);
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
            if (s.contains("Login Failed")){
                AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                builder2.setTitle("Login");
                builder2.setMessage("LoginId or Mobile No. Not Exist");
                builder2.setPositiveButton("OK",new DialogInterface.OnClickListener() {
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
