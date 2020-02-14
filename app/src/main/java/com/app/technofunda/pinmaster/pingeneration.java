package com.app.technofunda.pinmaster;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.app.technofunda.R;
import com.app.technofunda.ewallet.PinTransferActivity;

/**
 * Created by Administrator on 16/03/2017.
 */

public class pingeneration  extends Fragment {
    TextView txtclick1, txtclick2, click1, click2,click3,textavailablepin;
    Context context;
    FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pingeneration_frag,
                container, false);

        context = view.getContext(); // Assign your rootView to context
        txtclick1 = (TextView) view.findViewById(R.id.txtpingeneration);
        txtclick2 = (TextView) view.findViewById(R.id.pintransfer);
        click1 = (TextView) view.findViewById(R.id.click1);
        click2 = (TextView) view.findViewById(R.id.click2);
        click3=(TextView)view.findViewById(R.id.click3);
        textavailablepin=(TextView)view.findViewById(R.id.txtavailablepin);

        txtclick1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FragmentTransaction t = this.getFragmentManager().beginTransaction();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new pintransfer())
                        .commit();
            }
        });
        txtclick2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, PinTransferActivity.class);
                startActivity(intent);
            }
        });
        textavailablepin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container,new Available_pin())
                        .commit();
            }
        });
        Animation animation= AnimationUtils.loadAnimation(context,R.anim.blink);
       // fragmentTransaction.setCustomAnimations(R.anim.blink,R.anim.blink);
        click1.startAnimation(animation);
        click2.startAnimation(animation);
        click3.startAnimation(animation);
        return view;
    }
  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }*/
    }



