package com.app.technofunda.epaysystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.app.technofunda.R;
import com.app.technofunda.profile.ProfileActivity;
import com.app.technofunda.ethiqs.frontview;

public class EpaySystemActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    ImageView mobprepaid,mobpostpaid,electric,dth,metro,broadband,lindline,datacard,school,gas,insurance,water;
    ImageView movie,flight,bus,bike,car,hotel,deals;
    ImageView bazzar,samsung,phone,electronic,women,men,homekitchen,kids,sports,automotive;
    ImageView homef,updatef,logoutf,profilef;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainepay_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

       /* mobprepaid=(ImageView)findViewById(R.id.mob_prepaid);
        mobpostpaid=(ImageView)findViewById(R.id.mob_post);
        electric=(ImageView)findViewById(R.id.ele_city);
        dth=(ImageView)findViewById(R.id.dth_s);
        metro=(ImageView)findViewById(R.id.me_tro);
        broadband=(ImageView)findViewById(R.id.broad);
        lindline=(ImageView)findViewById(R.id.lind_line);
        datacard=(ImageView)findViewById(R.id.data_card);
        school=(ImageView)findViewById(R.id.sc_hool);
        gas=(ImageView)findViewById(R.id.g_as);
        insurance=(ImageView)findViewById(R.id.insu_rance);
        water=(ImageView)findViewById(R.id.w_ater);

        movie=(ImageView)findViewById(R.id.m_ovie);
        flight=(ImageView)findViewById(R.id.f_light);
        bus=(ImageView)findViewById(R.id.b_us);
        bike=(ImageView)findViewById(R.id.bikes);
        car=(ImageView)findViewById(R.id.cars);
        hotel=(ImageView)findViewById(R.id.h_otel);
        deals=(ImageView)findViewById(R.id.d_eal);

        bazzar=(ImageView)findViewById(R.id.ba_zzars);
        samsung=(ImageView)findViewById(R.id.sam_sung);
        phone=(ImageView)findViewById(R.id.phones);
        electronic=(ImageView)findViewById(R.id.ele_tronic);
        women=(ImageView)findViewById(R.id.wo_men);
        men=(ImageView)findViewById(R.id.m_en);
        homekitchen=(ImageView)findViewById(R.id.h_ome);
        kids=(ImageView)findViewById(R.id.k_ids);
        sports=(ImageView)findViewById(R.id.s_port);
        automotive=(ImageView)findViewById(R.id.automo);*/

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

               /* if(menuItem.getTitle().equals("Advertisement")) {
                    Intent inten = new Intent(utility_nev.this, AdvertiseActivitys.class);
                    startActivity(inten);
                }
                */
                return true;
            }
        });
        setTitle("E-Pay Coming Soon....");

        //footer
        homef=(ImageView)findViewById(R.id.home_footer);
        homef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EpaySystemActivity.this,frontview.class);
                startActivity(intent);
            }
        });
//        updatef=(ImageView)findViewById(R.id.update_footer);
//        updatef.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(EpaySystemActivity.this,NewsUpdateActivity.class);
//                startActivity(intent);
//
//            }
//        });
        logoutf=(ImageView)findViewById(R.id.logout_footer);
        logoutf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EpaySystemActivity.this);
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
                Intent intent=new Intent(EpaySystemActivity.this,ProfileActivity.class);
                startActivity(intent);

            }
        });


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
}
