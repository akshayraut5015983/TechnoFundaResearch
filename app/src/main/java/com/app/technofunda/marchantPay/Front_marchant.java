package com.app.technofunda.marchantPay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.technofunda.*;
import com.app.technofunda.ethiqs.SessionManager;
import com.app.technofunda.newsupdate.NewsUpdateActivity;
import com.app.technofunda.profile.ProfileActivity;
import com.app.technofunda.ethiqs.frontview;
import com.app.technofunda.util.Config;

public class Front_marchant extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    WebView webView;
    TextView head;
    ImageView pay,add,nearby,addtobank,passbook,acceptmoney;
    ActionBar actionBar;
    ImageView homef,updatef,logoutf,profilef;
    SharedPreferences pref;
    ProgressDialog progressBar;
    SessionManager session;
    String loginid,mobilenumber,passwords,s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_marchant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  actionBar.setHomeButtonEnabled(true);

        session = new SessionManager(getApplicationContext());
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

        pay=(ImageView)findViewById(R.id.pay);
        add=(ImageView)findViewById(R.id.addmoney);
        nearby=(ImageView)findViewById(R.id.nearby);
        passbook=(ImageView)findViewById(R.id.passbook);
        addtobank=(ImageView)findViewById(R.id.addtobank);
        acceptmoney=(ImageView)findViewById(R.id.accepttmoney);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent intent=new Intent(Front_marchant.this, Main2Activity.class);
                //startActivity(intent);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        passbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        addtobank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        acceptmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
     //  webView=(WebView)findViewById(R.id.webpay);
      //  webView.loadUrl("https://d13yacurqjgara.cloudfront.net/users/381820/screenshots/2330745/________.gif");

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);
                head=(TextView)findViewById(R.id.txthead);
        head.startAnimation(animation);

        //footer
        homef=(ImageView)findViewById(R.id.home_footer);
        homef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Front_marchant.this,frontview.class);
                startActivity(intent);
            }
        });
        updatef=(ImageView)findViewById(R.id.update_footer);
        updatef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Front_marchant.this,NewsUpdateActivity.class);
                startActivity(intent);

            }
        });
        logoutf=(ImageView)findViewById(R.id.logout_footer);
        logoutf.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                           NetworkInfo nf = cn.getActiveNetworkInfo();
                                           if (nf != null && nf.isConnected() == true) {
                                               progressBar = ProgressDialog.show(Front_marchant.this, "Please wait ...", "Logout processing ...", false);
                                               new Thread(new Runnable() {
                                                   @Override
                                                   public void run() {
                                                       try {
                                                           Thread.sleep(2000);
                                                       } catch (Exception e) {
                                                       }
                                                       session.logoutUser();
                                                       progressBar.dismiss();
                                                       Front_marchant.this.finish();
                                                   }
                                               }).start();

                                           } else {
                                               Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
                                           }
                                       }
                                   });
        profilef=(ImageView)findViewById(R.id.profile_footer);
        profilef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Front_marchant.this,ProfileActivity.class);
                startActivity(intent);

            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.front_marchant, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pay) {
            // Handle the camera action
        } else if (id == R.id.nav_addmoney) {

        } else if (id == R.id.nav_passbook) {

        } else if (id == R.id.nav_acceptmoney) {

        } else if (id == R.id.nav_nearby) {

        }else if (id==R.id.nav_bank){

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
