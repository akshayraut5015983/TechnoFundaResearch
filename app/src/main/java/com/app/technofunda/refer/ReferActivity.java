package com.app.technofunda.refer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.app.technofunda.R;
import com.app.technofunda.ethiqs.Validate;
import com.app.technofunda.util.Config;

public class ReferActivity extends AppCompatActivity {

    Spinner refposition;
    Button generate;
    EditText refmobile;
    String genpos,rmobile,refid;
    RadioGroup radiogroup;
    RadioButton radiowhatsapp;
    SharedPreferences pref;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.refer_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refposition = (Spinner) findViewById(R.id.refer_postion);
        refmobile = (EditText) findViewById(R.id.edit_ref_mobile);
        generate=(Button)findViewById(R.id.but_gen_refer);
        radiogroup=(RadioGroup)findViewById(R.id.choose_option);
        radiowhatsapp = (RadioButton) findViewById(R.id.radiobtn_whatsapp);

        List<String> list = new ArrayList<String>();
        list.add("Select Your Position");
        list.add("leftside");
        list.add("rightside");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        refposition.setAdapter(dataAdapter);
        refposition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (radiowhatsapp.isChecked()) {
                    refmobile.setVisibility(View.INVISIBLE);

                generate.setOnClickListener(new View.OnClickListener() {

                    @SuppressLint("ServiceCast")
                    @Override
                    public void onClick(View v) {

                        genpos = refposition.getSelectedItem().toString().trim();
                        boolean check = true;
                        if (genpos.equals("Select Your Position")) {
                            check = false;
                            Toast.makeText(getApplicationContext(), "Please Select your position", Toast.LENGTH_SHORT).show();
                        }
                        if (check) {
                            pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
                            if (pref.contains(Config.KEY_NAME)) {
                                refid = pref.getString(Config.KEY_NAME, "");
                            }
                            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                            whatsappIntent.setType("text/plain");
                            whatsappIntent.setPackage("com.whatsapp");
                            whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Download BigEmarrt app https://play.google.com/store/apps/details?id=com.app.dreamswaliya.&hl=en and to refer Website use this link http://income.bigemarrt.com and for your new registration to use reference id -->" + refid + " and position -->" + genpos);
                            try {
                                startActivity(whatsappIntent);
                            }catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(getApplicationContext(), "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                }
                 else {
                    refmobile.setVisibility(View.VISIBLE);
                    generate.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("ServiceCast")
                        @Override
                        public void onClick(View v) {
                                genpos = refposition.getSelectedItem().toString().trim();
                                rmobile = refmobile.getText().toString().trim();
                                boolean check = true;
                                Validate validate = new Validate();

                                if (genpos.equals("Select Your Position")) {
                                    check = false;
                                    Toast.makeText(getApplicationContext(), "Please Select your position", Toast.LENGTH_SHORT).show();
                                }
                                if (validate.isValidPhone(rmobile))
                                    rmobile = refmobile.getText().toString();
                                else {
                                    rmobile = "";
                                    check = false;
                                    refmobile.setError("Please enter mobile number of your friend");
                                }
                                if (check) {
                                    pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
                                    if (pref.contains(Config.KEY_NAME)) {
                                        refid = pref.getString(Config.KEY_NAME, "");
                                    }
                                    String phoneNo = refmobile.getText().toString();
                                    String sm = "Download BigEmarrt app https://play.google.com/store/apps/details?id=com.app.dreamswaliya.&hl=en and to refer Website use this link http://income.bigemarrt.com and for your new registration to use reference id -->" + refid + " and position -->" + genpos;
                                    String sms = sm.toString();
                                    try {
                                        SmsManager smsManager = SmsManager.getDefault();
                                        ArrayList<String> parts = smsManager.divideMessage(sms);
                                        smsManager.sendMultipartTextMessage(phoneNo, null, parts, null, null);
                                        Toast.makeText(getApplicationContext(), "SMS Sent to this " + phoneNo + "Number", Toast.LENGTH_LONG).show();
                                        refmobile.setText("");
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "SMS faild, please try again later!", Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }
                                }
                        }
                    });
                 }
            }
        });
    }

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