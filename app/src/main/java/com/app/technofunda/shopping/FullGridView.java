package com.app.technofunda.shopping;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.technofunda.R;
import com.app.technofunda.ethiqs.SessionManager;
import com.app.technofunda.ethiqs.frontview;
import com.app.technofunda.newsupdate.NewsUpdateActivity;
import com.app.technofunda.profile.ProfileActivity;
import com.app.technofunda.util.Config;

public class FullGridView extends AppCompatActivity {
    ImageView imgfull_product,like,imgcart;
    Button buy,addtocart;
    TextView txtlike,Price,ProductName;
    int counter=0;
    ImageView homef,updatef,logoutf,profilef;

    ProgressDialog progressBar;
    SessionManager session;
    SharedPreferences pref;
    Toolbar toolbar;
    String tokens,loginid,mobilenumber,passwords,token,s,id,name,price;

    RatingBar rb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_grid_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        imgcart=(ImageView)findViewById(R.id.cart);
        imgcart.setVisibility(View.VISIBLE);

        buy = (Button) findViewById(R.id.btnbuy);
        addtocart = (Button) findViewById(R.id.btnaddtocart);
        imgfull_product = (ImageView) findViewById(R.id.imgfull);
        rb = (RatingBar)findViewById(R.id.ratingBar);

        Price=(TextView)findViewById(R.id.price);
        ProductName=(TextView)findViewById(R.id.productname);
        txtlike=(TextView)findViewById(R.id.like);
        like=(ImageView)findViewById(R.id.imglike);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = counter + 1;
                display(counter);
            }
        });

        Intent intent = getIntent();
        final int imagePath = intent.getIntExtra("IMAGE_PATH",R.mipmap.ic_launcher);
        imgfull_product.setImageResource(imagePath);


        buy.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    Intent intent1 = new Intent(FullGridView.this,CurrentLocation.class);
                    startActivity(intent1);
                    Toast.makeText(FullGridView.this, "Image selected", Toast.LENGTH_SHORT).show();
                }
                catch (Exception io)
                {
                    Toast.makeText(FullGridView.this, "Failed to Set Wallpaper", Toast.LENGTH_SHORT).show();
                    io.printStackTrace();
                }
            }
        });

        //footer
        homef=(ImageView)findViewById(R.id.home_footer);
        homef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FullGridView.this,frontview.class);
                startActivity(intent);
            }
        });
        updatef=(ImageView)findViewById(R.id.update_footer);
        updatef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FullGridView.this, NewsUpdateActivity.class);
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
                    progressBar = ProgressDialog.show(FullGridView.this, "Please wait ...", "Logout processing ...", false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                            } catch (Exception e) {
                            }
                            session.logoutUser();
                            progressBar.dismiss();
                            FullGridView.this.finish();
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
                Intent intent=new Intent(FullGridView.this,ProfileActivity.class);
                startActivity(intent);

            }
        });


    }
    private void display(int number) {
        txtlike = (TextView) findViewById(R.id.like);
        txtlike.setText(number);

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

