package com.app.technofunda.planshow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.technofunda.R;
import com.app.technofunda.ethiqs.Validate;
import com.app.technofunda.util.Config;

public class PlanshowOTPActivity extends AppCompatActivity {

    OTPSessionManager otpses;
    SharedPreferences pref1;
    EditText otp;
    Button otpsubmit;
    TextView newotp;
    String otpno,otpcheck,otpsession,otps;
    Animation animBlink;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planshow_otp_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        otpses = new OTPSessionManager(getApplicationContext());
        otp = (EditText) findViewById(R.id.your_otp);
        otpsubmit = (Button) findViewById(R.id.btn_otp_submit);
        animBlink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        newotp=(TextView)findViewById(R.id.new_otp);
        newotp.setVisibility(View.VISIBLE);
        newotp.startAnimation(animBlink);

        otpsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpno = otp.getText().toString().trim();

                boolean check = true;
                Validate validate = new Validate();

                if (validate.isNotEmpty(otpno)) {
                    otpno = otp.getText().toString();
                    otpcheck = otpno.replaceAll(" ", "");
                } else {
                    otpno = "";
                    check = false;
                    otp.setError("Please enter OTP number");
                }

                pref1 = getSharedPreferences(Config.PREF_NAME1, Context.MODE_PRIVATE);
                if (pref1.contains(Config.KEY_OTP)) {
                    otpsession = pref1.getString(Config.KEY_OTP, "");
                    otps=otpsession.toString().trim();
                }
                if (check) {
                    Log.e("OTP SESSION=", otps);
                    Log.e("OTP otpcheck=", otpcheck);

                    if (otpcheck.toString().equals(otps)) {
                        Intent inten = new Intent(getApplicationContext(), PlanShowActivity.class);
                        startActivity(inten);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Enter Correct OTP Number", Toast.LENGTH_LONG).show();
                    }
                    otp.setText("");
                }
            }
        });

        newotp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                otpses.ClearOTP();
                finish();
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
