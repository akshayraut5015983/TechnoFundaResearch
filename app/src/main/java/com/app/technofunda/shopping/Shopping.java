package com.app.technofunda.shopping;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.technofunda.*;
import com.app.technofunda.profile.ProfileActivity;
import com.app.technofunda.ethiqs.frontview;

public class Shopping extends AppCompatActivity {
    ImageView imgmobilephone,imgwathch,imgdress,imgsofa,imgsound;
    Button shopcategory,Viewall;
    EditText edt_search;
    ImageView homef,updatef,logoutf,profilef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        imgmobilephone=(ImageView)findViewById(R.id.mobilephone);
      //  imgwathch=(ImageView)findViewById(R.id.watch);
       // imgdress=(ImageView)findViewById(R.id.dress);
//        imgsofa=(ImageView)findViewById(R.id.sofa);
      //  imgsound=(ImageView)findViewById(R.id.sound);
        imgmobilephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Shopping.this,shopping_details.class);
                startActivity(intent);
            }
        });

        shopcategory=(Button)findViewById(R.id.shopbycategory);
        shopcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Shopping.this,category_list.class);
                startActivity(intent);
            }
        });

        Viewall=(Button)findViewById(R.id.btnviewall);
        Viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Shopping.this,grid_categery.class);
                startActivity(intent);
            }
        });

        homef=(ImageView)findViewById(R.id.home_footer);
        homef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Shopping.this,frontview.class);
                startActivity(intent);
            }
        });
//        updatef=(ImageView)findViewById(R.id.update_footer);
//        updatef.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(Shopping.this,frontview.class);
//                startActivity(intent);
//
//            }
//        });
        logoutf=(ImageView)findViewById(R.id.logout_footer);
        logoutf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Shopping.this);
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
                Intent intent=new Intent(Shopping.this,ProfileActivity.class);
                startActivity(intent);

            }
        });



    }
}
