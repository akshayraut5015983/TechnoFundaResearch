package com.app.technofunda.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.technofunda.R;
import com.app.technofunda.booking.flight_booking;
import com.app.technofunda.ewallet.EwalletMasterStartActivity;
import com.app.technofunda.profile.ProfileActivity;
import com.app.technofunda.recharge.MultiRechargeActivity;
import com.app.technofunda.ethiqs.SessionManager;
import com.app.technofunda.ethiqs.frontview;

public class Utility extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
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
        setContentView(R.layout.activity_utility);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        session = new SessionManager(getApplicationContext());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //footter
        homef=(ImageView)findViewById(R.id.home_footer);
        homef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Utility.this,frontview.class);
                startActivity(intent);
            }
        });
//        updatef=(ImageView)findViewById(R.id.update_footer);
//        updatef.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(Utility.this,NewsUpdateActivity.class);
//                startActivity(intent);
//
//            }
//        });
        logoutf=(ImageView)findViewById(R.id.logout_footer);
        logoutf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nf = cn.getActiveNetworkInfo();
                if (nf != null && nf.isConnected() == true) {
                    progressBar = ProgressDialog.show(Utility.this, "Please wait ...", "Logout processing ...", false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                            } catch (Exception e) {
                            }
                            session.logoutUser();
                            progressBar.dismiss();
                            Utility.this.finish();
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
                Intent intent=new Intent(Utility.this,ProfileActivity.class);
                startActivity(intent);

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

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Utility.this,frontview.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.utility, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent intent=new Intent(Utility.this,frontview.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mobile) {
            // Handle the camera action
            Intent inten = new Intent(Utility.this, MultiRechargeActivity.class);
            startActivity(inten);
        } else if (id == R.id.nav_eletricity) {

            Intent inten = new Intent(Utility.this, MultiRechargeActivity.class);
            startActivity(inten);
        } else if (id == R.id.nav_DTH) {
            Intent inten = new Intent(Utility.this, MultiRechargeActivity.class);
            startActivity(inten);

        } else if (id == R.id.nav_datacard) {
            Intent inten = new Intent(Utility.this, MultiRechargeActivity.class);
            startActivity(inten);

        } else if (id == R.id.nav_bill) {
            Intent inten = new Intent(Utility.this, MultiRechargeActivity.class);
            startActivity(inten);

        } else if (id == R.id.nav_broadband) {
            Intent inten = new Intent(Utility.this, MultiRechargeActivity.class);
            startActivity(inten);

        }else if (id == R.id.nav_flight) {
            Intent inten = new Intent(Utility.this, flight_booking.class);
            startActivity(inten);

        } else if (id == R.id.nav_train) {
            Intent inten = new Intent(Utility.this, flight_booking.class);
            startActivity(inten);

        } else if (id == R.id.nav_bus) {
            Intent inten = new Intent(Utility.this, flight_booking.class);
            startActivity(inten);

        } else if (id == R.id.nav_hotel) {
            Intent inten = new Intent(Utility.this, flight_booking.class);
            startActivity(inten);

        } else if (id == R.id.nav_cab) {
            Intent inten = new Intent(Utility.this, flight_booking.class);
            startActivity(inten);

        }else if(id== R.id.logout)
        {
            ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nf = cn.getActiveNetworkInfo();
            if (nf != null && nf.isConnected() == true) {
                progressBar = ProgressDialog.show(Utility.this, "Please wait ...", "Logout processing ...", false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (Exception e) {
                        }
                        session.logoutUser();
                        progressBar.dismiss();
                        Utility.this.finish();
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
}
