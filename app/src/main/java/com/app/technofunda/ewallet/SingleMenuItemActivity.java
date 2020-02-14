package com.app.technofunda.ewallet;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import com.app.technofunda.R;

public class SingleMenuItemActivity extends BaseAdapter
{
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;

    public SingleMenuItemActivity(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;

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

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderVideo holder = null;
        if (convertView == null) {
            holder = new ViewHolderVideo();

            convertView = LayoutInflater.from(activity).inflate(R.layout.list_ewallet_transaction_row, null);
            holder.transid = (TextView) convertView.findViewById(R.id.trans_id);
            holder.tdate = (TextView) convertView.findViewById(R.id.trans_date);
            holder.tdebit = (TextView) convertView.findViewById(R.id.trans_debit);
            holder.tcredit = (TextView) convertView.findViewById(R.id.trans_credit);
            holder.tremark = (TextView) convertView.findViewById(R.id.trans_remark);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolderVideo) convertView.getTag();
        }
        holder.transid.setId(position);
        holder.tdate.setId(position);
        holder.tdebit.setId(position);
        holder.tcredit.setId(position);
        holder.tremark.setId(position);

        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);

        holder.transid.setText(song.get(EwalletActivitys.KEY_TRANSID));
        holder.tdate.setText(song.get(EwalletActivitys.KEY_DATE));
        holder.tdebit.setText(song.get(EwalletActivitys.KEY_DEBIT));
        holder.tcredit.setText(song.get(EwalletActivitys.KEY_CREDIT));
        holder.tremark.setText(song.get(EwalletActivitys.KEY_REMARK));

        return convertView;

    }
}

class ViewHolderVideo
{
    TextView transid, tdate,tdebit,tcredit,tremark;
}