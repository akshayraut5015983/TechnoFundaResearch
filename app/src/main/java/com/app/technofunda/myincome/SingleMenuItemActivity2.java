package com.app.technofunda.myincome;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import com.app.technofunda.R;

public class SingleMenuItemActivity2 extends BaseAdapter
{
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;

    public SingleMenuItemActivity2(Activity a, ArrayList<HashMap<String, String>> d)
    {
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
        if (convertView == null)
        {
        holder = new ViewHolderVideo();

        convertView = LayoutInflater.from(activity).inflate(R.layout.myincome_row_list, null);

        holder.inpname = (TextView) convertView.findViewById(R.id.txt_income_name);
        holder.inpdate = (TextView) convertView.findViewById(R.id.txt_income_date);
        holder.innetpay = (TextView) convertView.findViewById(R.id.txt_income_netpay);

        convertView.setTag(holder);
        }
        else
        {
        holder = (ViewHolderVideo) convertView.getTag();
        }
        holder.inpname.setId(position);
        holder.inpdate.setId(position);
        holder.innetpay.setId(position);

        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);

        holder.inpname.setText(song.get(IncomeHistoryActivity.KEY_PNAME));
        holder.inpdate.setText(song.get(IncomeHistoryActivity.KEY_PDATE));
        holder.innetpay.setText(song.get(IncomeHistoryActivity.KEY_NETPAY));

        return convertView;
    }
}

class ViewHolderVideo
{
    TextView inpname,inpdate,innetpay;
}
