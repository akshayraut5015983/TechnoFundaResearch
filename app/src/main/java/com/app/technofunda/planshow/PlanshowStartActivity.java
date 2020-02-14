package com.app.technofunda.planshow;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.app.technofunda.R;

public class PlanshowStartActivity extends AppCompatActivity {
    TextView textfirst,planshowregistration,planshowlist,textfrth,textfith;
    View first,forth,fifth;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_start_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Typeface pacificoFont = Typeface.createFromAsset(getAssets(), "arialnarrow.ttf");

        first=(View)findViewById(R.id.view_first);
        first.setVisibility(View.INVISIBLE);
        forth=(View)findViewById(R.id.view_frouth);
        forth.setVisibility(View.INVISIBLE);
        fifth=(View)findViewById(R.id.view_fifth);
        fifth.setVisibility(View.INVISIBLE);

        textfirst=(TextView)findViewById(R.id.text_view_f);
        textfirst.setVisibility(View.INVISIBLE);

        textfrth=(TextView)findViewById(R.id.text_view_fr);
        textfrth.setVisibility(View.INVISIBLE);

        textfith=(TextView)findViewById(R.id.text_view_fith);
        textfith.setVisibility(View.INVISIBLE);

//        planshowregistration=(TextView)findViewById(R.id.text_view_s);
//        planshowregistration.setText("Planshow Registration");
//        planshowregistration.setTypeface(pacificoFont);

        planshowlist=(TextView)findViewById(R.id.text_view_t);
        planshowlist.setText("Planshow List");
        planshowlist.setTypeface(pacificoFont);

        planshowregistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PlanshowMobileActivity.class);
                startActivity(i);
            }
        });
        planshowlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PlanshowListActivity.class);
                startActivity(i);
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
