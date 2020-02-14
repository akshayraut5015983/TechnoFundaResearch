package com.app.technofunda.utility;

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
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
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
import com.app.technofunda.booking.*;
import com.app.technofunda.ewallet.EwalletMasterStartActivity;
import com.app.technofunda.myincome.ReferralIncomeActivity;
import com.app.technofunda.recharge.*;
import com.app.technofunda.ethiqs.SessionManager;
import com.app.technofunda.ethiqs.frontview;
import com.app.technofunda.util.Config;

/**
 * Created by Administrator on 09/03/2017.
 */

public class utility_navigation extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    ImageView mobile1,dth1,flight1,train1,bus1,eletricity,cab,hotel1,datacard;
    ProgressDialog progressBar;
    SessionManager session;
    SharedPreferences pref;
    Toolbar toolbar;
    String tokens,loginid,mobilenumber,passwords,token,s;
    private TabLayout tabLayout;
    private ViewPager viewPager;
   FragmentTransaction fragmentTransaction;
    ImageView homef,updatef,logoutf,profilef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.utility_navigation_main);

        session = new SessionManager(getApplicationContext());

    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layoututility);
    navigationView = (NavigationView) findViewById(R.id.navigation_viewutility);

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


    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

                if (menuItem.getTitle().equals("Mobile")) {
                    Intent inten = new Intent(utility_navigation.this, MultiRechargeActivity.class);
                    startActivity(inten);
                } else if (menuItem.getTitle().equals("DTH")) {
                    Intent inten = new Intent(utility_navigation.this, MultiRechargeActivity.class);
                     viewPager= (ViewPager) findViewById(R.id.viewpager);
                    startActivity(inten);
                } else if (menuItem.getTitle().equals("Flight")) {
                    Intent inten = new Intent(utility_navigation.this,flight_booking.class);
                    startActivity(inten);
                } else if (menuItem.getTitle().equals("Bus")) {
                    Intent inten = new Intent(utility_navigation.this, flight_booking.class);
                    startActivity(inten);
                } else if (menuItem.getTitle().equals("Eletricity")) {
                    Intent inten = new Intent(utility_navigation.this, ReferralIncomeActivity.class);
                    startActivity(inten);
                } else if (menuItem.getTitle().equals("CAB")) {
                    Intent inten = new Intent(utility_navigation.this, flight_booking.class);
                    startActivity(inten);
                } else if (menuItem.getTitle().equals("Hotel")) {
                    Intent inten = new Intent(utility_navigation.this,flight_booking.class);
                    startActivity(inten);
                } else if (menuItem.getTitle().equals("Train")) {
                    Intent inten = new Intent(utility_navigation.this,flight_booking.class);
                    startActivity(inten);
                }
                else if ((menuItem.getTitle().equals("DataCard")))
                {
                    Intent intent=new Intent(utility_navigation.this, MultiRechargeActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

       mobile1 = (ImageView) findViewById(R.id.mobilerecharge);
        flight1 = (ImageView) findViewById(R.id.flight_book);
        cab = (ImageView) findViewById(R.id.cab);
        bus1 = (ImageView) findViewById(R.id.bus);
        dth1 = (ImageView) findViewById(R.id.dth);
        train1 = (ImageView) findViewById(R.id.train);
        eletricity = (ImageView) findViewById(R.id.electicity);
       hotel1=(ImageView)findViewById(R.id.hotel);
        datacard=(ImageView)findViewById(R.id.datacard);

       mobile1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(), MultiRechargeActivity.class);

               startActivity(intent);
           }
       });
       bus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),flight_booking.class);

                startActivity(i);
            }
        });
        cab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), flight_booking.class);
                startActivity(i);
            }
        });
        flight1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), flight_booking.class);
                startActivity(i);
            }
        });
        dth1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(getApplicationContext(),MultiRechargeActivity.class);
               startActivity(intent);
               // com.app.ethiqs.utility.DTHRecharge fragmentView=new com.app.ethiqs.utility.DTHRecharge();
               // FragmentManager fragmentManager=getSupportFragmentManager();
                //fragmentManager.beginTransaction()
                       // .add(R.id.frag,fragmentView).commit();
            }
        });
        eletricity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EwalletMasterStartActivity.class);
                startActivity(i);
            }
        });
        train1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), flight_booking.class);
                startActivity(i);
            }
        });
        hotel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),flight_booking.class);
                startActivity(intent);
            }
        });
        datacard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), MultiRechargeActivity.class);
                startActivity(intent);
            }
        });

    }

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
        Intent intent=new Intent(utility_navigation.this,frontview.class);
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
            AlertDialog.Builder builder2 = new AlertDialog.Builder(utility_navigation.this);
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
