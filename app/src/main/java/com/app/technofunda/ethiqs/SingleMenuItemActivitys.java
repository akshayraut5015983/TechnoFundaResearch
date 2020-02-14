package com.app.technofunda.ethiqs;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.app.technofunda.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SingleMenuItemActivitys extends BaseAdapter
{
    public String TEST,rupee;
    public Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private HashMap<String, String> song;
    private ViewHolderVideo holder;

    public SingleMenuItemActivitys(Activity a, ArrayList<HashMap<String, String>> d)
    {
        activity = a;
        data = d;
    }

    public int getCount() {
        return data.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        holder = new ViewHolderVideo();
        View v;

        if (convertView == null)
        {
            convertView = LayoutInflater.from(activity).inflate(R.layout.registration_pin_row_list, parent, false);
            v = convertView;
        } else
        {
            v = convertView;
        }
        final TextView txtpinnumber = (TextView) v.findViewById(R.id.reg_pin_number);
        holder.txtamount = (TextView) v.findViewById(R.id.pin_amount);
        holder.txtname = (TextView) v.findViewById(R.id.txt_reg_name);
        holder.btnpin = (Button) v.findViewById(R.id.but_pin);

        ReadPin(position);

        rupee = activity.getResources().getString(R.string.Rs);

        txtpinnumber.setText(song.get(PinRegistrationActivity.KEY_PINNUMBER));
        holder.txtamount.setText(rupee+" "+song.get(PinRegistrationActivity.KEY_AMOUNT));
        holder.txtname.setText(song.get(PinRegistrationActivity.KEY_NAMES));

        txtpinnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TEST = txtpinnumber.getText().toString();
                Log.e("Text Id", TEST);
            }
        });

        holder.btnpin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TEST = txtpinnumber.getText().toString();
                Log.e("PIN Get No:", TEST);
                Intent inten = new Intent(activity, NewRegistration.class);
                inten.putExtra("STRING_I_NEED", TEST);
                activity.startActivity(inten);
                activity.finish();
            }
        });

        return convertView;
    }

    private void ReadPin(int position)
    {
        song = new HashMap<String, String>();
        song = data.get(position);
        Log.e("User Id", String.valueOf(song));
    }
}

class ViewHolderVideo
{
    TextView txtamount, txtname;
    Button btnpin;
}