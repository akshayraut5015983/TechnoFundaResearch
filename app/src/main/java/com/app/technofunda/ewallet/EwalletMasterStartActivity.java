package com.app.technofunda.ewallet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.technofunda.R;
import com.app.technofunda.profile.ProfileActivity;
import com.app.technofunda.ethiqs.frontview;

public class EwalletMasterStartActivity extends AppCompatActivity {

    TextView textviewf,ewalletbal,ewalletpayment,textviewfrt,textviewfth;
    View first,fifth;
    ImageView homef,updatef,logoutf,profilef;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_start_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Typeface pacificoFont = Typeface.createFromAsset(getAssets(), "arialnarrow.ttf");

        first=(View)findViewById(R.id.view_first);
        first.setVisibility(View.INVISIBLE);

        fifth=(View)findViewById(R.id.view_fifth);
        fifth.setVisibility(View.INVISIBLE);

        textviewf=(TextView)findViewById(R.id.text_view_f);
        textviewf.setVisibility(View.INVISIBLE);

        textviewfth=(TextView)findViewById(R.id.text_view_fith);
        textviewfth.setVisibility(View.INVISIBLE);

//        ewalletbal=(TextView)findViewById(R.id.text_view_s);
//        ewalletbal.setText("E-Wallet Balance");
//        ewalletbal.setTypeface(pacificoFont);

        ewalletpayment=(TextView)findViewById(R.id.text_view_t);
        ewalletpayment.setText("E-Wallet Payment");
        ewalletpayment.setTypeface(pacificoFont);

      //  textviewfrt=(TextView)findViewById(R.id.text_view_fr);
      //  textviewfrt.setText("Pin Transfer");
       // textviewfrt.setTypeface(pacificoFont);

        ewalletbal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EwalletActivitys.class);
                startActivity(i);
            }
        });

        ewalletpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EwalletPaymentActivitys.class);
                startActivity(i);
            }
        });

      /*  textviewfrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PinTransferActivity.class);
                startActivity(i);
            }
        });*/

      //footer
        homef=(ImageView)findViewById(R.id.home_footer);
        homef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EwalletMasterStartActivity.this,frontview.class);
                startActivity(intent);
            }
        });
//        updatef=(ImageView)findViewById(R.id.update_footer);
//        updatef.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(EwalletMasterStartActivity.this,NewsUpdateActivity.class);
//                startActivity(intent);
//
//            }
//        });
        logoutf=(ImageView)findViewById(R.id.logout_footer);
        logoutf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EwalletMasterStartActivity.this);
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
                Intent intent=new Intent(EwalletMasterStartActivity.this,ProfileActivity.class);
                startActivity(intent);

            }
        });


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
}
